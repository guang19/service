package com.github.guang19.cos.template.objecttemplate;

import com.github.guang19.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.cos.util.COSUtil;
import com.github.guang19.util.CommonUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author yangguang
 * @date 2020/2/3
 * @description <p>腾讯云COS操作基础模板</p>
 */

public abstract class BaseTenCloudCOSObjectTemplate  implements TenCloudCOSObjectTemplate
{
    //腾讯云COS客户端属性
    private final TenCloudCOSClientProperties cosClientProperties;

    //腾讯云cos操作高级接口
    protected final TransferManager transferManager;

    //cos 客户端
    private final COSClient cosClient;

    //当前模板地域
    private String region;

    //当前模板存储桶
    private String objectTemplateBucket;

    //存储拷贝对象时使用的TransferManager
    private final Map<String,TransferManager> copyTransferManagerMap = new ConcurrentHashMap<>();

    //logger
    protected final Logger logger = LoggerFactory.getLogger(BaseTenCloudCOSObjectTemplate.class);

    /**
     * 构造腾讯云COS对象操作基础模板
     * @param cosClientProperties     腾讯云cos客户端属性
     */
    protected BaseTenCloudCOSObjectTemplate(TenCloudCOSClientProperties cosClientProperties)
    {
        this.cosClientProperties = cosClientProperties;
        this.region = cosClientProperties.getRegion();
        this.objectTemplateBucket = getStandardBucketName(cosClientProperties.getObjectTemplateBucket());
        this.cosClient = new COSClient(cosClientProperties.getCosCredentials(), COSUtil.newTenCloudCOSClientConfig(cosClientProperties));
        this.transferManager = new TransferManager(cosClient,COSUtil.newThreadPoolExecutor(cosClientProperties));
        this.copyTransferManagerMap.put(region,transferManager);
    }


    /**
     * <p>
     *     获取当前存储桶下的所有对象的key,返回的对象包括目录和文件
     *     返回的对象数量限制为1000
     * </p>
     * @return      对象key集合
     */
    @Override
    public List<String> getAllObjects()
    {
        return getAllObjectsWithKeyPrefix("");
    }

    /**
     * <p>
     *     查询拥有相同前缀key的所有对象的key,返回的对象包括目录和文件
     * </p>
     * @param keyPrefix    对象key前缀,如: img/ , file/upload/ , file/img/upload , file/img/upload/jquery.js(如果keyPrefix为文件,那么仅仅会返回这个文件)
     * @return             对象key集合
     */
    @Override
    public List<String> getAllObjectsWithKeyPrefix(String keyPrefix)
    {
        CommonUtil.assertObjectNull("key prefix",keyPrefix);
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(objectTemplateBucket);
        listObjectsRequest.setPrefix(keyPrefix);
        listObjectsRequest.setDelimiter("");
        listObjectsRequest.setMaxKeys(1000);
        LinkedList<String> keys = new LinkedList<>();
        String nextMarker = null;
        ObjectListing objectListing = null;
        try
        {
            do
            {
                objectListing = cosClient.listObjects(listObjectsRequest);
                objectListing.getObjectSummaries().forEach(cosObjectSummary -> keys.add(cosObjectSummary.getKey()));
                nextMarker = objectListing.getNextMarker();
                listObjectsRequest.setMarker(nextMarker);
            }while (objectListing.isTruncated());

        }
        catch (CosClientException e)
        {
            logger.error("error during cos client batch get object : ".concat(e.getMessage()));
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
        return getAllObjectsWithKeyPrefix("")
                .parallelStream()
                .filter(key -> !key.endsWith("/"))
                .map(key->
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
     * @param key 对象key
     * @return     对象元信息键值对
     *          <code>
     *              ETag:       xxxxxxxxxxxxxxxxxx
     *              Connection: keep-alive
     *              x-cos-request-id:xxxxxxxxxxxxxxxxxxxx
     *              Last-Modified   :Wed Sep 25 16:22:02 CST 2019
     *              Content-Length  :1024
     *              Date            :Tue, 04 Feb 2020 05:03:03 GMT
     *              Content-Type    :image/png
     *          </code>
     */
    @Override
    public Map<String, Object> getObjectMetaData(String key)
    {
        CommonUtil.assertObjectNull("key",key);
        Map<String,Object> metadata = null;
        try
        {
            ObjectMetadata objectMetadata = cosClient.getObjectMetadata(objectTemplateBucket, key);
            if(objectMetadata != null)
            {
                metadata = objectMetadata.getRawMetadata();
            }
        }
        catch (CosClientException e)
        {
            logger.error("error during get object metadata : ".concat(e.getMessage()));
        }
        finally
        {
            close();
        }
        return metadata;
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
        throw new UnsupportedOperationException("can not upload file with base template");
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
        throw new UnsupportedOperationException("can not upload file with base template");
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
        throw new UnsupportedOperationException("can not upload file with base template");
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
        throw new UnsupportedOperationException("can not upload file with base template");
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
        throw new UnsupportedOperationException("can not download file with base template");
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
        throw new UnsupportedOperationException("can not download file with base template");
    }

    /**
     * <p>根据对象key删除当前存储桶下的对象</p>
     *
     * @param key 对象的key
     */
    @Override
    public void deleteObjectWithKey(String key)
    {
       CommonUtil.assertObjectNull("key",key);
       try
       {
           cosClient.deleteObject(objectTemplateBucket,key);
       }
       catch (CosClientException e)
       {
           logger.error("error during delete object : ".concat(e.getMessage()));
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
        CommonUtil.assertListEmpty("keys",keys);
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(objectTemplateBucket);
        deleteObjectsRequest.setKeys(keys.stream().map(DeleteObjectsRequest.KeyVersion::new).collect(Collectors.toList()));
        try
        {
            cosClient.deleteObjects(deleteObjectsRequest);
        }
        catch (CosClientException e)
        {
            logger.error("error during cos client batch delete object : ".concat(e.getMessage()));
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
        CommonUtil.assertObjectNull("url",url);
        String domainPrefix = COSUtil.getTenCloudObjectUrlPrefix(objectTemplateBucket,region);
        if(!url.startsWith(domainPrefix))
        {
            throw new IllegalArgumentException("url must be tencent cloud url and region and bucket must be reasonable");
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
        CommonUtil.assertListEmpty("urls",urls);
        String domainPrefix = COSUtil.getTenCloudObjectUrlPrefix(objectTemplateBucket,region);
        deleteObjectsWithKeys(urls.stream().filter(url->url.startsWith(domainPrefix)).map(url->url.replace(domainPrefix,"")).collect(Collectors.toList()));
    }

    /**
     * <p>从当前存储桶复制对象到目标存储桶,存储桶无需填写 APPId,支持目标跨域拷贝</p>
     *
     * @param sourceKey        原对象key
     * @param targetRegion     目标地域
     * @param targetBucketName 目标存储桶
     * @param targetKey        目标对象key
     * @return 复制后的对象的url
     */
    @Override
    public String copyObject(String sourceKey, String targetRegion, String targetBucketName, String targetKey)
    {
        throw new UnsupportedOperationException("can not copy file with base template");
    }

    /**
     * <p>关闭cos客户端连接</p>
     */
    @Override
    public void close()
    {
        cosClient.shutdown();
    }

    /**
     * 设置操作模板的存储桶,无需填写APP ID
     * @param objectTemplateBucket    操作的存储桶
     */
    public void setObjectTemplateBucket(String objectTemplateBucket)
    {
        CommonUtil.assertObjectNull("object template bucket",objectTemplateBucket);
        this.objectTemplateBucket = getStandardBucketName(objectTemplateBucket);
    }

    /**
     * <p>
     *    获取操作模板的存储桶
     * </p>
     * @return      操作的存储桶
     */
    public String getObjectTemplateBucket()
    {
        return objectTemplateBucket;
    }

    /**
     * <p>获取上传对象的大小限制</p>
     * @return  上传对象的大小限制
     */
    public int getUploadLimitSize()
    {
        return cosClientProperties.getUploadLimitSize();
    }

    /**
     * <p>获取当前模板的region</p>
     * @return  地域
     */
    public String getRegion()
    {
        return region;
    }


    /**
     * <p>设置当前模板的地域</p>
     * @param region
     */
    public void setRegion(String region)
    {
        this.region = region;
    }

    /**
     * <p>
     *     创建拷贝专用TransferManager
     * </p>
     * @param targetRegion  目标地域
     * @return              TransferManager
     */
    protected final TransferManager newCopyTransferManager(String targetRegion)
    {
        TransferManager transferManager = copyTransferManagerMap.get(targetRegion);
        if(transferManager != null)
        {
            return transferManager;
        }
        else
        {
            copyTransferManagerMap.put
                    (targetRegion, (transferManager = new TransferManager(new COSClient(cosClientProperties.getCosCredentials(),new ClientConfig(new Region(targetRegion))))));
            return transferManager;
        }
    }

    /**
     * <p>创建复制请求</p>
     * @param sourceKey             源对象的key
     * @param targetBucketName      目标bucket name
     * @param targetKey             目标key
     * @return                      CopyObjectRequest
     */
    protected final CopyObjectRequest newCopyObjectRequest(String sourceKey,String targetBucketName,String targetKey)
    {
        return new CopyObjectRequest(new Region(region),
                objectTemplateBucket,
                sourceKey,
                targetBucketName,
                targetKey);
    }

    //返回原cos client
    protected final COSClient newCopySourceCOSClient()
    {
        return cosClient;
    }

    /**
     * 获取腾讯云标准的存储桶名
     * @param bucketName       存储桶名
     * @return                 标准存储桶名
     */
    protected final String getStandardBucketName(String bucketName)
    {
        return COSUtil.getTencloudStandardBucketName(bucketName,cosClientProperties.getAppId());
    }

    /**
     * <p>根据对象键生成对象url</p>
     * @param objectKey     对象键
     * @return              对象url
     */
    protected final String getObjectUrl(String objectKey)
    {
        return COSUtil.getTencloudObjectUrl(objectTemplateBucket,region,objectKey);
    }
}
