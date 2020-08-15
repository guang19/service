package com.github.guang19.service.service.cos.template.buckettemplate.impl;

import com.github.guang19.service.service.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.service.service.cos.template.buckettemplate.BaseTenCloudCOSBucketTemplate;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.CannedAccessControlList;

/**
 * @author yangguang
 * @date 2020/2/3
 * @description <p>默认采用此模板进行腾讯云COS存储桶服务</p>
 */
public class DefaultTenCloudCOSBucketTemplate extends BaseTenCloudCOSBucketTemplate
{

    /**
     * <p>构造基础模板</p>
     * @param cosProperties         cos客户端属性
     */
    public DefaultTenCloudCOSBucketTemplate(TenCloudCOSClientProperties cosProperties)
    {
        super(cosProperties);
    }

    /**
     * <p>创建存储桶,默认为私有读写</p>
     *
     * @param bucketName 存储桶名
     * @return 创建好的存储桶bucket
     */
    @Override
    public Bucket createBucket(String bucketName)
    {
        return createBucket(bucketName,CannedAccessControlList.Private);
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
        return createBucket(bucketName,CannedAccessControlList.PublicReadWrite);
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
        return createBucket(bucketName,CannedAccessControlList.PublicRead);
    }
}
