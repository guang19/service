package com.github.guang19.cos.template.objecttemplate;

/**
 * @author yangguang
 * @date 2020/2/3
 * @description <p>
 *                 腾讯云COS操作模板
 *              </p>
 */
public interface TenCloudCOSObjectTemplate extends COSObjectTemplate
{

    /**
     * <p>
     *     从当前存储桶复制对象到目标存储桶,存储桶无需填写 APPId,支持目标跨域拷贝
     *     此方法就算复制失败,仍然会返回一个错误的url,因为我在处理异常这点上尽量不影响程序的正常运行
     * </p>
     * @param sourceKey             原对象key
     * @param targetBucketName      目标存储桶
     * @param targetRegion          目标地域
     * @param targetKey             目标对象key
     * @return                      复制后的对象的url
     */
    public String copyObject(String sourceKey,String targetRegion,String targetBucketName,String targetKey);

    /**
     * 关闭高级接口
     */
    public void closeTransferManager();
}
