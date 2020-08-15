package com.github.guang19.service.service.cos.template.buckettemplate;

import com.qcloud.cos.model.Bucket;

/**
 * @author yangguang
 * @date 2020/2/3
 * @description  <p>腾讯云COS存储桶操作模板</p>
 *
 */
public interface TenCloudCOSBucketTemplate extends COSBucketTemplate
{
    /**
     * <p>创建存储桶,默认为私有读写</p>
     * @param bucketName 存储桶名
     * @return           存储桶
     */
    public Bucket createBucket(String bucketName);

    /**
     * <p>创建公有读写的存储桶</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createBucketPublicReadAndWrite(String bucketName);


    /**
     * <p>创建公有读私有写的存储桶</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createBucketPublicRead(String bucketName);
}
