package com.github.guang19.cos.template.buckettemplate;

import com.aliyun.oss.model.Bucket;

/**
 * @author yangguang
 * @date 2020/2/6
 * @description <p>
 *                  阿里云OSS存储桶操作模板
 *                  <comment>
 *                      由于阿里云存储桶的特点相对来说较多点,所以只得列出全部情况
 *                      并且由于阿里云接口设计问题,所有操作失败都会返回xml响应,所以创建操作返回的bucket可能会为null
 *                  </comment>
 *               </p>
 */
public interface AliyunOSSBucketTemplate extends COSBucketTemplate
{
    /**
     * <p>创建私有读写的存储桶:标准存储,本地冗余</p>
     * @param bucketName    存储桶名(3 - 63个字符)
     * @return              存储桶
     */
    public Bucket createPrivateStandardLRSBucket(String bucketName);

    /**
     * <p>创建私有读写的存储桶:标准存储,同城冗余</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPrivateStandardZRSBucket(String bucketName);

    /**
     * <p>创建私有读写的存储桶:低频访问,本地冗余</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPrivateIALRSBucket(String bucketName);

    /**
     * <p>创建私有读写的存储桶:低频访问,同城冗余</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPrivateIAZRSBucket(String bucketName);

    /**
     * <p>创建私有读写的存储桶:归档存储</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPrivateArchiveBucket(String bucketName);

    /**
     * <p>创建公有读写的存储桶:标准存储,本地冗余</p>
     * @param bucketName     存储桶名
     * @return               存储桶
     */
    public Bucket createPublicStandardLRSBucket(String bucketName);

    /**
     * <p>创建公有读写的存储桶:标准存储,同城冗余</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPublicStandardZRSBucket(String bucketName);

    /**
     * <p>创建公有读写的存储桶:低频访问,本地冗余</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPublicIALRSBucket(String bucketName);

    /**
     * <p>创建公有读写的存储桶:低频访问,同城冗余</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPublicIAZRSBucket(String bucketName);

    /**
     * <p>创建公有读写的存储桶:归档存储</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPublicArchiveBucket(String bucketName);

    /**
     * <p>创建公有读私有写的存储桶:标准存储,本地冗余</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPublicReadStandardLRSBucket(String bucketName);

    /**
     * <p>创建公有读私有写的存储桶:标准存储,同城冗余</p>
     * @param bucketName     存储桶名
     * @return                存储桶
     */
    public Bucket createPublicReadStandardZRSBucket(String bucketName);

    /**
     * <p>创建公有读私有写的存储桶:低频访问,本地冗余</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPublicReadIALRSBucket(String bucketName);

    /**
     * <p>创建公有读私有写的存储桶:低频访问,同城冗余</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPublicReadIAZRSBucket(String bucketName);

    /**
     * <p>创建公有读私有写的存储桶:归档存储</p>
     * @param bucketName    存储桶名
     * @return              存储桶
     */
    public Bucket createPublicReadArchiveBucket(String bucketName);
}
