package com.github.guang19.cos.template.buckettemplate;

import com.github.guang19.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.cos.util.COSUtil;
import com.github.guang19.util.CommonUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.AccessControlList;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yangguang
 * @date 2020/2/3
 * @description        <p> 腾讯云COS存储桶基础操作模板</p>
 */
public abstract class BaseTenCloudCOSBucketTemplate implements TenCloudCOSBucketTemplate
{
    //tencen cos client
    protected final COSClient cosClient;

    //腾讯云 APP Id
    private final String appId;

    //logger
    protected static final Logger logger = LoggerFactory.getLogger(BaseTenCloudCOSBucketTemplate.class);

    /**
     * <p>构造腾讯云COS存储桶操作基础模板</p>
     * @param cosClientProperties         腾讯云cos客户端属性
     */
    protected BaseTenCloudCOSBucketTemplate(TenCloudCOSClientProperties cosClientProperties)
    {
        this.appId = cosClientProperties.getAppId();
        this.cosClient = new COSClient(cosClientProperties.getCosCredentials(), COSUtil.newTenCloudCOSClientConfig(cosClientProperties));
    }

    /**
     * <p>获取当前地域下的所有存储桶</p>
     *
     * @return 存储桶集合
     */
    @Override
    public List<String> getAllBuckets()
    {
        List<String> buckets = cosClient.listBuckets().stream().map(Bucket::toString).collect(Collectors.toList());
        close();
        return buckets;
    }

    /**
     * <p>获取存储桶的位置</p>
     * @param bucketName 存储桶名
     * @return 存储桶位置, 也就是region {@link Region}  ,    aliyun may be null
     */
    @Override
    public String getBucketLocation(String bucketName)
    {
        CommonUtil.assertObjectNull("bucketName",bucketName);
        String bucketLocation = null;
        try
        {
            bucketLocation = cosClient.getBucketLocation(getStandardBucketName(bucketName));
        }
        catch (CosClientException e)
        {
            logger.error("error during get bucket location : ".concat(e.getMessage()));
        }
        finally
        {
            close();
        }
        return bucketLocation;
    }

    /**
     * <p>获取存储桶的访问权限控制列表</p>
     *
     * @param bucketName 存储桶名
     * @return 访问权限控制列表
     *  <code>
     *      Map:
     *      owner: xxx
     *      grants:xxx
     *  </code>
     */
    @Override
    public Map<String, Object> getBucketAccessControllerList(String bucketName)
    {
        CommonUtil.assertObjectNull("bucketName",bucketName);
        Map<String,Object> map = null;
        try
        {
            AccessControlList accessControlList = cosClient.getBucketAcl(getStandardBucketName(bucketName));
            map = new HashMap<>();
            getBucketAccessControllerList(map,accessControlList);
        }
        catch (CosClientException e)
        {
            logger.error("error during get bucket access controller list : ".concat(e.getMessage()));
        }
        finally
        {
            close();
        }
        return map;
    }

    //获取存储桶权限
    private void getBucketAccessControllerList(Map<String,Object> map,AccessControlList accessControlList)
    {
        if(accessControlList != null)
        {
            map.put("owner",accessControlList.getOwner());
            map.put("grants",accessControlList.getGrantsAsList());
        }
        else
        {
            map.put("owner",null);
            map.put("grants",null);
        }
    }

    /**
     * <p>判断存储桶是否存在</p>
     *
     * @param bucketName 存储桶名
     * @return           存储桶是否存在
     */
    @Override
    public boolean existBucket(String bucketName)
    {
        CommonUtil.assertObjectNull("bucketName",bucketName);
        boolean exist = cosClient.doesBucketExist(getStandardBucketName(bucketName));
        close();
        return exist;
    }

    /**
     * <p>创建存储桶</p>
     *
     * @param bucketName 存储桶名
     * @return 创建好的存储桶bucket
     */
    @Override
    public Bucket createBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读写的存储桶</p>
     *
     * @param bucketName 存储桶名
     * @return 公有读写的存储桶
     */
    @Override
    public Bucket createBucketPublicReadAndWrite(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读私有写的存储桶</p>
     *
     * @param bucketName 存储桶名
     * @return 公有读私有写的存储桶
     */
    @Override
    public Bucket createBucketPublicRead(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>删除存储桶</p>
     *
     * @param bucketName 存储桶名
     */
    @Override
    public void deleteBucket(String bucketName)
    {
        CommonUtil.assertObjectNull("bucketName",bucketName);
        try
        {
            cosClient.deleteBucket(getStandardBucketName(bucketName));
        }
        catch (CosClientException e)
        {
            logger.error("error during delete bucket : ".concat(e.getMessage()));
        }
        finally
        {
            close();
        }
    }

    /**
     * <p>关闭cos客户端会话</p>
     */
    @Override
    public void close()
    {
        cosClient.shutdown();
    }

    //根据存储桶获取标准存储桶名
    protected String getStandardBucketName(String bucketName)
    {
        return COSUtil.getTencloudStandardBucketName(bucketName,appId);
    }
}
