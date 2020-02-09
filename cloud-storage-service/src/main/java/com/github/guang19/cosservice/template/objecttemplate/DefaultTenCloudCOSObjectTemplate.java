package com.github.guang19.cosservice.template.objecttemplate;

import com.github.guang19.cosservice.config.TenCloudCOSClientProperties;
import com.github.guang19.cosservice.util.COSUtil;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.transfer.Copy;
import com.qcloud.cos.transfer.Download;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author yangguang
 * @date 2020/2/4
 * @description <p>默认腾讯云COS操作模板</p>
 */
public class DefaultTenCloudCOSObjectTemplate extends BaseTenCloudCOSObjectTemplate
{

    /**
     * 构造腾讯云COS对象操作基础模板
     *
     * @param cosClientProperties cos客户端属性
     */
    public DefaultTenCloudCOSObjectTemplate(TenCloudCOSClientProperties cosClientProperties)
    {
        super(cosClientProperties);
    }


    /**
     * <p>上传文件到存储桶的默认目录</p>
     *
     * @param filePath 本地文件路径
     * @return 上传成功后, 对象的url
     */
    @Override
    public String uploadFile(String filePath)
    {
       return uploadFile("",filePath);
    }

    /**
     * <p>上传文件到存储桶</p>
     *
     * @param cosDir      需要将对象上传到存储桶的哪个目录,必须以 '/' 结尾,允许空串
     * @param filePath    本地文件路径
     * @return 上传成功后, 对象的url
     */
    @Override
    public String uploadFile(String cosDir, String filePath)
    {
        //校验参数
        COSUtil.checkCOSDir(cosDir);
        Path path = COSUtil.checkLocalFile(filePath);
        String key = null;
        try
        {
            //计算对象的key
            key = cosDir.isBlank() ? path.getFileName().toString() : cosDir.concat(path.getFileName().toString());
            return putObjectToCOS(Files.newInputStream(path),key,Files.probeContentType(path));
        }
        catch (Exception e)
        {
            logger.error("error during cos client upload object : ".concat(e.getMessage()));
        }
        return getObjectUrl(key);
    }

    /**
     * <p>上传文件到存储桶的默认目录</p>
     *
     * @param fileStream 文件的输入流
     * @param objectName      指定上传后的对象名,但不需要指定后缀,如: a.jpg, a , b.jpg , b , cat 都行
     * @return 上传成功后, 对象的url
     */
    @Override
    public String uploadFile(InputStream fileStream, String objectName)
    {
        return uploadFile(fileStream,"",objectName);
    }

    /**
     * <p>上传文件到存储桶</p>
     *
     * @param fileStream 文件的输入流
     * @param cosDir        需要将对象上传到存储桶的哪个目录,必须以 '/' 结尾,允许空串
     * @param objectName   文件名,但不需要指定后缀,如: a.jpg, a , b.jpg , b , cat 都行
     * @return 上传成功后, 对象的url
     */
    @Override
    public String uploadFile(InputStream fileStream, String cosDir, String objectName)
    {
        //校验参数
        COSUtil.checkCOSDir(cosDir);
        COSUtil.checkObjectName(objectName);
        //计算对象的key
        String key = cosDir.isBlank() ? objectName : cosDir.concat(objectName);
        return putObjectToCOS(fileStream,key,null);
    }

    /**mulu
     * <p>
     *     对象上传的核心方法,此方法是阻塞的,所以整个模板上传对象的过程都是阻塞的.
     * </p>
     * @param inputStream   对象内容流
     * @param key           对象key
     * @param contentType   对象类型: image/png,image/jpg...
     * @return              对象在cos上的url
     */
    private String putObjectToCOS(InputStream inputStream,String key,String contentType)
    {
        try
        {
            //校验对象流的大小
            int size = COSUtil.checkObjectInputStream(inputStream,getUploadLimitSize());
            //设置对象元信息
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(size);
            if(contentType != null)
            {
                objectMetadata.setContentType(contentType);
            }
            PutObjectRequest putObjectRequest = new PutObjectRequest(getObjectTemplateBucket(),key,inputStream,objectMetadata);
            //阻塞上传
            Upload upload = transferManager.upload(putObjectRequest);
            upload.waitForUploadResult();
        }
        catch (Exception e)
        {
            logger.error("error during cos client upload object : ".concat(e.getMessage()));
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {}
            transferManager.shutdownNow();
        }
        //返回对象url
        return getObjectUrl(key);
    }

    /**
     * <p>下载文件到本地</p>
     *
     * @param key      文件的key
     * @param saveFile 指定下载后的文件,这个文件不需要存在
     *                 比如你需要下载的文件key为: img/a.jpg,
     *                 <p>
     *                 那么你可以指定filepath为 :
     *                 <code>
     *                 a.jpg
     *                 <p>
     *                 b.jpg")
     *                 <p>
     *                 /usr/dir/img.jpg
     *
     *                 <p>无需指定下载的文件类型</p>
     *                 /usr/dir/a
     *                 <p>
     */
    @Override
    public void downloadFile(String key, String saveFile)
    {
        COSUtil.assertObjectNull(key);
        COSUtil.checkVirtualFile(saveFile);
        try
        {
            GetObjectRequest getObjectRequest = new GetObjectRequest(getObjectTemplateBucket(),key);
            Download download = transferManager.download(getObjectRequest, new File(saveFile));
            download.waitForCompletion();
        }
        catch (Exception e)
        {
            logger.error("error during cos client download file : ".concat(e.getMessage()));
        }
        finally
        {
            transferManager.shutdownNow();
        }
    }

    /**
     * <p>
     *     把当前存储桶下的所有文件都下载到本地
     *     此方法的耗时取决于当前存储桶的文件数量和文件大小
     * </p>
     * @param saveDir   指定本地目录
     */
    @Override
    public void downloadAllFiles(String saveDir)
    {
        COSUtil.checkLocalDir(saveDir);
        List<String> keys = getAllFileKeys();
        GetObjectRequest getObjectRequest = new GetObjectRequest(getObjectTemplateBucket(),"");
        try
        {
            for (String key : keys)
            {
                getObjectRequest.setKey(key);
                Download download = transferManager.download(getObjectRequest,new File(saveDir.endsWith("/") ? saveDir.concat(key) : saveDir.concat("/").concat(key)));
                download.waitForCompletion();
            }
        }
        catch (Exception e)
        {
            logger.error("error during cos client download file : ".concat(e.getMessage()));
        }
        finally
        {
            transferManager.shutdownNow();
        }
    }

    /**
     * <p>从当前存储桶复制对象到目标存储桶,存储桶无需填写 APPId,支持目标跨域拷贝</p>
     *
     * @param sourceKey        原对象key
     * @param targetRegion     目标地域
     * @param targetBucketName 目标存储桶,无需填写 APPId
     * @param targetKey        目标对象key
     * @return 复制后的对象的url
     */
    @Override
    public String copyObject(String sourceKey, String targetRegion, String targetBucketName, String targetKey)
    {
        COSUtil.assertObjectsNull(sourceKey,targetRegion,targetBucketName,targetKey);
        try
        {
            TransferManager copyTransferManager = newCopyTransferManager(targetRegion);
            Copy copy = copyTransferManager.copy(newCopyObjectRequest(sourceKey,(targetBucketName = getStandardBucketName(targetBucketName)), targetKey),newCopySourceCOSClient(),null);
            copy.waitForCopyResult();
        }
        catch (Exception e)
        {
            logger.error("error during cos client copy file : ".concat(e.getMessage()));
        }
        finally
        {
            transferManager.shutdownNow();
        }
        return COSUtil.getTencloudObjectUrl(targetBucketName,targetRegion,targetKey);
    }
}
