package com.github.guang19.cos.template.buckettemplate;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.AccessControlList;
import com.aliyun.oss.model.Bucket;
import com.github.guang19.cos.util.COSUtil;
import com.github.guang19.cos.config.AliyunOSSClientProperties;
import com.github.guang19.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yangguang
 * @date 2020/2/6
 * @description <p>阿里云oss存储桶操作基础模板</p>
 */
public abstract class BaseAliyunOSSBucketTemplate implements AliyunOSSBucketTemplate
{
    //阿里云oss客户端
    protected final OSS ossClient;

    //logger
    protected static final Logger log = LoggerFactory.getLogger(BaseAliyunOSSBucketTemplate.class);

    /**
     * 阿里云OSS客户端属性构造
     * @param ossClientProperties   OSS客户端属性构造
     */
    protected BaseAliyunOSSBucketTemplate(AliyunOSSClientProperties ossClientProperties)
    {
        this.ossClient = new OSSClientBuilder().build(ossClientProperties.getCname() != null ? ossClientProperties.getCname() : ossClientProperties.getEndpoint(),
                                                      ossClientProperties.getCredentialsProvider(),
                                                      COSUtil.newAliyunOSSClientConfig(ossClientProperties));
    }

    /**
     * <p>创建私有读写的存储桶,默认为标准存储桶,本地冗余</p>
     *
     * @param bucketName 存储桶名(3 - 63个字符)
     * @return 存储桶
     */
    @Override
    public Bucket createPrivateStandardLRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建私有读写的存储桶:标准存储,同城冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPrivateStandardZRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建私有读写的存储桶:低频访问,本地冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPrivateIALRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建私有读写的存储桶:低频访问,同城冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPrivateIAZRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建私有读写的存储桶:归档存储</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPrivateArchiveBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读写的存储桶:标准存储,本地冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicStandardLRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读写的存储桶:标准存储,同城冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicStandardZRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读写的存储桶:低频访问,本地冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicIALRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读写的存储桶:低频访问,同城冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicIAZRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读写的存储桶:归档存储</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicArchiveBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读私有写的存储桶:标准存储,本地冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicReadStandardLRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读私有写的存储桶:标准存储,同城冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicReadStandardZRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读私有写的存储桶:低频访问,本地冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicReadIALRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读私有写的存储桶:低频访问,同城冗余</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicReadIAZRSBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>创建公有读私有写的存储桶:归档存储</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶
     */
    @Override
    public Bucket createPublicReadArchiveBucket(String bucketName)
    {
        throw new UnsupportedOperationException("can not create bucket with base template");
    }

    /**
     * <p>获取当前地域下的所有存储桶</p>
     *
     * @return 存储桶集合
     */
    @Override
    public List<String> getAllBuckets()
    {
        List<String> buckets = ossClient.listBuckets().stream().map(Bucket::toString).collect(Collectors.toList());
        close();
        return buckets;
    }

    /**
     * <p>获取存储桶的位置</p>
     *
     * @param bucketName 存储桶名
     * @return 存储桶位置, 也就是region
     */
    @Override
    public String getBucketLocation(String bucketName)
    {
        CommonUtil.assertObjectNull("bucketName",bucketName);
        String location = null;
        try
        {
            location =  ossClient.getBucketLocation(bucketName);
        }
        catch (OSSException e)
        {
            log.error("error during get bucket location".concat(COSUtil.parseAliyunErrorMessage(e)));
        }
        finally
        {
            close();
        }
        return location;
    }

    /**
     * <p>获取存储桶的访问权限控制列表</p>
     *
     * @param bucketName 存储桶名
     * @return 访问权限控制列表
     * <code>
     * Map:
     * owner: xxx
     * grants:xxx
     * </code>
     */
    @Override
    public Map<String, Object> getBucketAccessControllerList(String bucketName)
    {
        CommonUtil.assertObjectNull("bucketName",bucketName);
        Map<String,Object> map = null;
        try
        {
            AccessControlList accessControlList = ossClient.getBucketAcl(bucketName);
            map = new HashMap<>();
            getBucketAccessControllerList(map,accessControlList);
        }
        catch (OSSException e)
        {
            log.error("error during get bucket access controller list : ".concat(COSUtil.parseAliyunErrorMessage(e)));
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
            map.put("grants",accessControlList.getGrants());
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
     * @return          存储桶是否存在
     */
    @Override
    public boolean existBucket(String bucketName)
    {
        throw new UnsupportedOperationException("aliyun oss service does not support judge exist bucket");
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
            ossClient.deleteBucket(bucketName);
        }
        catch (OSSException e)
        {
            log.error("error during delete bucket : ".concat(COSUtil.parseAliyunErrorMessage(e)));
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
        ossClient.shutdown();
    }
}
