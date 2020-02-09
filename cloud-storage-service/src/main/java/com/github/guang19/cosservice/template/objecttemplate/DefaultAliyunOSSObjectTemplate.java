package com.github.guang19.cosservice.template.objecttemplate;

import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.github.guang19.cosservice.config.AliyunOSSClientProperties;
import com.github.guang19.cosservice.util.COSUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * @author yangguang
 * @date 2020/2/7
 * @description <p>阿里云默认OSS对象操作模板</p>
 */
public class DefaultAliyunOSSObjectTemplate extends BaseAliyunOSSObjectTemplate
{

    /**
     * 阿里云OSS客户端属性构造
     * @param ossClientProperties  OSS客户段属性
     */
    public DefaultAliyunOSSObjectTemplate(AliyunOSSClientProperties ossClientProperties)
    {
        super(ossClientProperties);
    }

    /**
     * <p>上传文件到存储桶的默认目录,注意,是文件路径,不是目录</p>
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
     * @param filePath 本地文件路径
     * @return 上传成功后, 对象的url
     */
    @Override
    public String uploadFile(String cosDir, String filePath)
    {
        //校验参数
        COSUtil.checkCOSDir(cosDir);
        Path path = COSUtil.checkLocalFile(filePath);
        //计算对象的key
        String key = cosDir.isBlank() ? path.getFileName().toString() : cosDir.concat(path.getFileName().toString());;
        try
        {
            return putObjectToOSS(Files.newInputStream(path),key,Files.probeContentType(path));
        }
        catch (OSSException e)
        {
            logger.error("error during cos client upload object : ".concat(COSUtil.parseAliyunErrorMessage(e)));
        }
        catch (Exception e)
        {
            logger.error("error during cos client upload object : ".concat(e.getMessage()));
        }
        //返回对象url
        return COSUtil.endWithImgType(key) ? getImgObjectUrl(key) : getObjectUrl(key);
    }

    /**
     * <p>上传对象到存储桶的默认目录</p>
     *
     * @param fileStream 对象的输入流
     * @param objectName 指定上传后的对象名,但不需要指定后缀,如: a.jpg, a , b.jpg , b , cat 都行
     * @return 上传成功后, 对象的url
     */
    @Override
    public String uploadFile(InputStream fileStream, String objectName)
    {
        return uploadFile(fileStream,"",objectName);
    }

    /**
     * <p>上传对象到存储桶</p>
     *
     * @param fileStream 对象的输入流
     * @param cosDir        需要将对象上传到存储桶的哪个目录,必须以 '/' 结尾,允许空串
     * @param objectName 对象名,但不需要指定后缀,如: a.jpg, a , b.jpg , b , cat 都行
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
        return putObjectToOSS(fileStream,key,null);
    }

    /**mulu
     * <p>
     *     对象上传的核心方法
     * </p>
     * @param inputStream   对象内容流
     * @param key           对象key
     * @param contentType   对象类型: image/png,image/jpg...
     * @return              对象在cos上的url
     */
    private String putObjectToOSS(InputStream inputStream,String key,String contentType)
    {
        try
        {
            //校验流
            int size =  COSUtil.checkObjectInputStream(inputStream,getUploadLimitSize());
            //设置对象元信息
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(size);
            if(contentType != null)
            {
                objectMetadata.setContentType(contentType);
            }
            PutObjectRequest putObjectRequest = new PutObjectRequest(getObjectTemplateBucket(),key,inputStream,objectMetadata);
            //上传
            ossClient.putObject(putObjectRequest);
        }
        catch (OSSException e)
        {
            throw new OSSException("error during cos client upload object : ".concat(COSUtil.parseAliyunErrorMessage(e)));
        }
        catch (Exception e)
        {
            throw new OSSException("error during cos client upload object : ".concat(e.getMessage()));
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {}
            close();
        }
        //返回对象url
        return COSUtil.endWithImgType(key) ? getImgObjectUrl(key) : getObjectUrl(key);
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
            long contentLength = ossClient.getObjectMetadata(getObjectTemplateBucket(),key).getContentLength();
            if((float)contentLength / 1024 / 1024 < 512)
            {
                downloadSmallFile(key,saveFile);
            }
            else
            {
                downloadLargeFile(key,saveFile);
            }
        }
        catch (OSSException e)
        {
            logger.error("error during  download file : ".concat(COSUtil.parseAliyunErrorMessage(e)));
        }
        catch (Throwable e)
        {
            logger.error("error during  download file : ".concat(e.getMessage()));
        }
        finally
        {
            close();
        }
    }

    /**
     * <p>
     * 把当前存储桶下的所有文件都下载到本地
     * 此方法的耗时取决于当前存储桶的文件数量和文件大小
     * </p>
     *
     * @param saveDir 指定本地目录
     */
    @Override
    public void downloadAllFiles(String saveDir)
    {
        COSUtil.checkLocalDir(saveDir);
        List<String> keys = getAllFileKeys();
        try
        {
            for (String key : keys)
            {
                downloadSmallFile(key,saveDir.endsWith("/") ? saveDir.concat(key) : saveDir.concat("/").concat(key));
            }
        }
        catch (OSSException e)
        {
            logger.error("error during download file : ".concat(COSUtil.parseAliyunErrorMessage(e)));
        }
        catch (Exception e)
        {
            logger.error("error during download file : ".concat(e.getMessage()));
        }
        finally
        {
            close();
        }
    }

    //下载小文件
    private void downloadSmallFile(String key,String saveFile) throws Exception
    {
        ossClient.getObject(new GetObjectRequest(getObjectTemplateBucket(),key),COSUtil.createFile(saveFile));
    }

    //下载大文件
    private void downloadLargeFile(String key,String saveFile) throws Throwable
    {
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(getObjectTemplateBucket(),key);
        downloadFileRequest.setDownloadFile(saveFile);
        downloadFileRequest.setPartSize(COSUtil.getLargeObjectPartSize());
        downloadFileRequest.setTaskNum(5);
        downloadFileRequest.setEnableCheckpoint(true);
        downloadFileRequest.setCheckpointFile(saveFile.concat("checkpoint"));
        ossClient.downloadFile(downloadFileRequest);
    }

    /**
     * <p>从当前存储桶复制对象到目标存储桶,不支持目标跨域拷贝</p>
     *
     * @param sourceKey        原对象key
     * @param targetBucketName 目标存储桶
     * @param targetKey        目标对象key
     * @return 复制后的对象的url
     */
    @Override
    public String copyObject(String sourceKey, String targetBucketName, String targetKey)
    {
        COSUtil.assertObjectsNull(sourceKey,targetBucketName,targetKey);
        try
        {
            long contentLength = ossClient.getObjectMetadata(getObjectTemplateBucket(),sourceKey).getContentLength();
            //拷贝小对象
            if((float)contentLength / 1024 / 1024 < 512)
            {
                copySmallObject(sourceKey,targetBucketName,targetKey);
            }
            //拷贝大对象
            else
            {
                copyLargeObject(contentLength,sourceKey,targetBucketName,targetKey);
            }
        }
        catch (OSSException e)
        {
            logger.error("error during copy file : ".concat(COSUtil.parseAliyunErrorMessage(e)));
        }
        catch (Throwable e)
        {
            logger.error("error during copy file : ".concat(e.getMessage()));
        }
        finally
        {
            close();
        }
        //因为复制到另一个存储桶肯定不是相同的域名,所以cname为null
        return COSUtil.getAliyunObjectUrl(targetBucketName,getEndpoint(),null,null,targetKey);
    }


    //拷贝小对象
    private void copySmallObject(String sourceKey, String targetBucketName, String targetKey)
    {
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(getObjectTemplateBucket(),sourceKey,targetBucketName,targetKey);
        ossClient.copyObject(copyObjectRequest);
    }


    //拷贝大对象
    private void copyLargeObject(long contentLength, String sourceKey, String targetBucketName, String targetKey)
    {
        //计算分片总数
        long partSize = COSUtil.getLargeObjectPartSize();
        int partCount = (int)(contentLength / partSize);
        if(contentLength % partSize != 0)
        {
            ++partCount;
        }
        InitiateMultipartUploadRequest multipartUploadRequest = new InitiateMultipartUploadRequest(targetBucketName,targetKey);
        InitiateMultipartUploadResult multipartUploadResult = ossClient.initiateMultipartUpload(multipartUploadRequest);
        UploadPartCopyRequest copyRequest = new UploadPartCopyRequest(getObjectTemplateBucket(),sourceKey,targetBucketName,targetKey);
        copyRequest.setUploadId(multipartUploadResult.getUploadId());
        List<PartETag> partETags = new LinkedList<>();
        for(int i = 0 ; i < partCount ; ++i)
        {
            //已经拷贝过的大小
            long alreadyCopy = partSize * i;
            long size = Math.min(partSize, contentLength - alreadyCopy);
            copyRequest.setPartSize(size);
            copyRequest.setBeginIndex(alreadyCopy);
            copyRequest.setPartNumber(i + 1);
            UploadPartCopyResult copyResult = ossClient.uploadPartCopy(copyRequest);
            partETags.add(copyResult.getPartETag());
        }
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(targetBucketName,targetKey,multipartUploadResult.getUploadId(),partETags);
        ossClient.completeMultipartUpload(completeMultipartUploadRequest);
    }
}
