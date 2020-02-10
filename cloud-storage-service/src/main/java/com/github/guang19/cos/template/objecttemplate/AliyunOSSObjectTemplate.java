package com.github.guang19.cos.template.objecttemplate;

/**
 * @author yangguang
 * @date 2020/2/7
 * @description <p>
 *                     阿里云OSS对象操作模板
 *                    <comment>
 *                      阿里云的对象拷贝不支持跨域
 *                    </comment>
 *              </p>
 */
public interface AliyunOSSObjectTemplate extends COSObjectTemplate
{

    /**
     * <p>
     *     从当前存储桶复制对象到目标存储桶,不支持目标跨域拷贝
     *     此方法就算复制失败,仍然会返回一个错误的url,因为我在处理异常这点上尽量不影响程序的正常运行
     * </p>
     * @param sourceKey             原对象key
     * @param targetBucketName      目标存储桶
     * @param targetKey             目标对象key
     * @return                      复制后的对象的url
     */
    public String copyObject(String sourceKey,String targetBucketName,String targetKey);
}
