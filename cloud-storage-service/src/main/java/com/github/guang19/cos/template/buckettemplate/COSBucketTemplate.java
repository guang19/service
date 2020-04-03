package com.github.guang19.cos.template.buckettemplate;

import java.util.List;
import java.util.Map;

/**
 * @author yangguang
 * @date 2020/2/3
 * @description <p>
 *               COS存储桶操作模板
 *               不过个人不建议使用API操作存储桶
 *               毕竟不是特别方便和直观,还是使用控制台创建好
 *               </p>
 *             <h2>如果是腾讯云,此模板下的所有bucketName参数,无需填写APPID,只需要前缀名字</h2>
 */
public interface COSBucketTemplate
{

    /**
     *
     * <p>获取当前地域下的所有存储桶</p>
     * <comment>如果是阿里云OSS服务,那么使用自定义域名时无法使用listBuckets方法。</comment>
     * @return          存储桶集合
     */
    public List<String> getAllBuckets();

    /**
     * <p>获取存储桶的位置</p>
     * @param bucketName 存储桶名
     * @return          存储桶位置,也就是region {@link com.qcloud.cos.region.Region}
     */
    public String getBucketLocation(String bucketName);

    /**
     *<p>获取存储桶的访问权限控制列表</p>
     * @param bucketName    存储桶名
     * @return              访问权限控制列表
     * <code>
     *     Map:
     *     owner: xxx
     *     grants:xxx
     * </code>
     */
    public Map<String,Object> getBucketAccessControllerList(String bucketName);

    /**
     * <p>判断存储桶是否存在</p>
     * @param bucketName    存储桶名
     * @return              存储桶是否存在
     */
    public boolean existBucket(String bucketName);

    /**
     * <p>删除存储桶</p>
     * @param bucketName    存储桶名
     */
    public void deleteBucket(String bucketName);

    /**
     * <p>关闭cos客户端会话</p>
     */
    public void close();
}
