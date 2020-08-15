package com.github.guang19.service.service.cos.template.objecttemplate.impl;

import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.github.guang19.service.service.cos.config.AliyunOSSClientProperties;
import com.github.guang19.service.service.cos.template.objecttemplate.BaseAliyunOSSObjectTemplate;
import com.github.guang19.service.service.cos.util.COSUtil;
import com.github.guang19.service.util.CommonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
        Path path = CommonUtil.checkLocalFile(filePath);
        //计算对象的key
        String key = (cosDir.isEmpty() || cosDir.trim().isEmpty()) ? path.getFileName().toString() : cosDir+path.getFileName().toString();
        try
        {
            return putObjectToOSS(Files.newInputStream(path),key,Files.probeContentType(path));
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while cos upload object : {}" , COSUtil.parseAliyunErrorMessage(e));
        }
        catch (Exception e)
        {
            LOGGER.error("an error occurred while cos upload object : {}" ,e.getMessage());
        }
        //返回对象url
        return CommonUtil.endWithImgType(filePath) ? getImgObjectUrl(key) : getObjectUrl(key);
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
        String key = (cosDir.isEmpty() || cosDir.trim().isEmpty()) ? objectName : cosDir + objectName;
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
            //返回对象url
            return CommonUtil.endWithImgType(key) ? getImgObjectUrl(key) : getObjectUrl(key);
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while cos upload object : {}",COSUtil.parseAliyunErrorMessage(e));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("an error occurred while cos upload object : {}", e.getMessage());
            return null;
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
        CommonUtil.assertObjectNull(key,"key cannot be null.");
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
            LOGGER.error("an error occurred while oss download file : {}", COSUtil.parseAliyunErrorMessage(e));
        }
        catch (Throwable e)
        {
            LOGGER.error("an error occurred while oss download file : {}", e.getMessage());
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
        CommonUtil.checkLocalDir(saveDir);
        List<String> keys = getAllFileKeys();
        try
        {
            for (String key : keys)
            {
                downloadSmallFile(key,saveDir.endsWith("/") ? saveDir+key : saveDir + "/" + key);
            }
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while oss download file : {}" ,COSUtil.parseAliyunErrorMessage(e));
        }
        catch (Exception e)
        {
            LOGGER.error("an error occurred while oss download file : {}" , e.getMessage());
        }
        finally
        {
            close();
        }
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
        CommonUtil.assertObjectsNull(sourceKey,targetBucketName,targetKey);
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
            //因为复制到另一个存储桶肯定不是相同的域名,所以cname为null
            return COSUtil.getAliyunObjectUrl(targetBucketName,getEndpoint(),null,null,targetKey);
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while oss copy file : {}" ,COSUtil.parseAliyunErrorMessage(e));
            return null;
        }
        catch (Throwable e)
        {
            LOGGER.error("an error occurred while oss copy file : {}" ,e.getMessage());
            return null;
        }
        finally
        {
            close();
        }
    }
}
