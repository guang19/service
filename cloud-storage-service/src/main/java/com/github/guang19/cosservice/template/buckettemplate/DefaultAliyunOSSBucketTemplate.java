package com.github.guang19.cosservice.template.buckettemplate;

import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.github.guang19.cosservice.config.AliyunOSSClientProperties;
import com.github.guang19.cosservice.util.COSUtil;

/**
 * @author yangguang
 * @date 2020/2/7
 * @description <p>阿里云OSS存储桶操作默认模板</p>
 */
public class DefaultAliyunOSSBucketTemplate extends BaseAliyunOSSBucketTemplate
{

    /**
     * 阿里云OSS客户端属性构造
     * @param ossClientProperties   OSS客户端属性构造
     */
    public DefaultAliyunOSSBucketTemplate(AliyunOSSClientProperties ossClientProperties)
    {
        super(ossClientProperties);
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
        return createBucket(bucketName,StorageClass.Standard,DataRedundancyType.LRS,CannedAccessControlList.Private);
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
        return createBucket(bucketName,StorageClass.Standard,DataRedundancyType.ZRS,CannedAccessControlList.Private);
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
        return createBucket(bucketName,StorageClass.IA,DataRedundancyType.LRS,CannedAccessControlList.Private);
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
        return createBucket(bucketName,StorageClass.IA,DataRedundancyType.ZRS,CannedAccessControlList.Private);
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
        return createBucket(bucketName,StorageClass.Archive,null,CannedAccessControlList.Private);
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
        return createBucket(bucketName,StorageClass.Standard,DataRedundancyType.LRS,CannedAccessControlList.PublicReadWrite);
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
        return createBucket(bucketName,StorageClass.Standard,DataRedundancyType.ZRS,CannedAccessControlList.PublicReadWrite);
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
        return createBucket(bucketName,StorageClass.IA,DataRedundancyType.LRS,CannedAccessControlList.PublicReadWrite);
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
        return createBucket(bucketName,StorageClass.IA,DataRedundancyType.ZRS,CannedAccessControlList.PublicReadWrite);
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
        return createBucket(bucketName,StorageClass.Archive,null,CannedAccessControlList.PublicReadWrite);
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
        return createBucket(bucketName,StorageClass.Standard,DataRedundancyType.LRS,CannedAccessControlList.PublicRead);
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
        return createBucket(bucketName,StorageClass.Standard,DataRedundancyType.ZRS,CannedAccessControlList.PublicRead);
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
        return createBucket(bucketName,StorageClass.IA,DataRedundancyType.LRS,CannedAccessControlList.PublicRead);
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
        return createBucket(bucketName,StorageClass.IA,DataRedundancyType.ZRS,CannedAccessControlList.PublicRead);
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
        return createBucket(bucketName,StorageClass.Archive,null,CannedAccessControlList.PublicRead);
    }

    /**
     * <p>构造存储桶的核心方法</p>
     * @param bucketName                存储桶类型
     * @param storageClass              存储桶存储类型
     * @param redundancyType            冗余类型
     * @param accessControlList         访问权限问题
     * @return                          创建的存储桶
     */
    private Bucket createBucket(String bucketName, StorageClass storageClass , DataRedundancyType redundancyType, CannedAccessControlList accessControlList)
    {
        COSUtil.assertObjectNull(bucketName);
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
            log.error("error during create bucket : " .concat(COSUtil.parseAliyunErrorMessage(e)));
        }
        finally
        {
            close();
        }
        return bucket;
    }
}
