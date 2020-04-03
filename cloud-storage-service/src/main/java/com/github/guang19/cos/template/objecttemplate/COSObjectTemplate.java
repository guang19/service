package com.github.guang19.cos.template.objecttemplate;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author yangguang
 * @date 2020/2/3
 * @description <p>
 *                  COS对象操作模板
 *              </p>
 */
public interface COSObjectTemplate
{
    /**
     * <p>
     *     获取当前存储桶下的所有对象的key,返回的对象包括目录和文件
     *     返回的对象数量限制为1000
     * </p>
     * @return      对象key集合
     */
    public List<String> getAllObjects();

    /**
     * <p>
     *     查询拥有相同前缀key的所有对象的key,返回的对象包括目录和文件
     * </p>
     * @param keyPrefix    对象key前缀,如: img/ , file/upload/ , file/img/upload , file/img/upload/jquery.js(如果keyPrefix为文件,那么仅仅会返回这个文件)
     * @return             对象key集合
     */
    public List<String> getAllObjectsWithKeyPrefix(String keyPrefix);

    /**
     * <p>
     *     获取当前存储桶下的所有文件名
     * </p>
     * @return      文件名集合
     */
    public List<String> getAllFileNames();

    /**
     * <p>
     *     查询拥有相同前缀key的所有文件名
     * </p>
     * @param keyPrefix    文件key前缀,如: img/ , file/upload/ , file/img/upload , file/img/upload/jquery.js(如果keyPrefix为文件,那么仅仅会返回这个文件)
     * @return             文件名集合
     */
    public List<String> getAllFileNamesWithKeyPrefix(String keyPrefix);

    /**
     * <p>获取所有文件的key</p>
     * @return      文件key集合 ：　img/a.jpg , img/a/b/c.jpg , d.jpg
     */
    public List<String> getAllFileKeys();

    /**
     * 获取对象的元信息
     * @param key 对象key
     * @return     对象元信息键值对
     *          <code>
     *              ETag:       xxxxxxxxxxxxxxxxxx
     *              Connection: keep-alive
     *              x-cos-request-id:xxxxxxxxxxxxxxxxxxxx
     *              Last-Modified   :Wed Sep 25 16:22:02 CST 2019
     *              Content-Length  :1024
     *              Date            :Tue, 04 Feb 2020 05:03:03 GMT
     *              Content-Type    :image/png
     *          </code>
     */
    public Map<String, Object> getObjectMetaData(String key);

    /**
     * <p>根据对象key删除当前存储桶下的对象</p>
     * @param key       对象的key
     */
    public void deleteObjectWithKey(String key);

    /**
     * <p>根据对象键集合,批量删除当前存储桶下的对象</p>
     * @param keys     对象键集合
     */
    public void deleteObjectsWithKeys(List<String> keys);

    /**
     * <p>根据url删除当前存储桶对象</p>
     * @param url       对象的url
     */
    public void deleteObjectWithUrl(String url);

    /**
     * <p>根据url集合,批量删除当前存储桶对象</p>
     * @param urls  url集合
     */
    public void deleteObjectsWithUrls(List<String> urls);

    /**
     * <p>上传文件到存储桶的默认目录,注意,是文件路径,不是目录</p>
     * @param filePath      本地文件路径
     * @return              上传成功后, 对象的url，如果上传失败，则返回null
     */
    public String uploadFile(String filePath);

    /**
     * <p>上传文件到存储桶</p>
     * @param cosDir           需要将对象上传到存储桶的哪个目录,必须以 '/' 结尾,允许空串
     * @param filePath      本地文件路径
     * @return              上传成功后, 对象的url，如果上传失败，则返回null
     */
    public String uploadFile(String cosDir,String filePath);

    /**
     * <p>上传对象到存储桶的默认目录</p>
     * @param fileStream    对象的输入流
     * @param objectName    指定上传后的对象名,但不需要指定后缀,如: a.jpg, a , b.jpg , b , cat 都行
     * @return              上传成功后, 对象的url，如果上传失败，则返回null
     */
    public String uploadFile(InputStream fileStream, String objectName);

    /**
     * <p>上传对象到存储桶</p>
     * @param fileStream    对象的输入流
     * @param cosDir           需要将对象上传到存储桶的哪个目录,必须以 '/' 结尾,允许空串
     * @param objectName    对象名,但不需要指定后缀,如: a.jpg, a , b.jpg , b , cat 都行
     * @return              上传成功后, 对象的url，如果上传失败，则返回null
     */
    public String uploadFile(InputStream fileStream,String cosDir,String objectName);

    /**
     * <p>下载文件到本地</p>
     * @param key        文件的key
     * @param saveFile   指定下载后的文件,这个文件不需要存在
     *                   比如你需要下载的文件key为: img/a.jpg,
     *
     *                   那么你可以指定filepath为 :
     *                   <code>
     *                   a.jpg
     *
     *                   b.jpg")
     *
     *                   /usr/dir/img.jpg
     *
     *                   <p>无需指定下载的文件类型</p>
     *                   /usr/dir/a
     *
     *                   G:/dir/b
     *                   </code>
     */
    public void downloadFile(String key,String saveFile);

    /**
     * <p>
     *     把当前存储桶下的所有文件都下载到本地
     *     此方法的耗时取决于当前存储桶的文件数量和文件大小
     * </p>
     * @param saveDir   指定本地目录
     */
    public void downloadAllFiles(String saveDir);

    /**
     * <p>关闭cos客户端连接</p>
     */
    public void close();
}
