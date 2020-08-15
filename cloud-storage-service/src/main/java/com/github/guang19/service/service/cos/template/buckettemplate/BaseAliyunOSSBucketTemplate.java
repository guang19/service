package com.github.guang19.service.service.cos.template.buckettemplate;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.github.guang19.service.service.cos.util.COSUtil;
import com.github.guang19.service.service.cos.config.AliyunOSSClientProperties;
import com.github.guang19.service.util.CommonUtil;
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

    //LOGGER
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseAliyunOSSBucketTemplate.class);

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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
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
        throw new UnsupportedOperationException("can not create bucket with base template.");
    }

    /**
     * <p>获取当前地域下的所有存储桶</p>
     *
     * @return 存储桶集合
     */
    @Override
    public List<String> getAllBuckets()
    {
        try
        {
            return ossClient.listBuckets().stream().map(Bucket::toString).collect(Collectors.toList());
        }
        finally
        {
            close();
        }
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
        CommonUtil.assertObjectNull(bucketName,"bucket name can not be null.");
        try
        {
            return  ossClient.getBucketLocation(bucketName);
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while cos get bucket location : {}" ,COSUtil.parseAliyunErrorMessage(e));
            return null;
        }
        finally
        {
            close();
        }
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
        CommonUtil.assertObjectNull(bucketName,"bucket name can not be null.");
        AccessControlList accessControlList = null;
        try
        {
            accessControlList = ossClient.getBucketAcl(bucketName);
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while cos get bucket access controller list : {}" ,COSUtil.parseAliyunErrorMessage(e));
        }
        finally
        {
            close();
        }
        return putBucketAccessControllerListMap(accessControlList);
    }

    //获取存储桶权限
    private Map<String,Object> putBucketAccessControllerListMap(AccessControlList accessControlList)
    {
        Map<String,Object> bucketAccessControllerListMap = new HashMap<>();
        if(accessControlList != null)
        {
            bucketAccessControllerListMap.put("owner",accessControlList.getOwner());
            bucketAccessControllerListMap.put("grants",accessControlList.getGrants());
        }
        else
        {
            bucketAccessControllerListMap.put("owner",null);
            bucketAccessControllerListMap.put("grants",null);
        }
        return bucketAccessControllerListMap;
    }

    /**
     * <p>
     *     判断存储桶是否存在
     *     阿里云的这个API有问题，在我测试的时候，无论什么是否存在，
     *     都返回true。
     *     我的测试结果如下:
     *     ```
     *     true  //无论什么结果都返回true
     *     警告: [Server]Unable to execute HTTP request: 返回结果无效，无法解析。
     *     [ErrorCode]: InvalidResponse
     *     [RequestId]: 5E85DE19A920FB3932139567
     *     [HostId]: null
     *     [ResponseError]:
     *     <?xml version="1.0" encoding="UTF-8"?>
     *     <Error>
     *       <Code>NoSuchBucket</Code>
     *       <Message>The specified bucket does not exist.</Message>
     *       <RequestId>5E85DE19A920FB3932139567</RequestId>
     *       <HostId>lyciab.oss-cn-shanghai.aliyuncs.com</HostId>
     *       <BucketName>lyciab</BucketName>
     *     </Error>
     * Process finished with exit code 0
     *
     *     ```
     * </p>
     *
     * @param bucketName 存储桶名
     * @return          存储桶是否存在
     */
    @Override
    public boolean existBucket(String bucketName)
    {
        throw new UnsupportedOperationException("aliyun oss service does not support judge bucket exist.");
    }

    /**
     * <p>删除存储桶</p>
     *
     * @param bucketName 存储桶名
     */
    @Override
    public void deleteBucket(String bucketName)
    {
        CommonUtil.assertObjectNull(bucketName,"bucket name can not be null.");
        try
        {
            ossClient.deleteBucket(bucketName);
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while cos delete bucket : {}" ,COSUtil.parseAliyunErrorMessage(e));
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


    /**
     * <p>构造存储桶的核心方法</p>
     * @param bucketName                存储桶类型
     * @param storageClass              存储桶存储类型
     * @param redundancyType            冗余类型
     * @param accessControlList         访问权限问题
     * @return                          创建的存储桶
     */
    protected final Bucket createBucket(String bucketName, StorageClass storageClass , DataRedundancyType redundancyType, CannedAccessControlList accessControlList)
    {
        CommonUtil.assertObjectNull(bucketName,"bucket name can not be null.");
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        //设置存储类型
        createBucketRequest.setStorageClass(storageClass);
        if(redundancyType != null)
        {
            //设置冗余类型
            createBucketRequest.setDataRedundancyType(redundancyType);
        }
        //设置访问权限
        createBucketRequest.setCannedACL(accessControlList);
        Bucket bucket = null;
        try
        {
            bucket = ossClient.createBucket(createBucketRequest);
        }
        catch (OSSException e)
        {
            LOGGER.error("an error occurred while cos create bucket : {}" , COSUtil.parseAliyunErrorMessage(e));
        }
        finally
        {
            close();
        }
        return bucket;
    }

}
