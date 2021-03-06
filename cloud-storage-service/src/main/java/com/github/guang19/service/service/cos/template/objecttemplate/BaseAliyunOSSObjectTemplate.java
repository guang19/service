package com.github.guang19.service.service.cos.template.objecttemplate;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.github.guang19.service.service.cos.config.AliyunOSSClientProperties;
import com.github.guang19.service.service.cos.template.buckettemplate.BaseAliyunOSSBucketTemplate;
import com.github.guang19.service.service.cos.util.COSUtil;
import com.github.guang19.service.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yangguang
 * @date 2020/2/7
 * @description <p>阿里云OSS基础操作模板</p>
 */
public abstract class BaseAliyunOSSObjectTemplate implements AliyunOSSObjectTemplate
{
    //当前模板endpoint
    private final String endpoint;

    //当前模板的自定义域名
    private final String cname;

    //当前模板存储桶
    private String objectTemplateBucket;

    //图片上传时的样式
    private String uploadImgStyle;

    private int uploadLimitSize;

    //阿里云OSS客户端
    protected final OSS ossClient;

    //LOGGER
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseAliyunOSSBucketTemplate.class);


    /**
     * 阿里云OSS客户端属性构造
     * @param ossClientProperties   OSS客户端属性
     */
    protected BaseAliyunOSSObjectTemplate(AliyunOSSClientProperties ossClientProperties)
    {
        this.endpoint = ossClientProperties.getEndpoint();
        this.cname = ossClientProperties.getCname();
        this.objectTemplateBucket = ossClientProperties.getObjectTemplateBucket();
        this.uploadImgStyle = ossClientProperties.getUploadImgStyle();
        this.uploadLimitSize = ossClientProperties.getUploadLimitSize();
        this.ossClient = new OSSClient(cname != null ? cname : endpoint, ossClientProperties.getCredentialsProvider(),
                COSUtil.newAliyunOSSClientConfig(ossClientProperties));
    }

    /**
     * <p>
     * 获取当前存储桶下的所有对象的key,返回的对象包括目录和文件
     * 返回的对象数量限制为1000
     * </p>
     *
     * @return 对象key集合
     */
    @Override
    public List<String> getAllObjects()
    {
        return getAllObjectsWithKeyPrefix("");
    }

    /**
     * <p>
     * 查询拥有相同前缀key的所有对象的key,返回的对象包括目录和文件
     * </p>
     *
     * @param keyPrefix 对象key前缀,如: img/ , file/upload/ , file/img/upload , file/img/upload/jquery.js(如果keyPrefix为文件,那么仅仅会返回这个文件)
     * @return 对象key集合
     */
    @Override
    public List<String> getAllObjectsWithKeyPrefix(String keyPrefix)
    {
        CommonUtil.assertObjectNull(keyPrefix,"key prefix cannot be null.");
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(objectTemplateBucket);
        listObjectsRequest.setPrefix(keyPrefix);
        listObjectsRequest.setDelimiter("");
        listObjectsRequest.setMaxKeys(1000);
        List<String> keys = new LinkedList<>();
        ObjectListing objectListing = null;
        try
        {
            do
            {
                objectListing = ossClient.listObjects(listObjectsRequest);
                objectListing.getObjectSummaries().forEach(ossObjectSummary -> keys.add(ossObjectSummary.getKey()));
                listObjectsRequest.setMarker(objectListing.getNextMarker());
            } while (objectListing.isTruncated());
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while cos client batch get object : {}",COSUtil.parseAliyunErrorMessage(e));
        }
        finally
        {
            close();
        }
        return keys;
    }

    /**
     * <p>
     * 获取当前存储桶下的所有文件名
     * </p>
     *
     * @return 文件名集合
     */
    @Override
    public List<String> getAllFileNames()
    {
        return getAllFileNamesWithKeyPrefix("");
    }

    /**
     * <p>
     * 查询拥有相同前缀key的所有文件名
     * </p>
     *
     * @param keyPrefix 文件key前缀,如: img/ , file/upload/ , file/img/upload , file/img/upload/jquery.js(如果keyPrefix为文件,那么仅仅会返回这个文件)
     * @return 文件名集合
     */
    @Override
    public List<String> getAllFileNamesWithKeyPrefix(String keyPrefix)
    {
        return getAllObjectsWithKeyPrefix(keyPrefix)
                .parallelStream()
                .filter(key -> !key.endsWith("/"))
                .map(key ->
                {
                    String[] splitArr = key.split("/");
                    return splitArr[splitArr.length - 1];
                })
                .collect(Collectors.toList());
    }

    /**
     * <p>获取所有文件的key</p>
     *
     * @return 文件key集合 ：　img/a.jpg , img/a/b/c.jpg , d.jpg
     */
    @Override
    public List<String> getAllFileKeys()
    {
        return getAllObjectsWithKeyPrefix("")
                .stream()
                .filter(key -> !key.endsWith("/"))
                .collect(Collectors.toList());
    }

    /**
     * 获取对象的元信息
     *
     * @param key 对象key
     * @return 对象元信息键值对
     * <code>
     * ETag:       xxxxxxxxxxxxxxxxxx
     * Connection: keep-alive
     * x-cos-request-id:xxxxxxxxxxxxxxxxxxxx
     * Last-Modified   :Wed Sep 25 16:22:02 CST 2019
     * Content-Length  :1024
     * Date            :Tue, 04 Feb 2020 05:03:03 GMT
     * Content-Type    :image/png
     * </code>
     */
    @Override
    public Map<String, Object> getObjectMetaData(String key)
    {
        CommonUtil.assertObjectNull(key,"key cannot be null.");
        try
        {
            ObjectMetadata objectMetadata = ossClient.getObjectMetadata(objectTemplateBucket, key);
            if(objectMetadata != null)
            {
                return objectMetadata.getRawMetadata();
            }
        }catch (OSSException e)
        {
            LOGGER.error("an error occurred while oss get object metadata : {}" , COSUtil.parseAliyunErrorMessage(e));
        }
        finally
        {
            close();
        }
        return null;
    }

    /**
     * <p>根据对象key删除当前存储桶下的对象</p>
     *
     * @param key 对象的key
     */
    @Override
    public void deleteObjectWithKey(String key)
    {
        CommonUtil.assertObjectNull(key,"key cannot be null.");
        try
        {
            ossClient.deleteObject(objectTemplateBucket,key);
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while oss delete object : {}", COSUtil.parseAliyunErrorMessage(e));
        }
        finally
        {
            close();
        }
    }

    /**
     * <p>根据对象键集合,批量删除当前存储桶下的对象</p>
     *
     * @param keys 对象键集合
     */
    @Override
    public void deleteObjectsWithKeys(List<String> keys)
    {
        CommonUtil.assertCollectionEmpty(keys,"keys cannot be empty.");
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(objectTemplateBucket).withKeys(keys);
        try
        {
            ossClient.deleteObjects(deleteObjectsRequest);
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while cos batch delete object : {}" , COSUtil.parseAliyunErrorMessage(e));
        }
        finally
        {
            close();
        }
    }

    /**
     * <p>根据url删除当前存储桶对象</p>
     *
     * @param url 对象的url
     */
    @Override
    public void deleteObjectWithUrl(String url)
    {
        CommonUtil.assertObjectNull(url,"url cannot be null.");
        String domainPrefix = COSUtil.getAliyunObjectUrlPrefix(objectTemplateBucket,endpoint,cname);
        if(!url.startsWith(domainPrefix))
        {
            throw new IllegalArgumentException("url must be aliyun url.");
        }
        else
        {
            deleteObjectWithKey(url.replace(domainPrefix,""));
        }
    }

    /**
     * <p>根据url集合,批量删除当前存储桶对象</p>
     *
     * @param urls url集合
     */
    @Override
    public void deleteObjectsWithUrls(List<String> urls)
    {
        CommonUtil.assertCollectionEmpty(urls,"urls cannot be empty.");
        String domainPrefix = COSUtil.getAliyunObjectUrlPrefix(objectTemplateBucket,endpoint,cname);
        deleteObjectsWithKeys(urls.stream().filter(url -> url.startsWith(domainPrefix)).map(url -> url.replace(domainPrefix,"")).collect(Collectors.toList()));
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
        throw new UnsupportedOperationException("can not upload file with base template.");
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
        throw new UnsupportedOperationException("can not upload file with base template.");
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
        throw new UnsupportedOperationException("can not upload file with base template.");
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
        throw new UnsupportedOperationException("can not upload file with base template.");
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
     *                 G:/dir/b
     */
    @Override
    public void downloadFile(String key, String saveFile)
    {
        throw new UnsupportedOperationException("can not download file with base template.");
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
        throw new UnsupportedOperationException("can not download file with base template.");
    }

    /**
     * <p>关闭cos客户端连接</p>
     */
    @Override
    public void close()
    {
        ossClient.shutdown();
    }

    /**
     * 获取当前模板的存储桶
     * @return      存储桶
     */
    public String getObjectTemplateBucket()
    {
        return objectTemplateBucket;
    }

    /**
     * 设置当前模板的存储桶
     * @param objectTemplateBucket  存储桶
     */
    public void setObjectTemplateBucket(String objectTemplateBucket)
    {
        this.objectTemplateBucket = objectTemplateBucket;
    }

    /**
     * 获取图片上传时的样式
     * @return  图片上传时的样式
     */
    public String getUploadImgStyle()
    {
        return uploadImgStyle;
    }

    /**
     * 设置图片上传时的样式
     * @param uploadImgStyle    图片上传时的样式
     */
    public void setUploadImgStyle(String uploadImgStyle)
    {
        this.uploadImgStyle = uploadImgStyle;
    }

    /**
     * 获取对象对外访问的域名
     * @return      对象对外访问的域名
     */
    public String getEndpoint()
    {
        return endpoint;
    }

    /**
     * 获取自定义域名
     * @return  自定义域名
     */
    public String getCname()
    {
        return cname;
    }

    /**
     * <p>获取上传对象的大小限制</p>
     * @return  上传对象的大小限制
     */
    public int getUploadLimitSize()
    {
        return uploadLimitSize;
    }

    /**
     * <p>根据对象键生成对象url</p>
     * @param objectKey     对象键
     * @return              对象url
     */
    protected final String getObjectUrl(String objectKey)
    {
        return COSUtil.getAliyunObjectUrl(objectTemplateBucket,endpoint,cname,null,objectKey);
    }

    /**
     * <p>根据图片对象键生成对象url</p>
     * @param objectKey 对象键
     * @return          对象url
     */
    protected final String getImgObjectUrl(String objectKey)
    {
        return COSUtil.getAliyunObjectUrl(objectTemplateBucket,endpoint,cname,uploadImgStyle,objectKey);
    }


    /**
     * 下载小文件
     * @param key           对象key
     * @param saveFile      保存到文件
     * @throws IOException   IO异常
     */
    protected final void downloadSmallFile(String key,String saveFile) throws IOException
    {
        ossClient.getObject(new GetObjectRequest(getObjectTemplateBucket(),key),CommonUtil.createFile(saveFile));
    }

    /**
     * 下载大文件
     * @param key           对象key
     * @param saveFile      保存到文件
     * @throws Throwable    throwable
     */
    protected final void downloadLargeFile(String key,String saveFile) throws Throwable
    {
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(getObjectTemplateBucket(),key);
        downloadFileRequest.setDownloadFile(saveFile);
        downloadFileRequest.setPartSize(COSUtil.getLargeObjectPartSize());
        downloadFileRequest.setTaskNum(5);
        downloadFileRequest.setEnableCheckpoint(true);
        downloadFileRequest.setCheckpointFile(saveFile + "checkpoint");
        ossClient.downloadFile(downloadFileRequest);
    }


    /**
     * 拷贝小对象
     * @param sourceKey         源对象key
     * @param targetBucketName  目标存储桶名
     * @param targetKey         目标对象key
     */
    protected final void copySmallObject(String sourceKey, String targetBucketName, String targetKey)
    {
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(getObjectTemplateBucket(),sourceKey,targetBucketName,targetKey);
        ossClient.copyObject(copyObjectRequest);
    }

    /**
     * 拷贝大对象
     * @param contentLength         对象长度
     * @param sourceKey             源对象key
     * @param targetBucketName      目标存储桶
     * @param targetKey             目标key
     */
    protected final void copyLargeObject(long contentLength, String sourceKey, String targetBucketName, String targetKey)
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
