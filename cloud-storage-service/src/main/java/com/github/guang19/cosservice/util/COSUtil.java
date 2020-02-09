package com.github.guang19.cosservice.util;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.comm.Protocol;
import com.github.guang19.cosservice.config.TenCloudCOSClientProperties;
import com.github.guang19.cosservice.config.AliyunOSSClientProperties;
import com.github.guang19.cosservice.config.COSClientProperties;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yangguang
 * @date 2020/2/5
 * @description <p>
 *                  COS工具类
 *              </p>
 */
@Slf4j
public class COSUtil
{
    //腾讯云对象域名:bucket name , regio , key
    private static final String TENCLOUD_OBJECT_URL = "https://%s.cos.%s.myqcloud.com/%s";

    //腾讯云对象域名前缀: bucket name , regio
    private static final String TENCLOUD_OBJECT_URL_PREFIX = "https://%s.cos.%s.myqcloud.com/";

    //阿里云对象域名: bucket name , endpoint , key
    private static final String ALIYUN_OBJECT_DEFAULT_URL = "https://%s.%s/%s";

    //阿里云对象域名带有指定style: bucket name , endpoint , key , style
    private static final String ALIYUN_OBJECT_DEFAULT_URL_WITH_STYLE = "https://%s.%s/%s?%s";;

    //阿里云对象域名前缀: bucket name , endpoint
    private static final String ALIYUN_OBJECT_DEFAULT_URL_PREFIX = "https://%s.%s/";

    //阿里云自定义对象域名: cname , key
    private static final String ALIYUN_OBJECT_CNAME_URL = "https://%s/%s";

    //阿里云自定义对象域名带有指定style:cname , key , style
    private static final String ALIYUN_OBJECT_CNAME_URL_WITH_STYLE = "https://%s/%s?%s";

    //阿里云自定义对象域名前缀: cname
    private static final String ALIYUN_OBJECT_CNAME_URL_PREFIX = "https://%s/";

    //XML Reader
    private static final SAXReader SAX_READER = new SAXReader();

    //统一大文件操作的分块大小 part size : 10M
    private static final long LARGE_OBJECT_PART_SIZE = 1024 * 1024 * 10;

    /**
     *  <p>获取标准的腾讯云存储桶名</p>
     * @param bucketName       存储桶名
     * @param appId            app id
     * @return                 存储桶的标准名
     */
    public static String getTencloudStandardBucketName(String bucketName,String appId)
    {
        return bucketName.concat("-").concat(appId);
    }

    /**
     * <p>根据存储桶,地域和对象key生成腾讯云对象的url</p>
     * @param bucketName    标准存储桶名. standard bucketName
     * @param region        地域
     * @param key           对象key
     * @return              对象的url
     */
    public static String getTencloudObjectUrl(String bucketName,String region,String key)
    {
        return String.format(TENCLOUD_OBJECT_URL,bucketName,region,key);
    }


    /**
     *  <p> 根据存储桶,地域生成腾讯云对象域名前缀</p>
     * @param bucketName        存储桶名
     * @param region            地域
     * @return                  对象域名前缀
     */
    public static String getTenCloudObjectUrlPrefix(String bucketName,String region)
    {
        return String.format(TENCLOUD_OBJECT_URL_PREFIX,bucketName,region);
    }

    /**
     * <p>根据存储桶,endpoint和对象key生成阿里云对象的url</p>
     * @param bucketName    存储桶名
     * @param endpoint      oss对外访问等等域名
     * @param cname         自定义域名
     * @param style         图片样式
     * @param key           对象的key
     * @return              对象的url
     */
    public static String getAliyunObjectUrl(String bucketName,String endpoint,String cname,String style,String key)
    {
        if(cname != null)
        {
            if(style != null)
            {
                //自定义域名并带有样式
                return String.format(ALIYUN_OBJECT_CNAME_URL_WITH_STYLE,cname,key,style);
            }
            else
            {
                //自定义域名没有样式
                return String.format(ALIYUN_OBJECT_CNAME_URL,cname,key);
            }
        }
        else
        {
            if(style != null)
            {
                //endpoint并带有样式
                return String.format(ALIYUN_OBJECT_DEFAULT_URL_WITH_STYLE,bucketName,endpoint,key,style);
            }
            else
            {
                //endpoint没有样式
                return String.format(ALIYUN_OBJECT_DEFAULT_URL,bucketName,endpoint,key);
            }
        }
    }

    /**
     * <p>根据存储桶,endpoint生成阿里云对象域名前缀</p>
     * @param bucketName        存储桶名
     * @param endpoint          oss对外访问的域名
     * @param cname             自定义域名
     * @return                  对象域名前缀
     */
    public static String getAliyunObjectUrlPrefix(String bucketName,String endpoint,String cname)
    {
        if(cname != null)
        {
            return String.format(ALIYUN_OBJECT_CNAME_URL_PREFIX,cname);
        }
        else
        {
            return String.format(ALIYUN_OBJECT_DEFAULT_URL_PREFIX,bucketName,endpoint);
        }
    }

    /**
     * <p>解析阿里云返回的xml响应为错误信息</p>
     * @param ossException       OSSException
     * @return                  正常的错误信息
     */
    public static String parseAliyunErrorMessage(OSSException ossException)
    {
        String errorMessage = ossException.getErrorCode();
        try
        {
            if(ossException.getRawResponseError() != null)
            {
                Document document = SAX_READER.read(new ByteArrayInputStream(ossException.getRawResponseError().getBytes("UTF-8")));
                errorMessage = document.getRootElement().elementText("Message");
            }
        }
        catch (Throwable e)
        {
            log.error("error during parse aliyun xml response : " + e.getMessage());
        }
        return errorMessage;
    }


    /**
     * <p>创建腾讯云COS客户端配置</p>
     * @param cosClientProperties    腾讯云客户端属性
     * @return                       腾讯云客户端配置
     */
    public static ClientConfig newTenCloudCOSClientConfig(TenCloudCOSClientProperties cosClientProperties)
    {
        ClientConfig clientConfig = new ClientConfig(new Region(cosClientProperties.getRegion()));
        clientConfig.setHttpProtocol(cosClientProperties.getProtocol().equals("http") ? HttpProtocol.http : HttpProtocol.https);
        clientConfig.setHttpProxyIp(cosClientProperties.getProxyIP());
        if(cosClientProperties.getProxyPort() > 0)
        {
            clientConfig.setHttpProxyPort(cosClientProperties.getProxyPort());
        }
        clientConfig.setProxyUsername(cosClientProperties.getProxyUsername());
        clientConfig.setProxyPassword(cosClientProperties.getProxyPassword());
        clientConfig.setMaxConnectionsCount(cosClientProperties.getMaxConnections());
        clientConfig.setSocketTimeout(cosClientProperties.getSocketTimeout());
        clientConfig.setConnectionRequestTimeout(cosClientProperties.getConnectionRequestTimeout());
        clientConfig.setConnectionTimeout(cosClientProperties.getConnectionTimeout());
        return clientConfig;
    }

    /**
     * <p>创建阿里云oss客户端配置</p>
     * @param ossClientProperties   阿里云客户端属性
     * @return                      阿里云客户端配置
     */
    public static ClientBuilderConfiguration newAliyunOSSClientConfig(AliyunOSSClientProperties ossClientProperties)
    {
        ClientBuilderConfiguration ossClientConfiguration = new ClientBuilderConfiguration();
        ossClientConfiguration.setSupportCname(true);
        ossClientConfiguration.setProtocol(ossClientProperties.getProtocol().endsWith("http") ? Protocol.HTTP : Protocol.HTTPS);
        ossClientConfiguration.setProxyHost(ossClientProperties.getProxyIP());
        if(ossClientProperties.getProxyPort() > 0)
        {
            ossClientConfiguration.setProxyPort(ossClientProperties.getProxyPort());
        }
        ossClientConfiguration.setProxyUsername(ossClientProperties.getProxyUsername());
        ossClientConfiguration.setProxyPassword(ossClientProperties.getProxyPassword());
        ossClientConfiguration.setMaxConnections(ossClientProperties.getMaxConnections());
        ossClientConfiguration.setSocketTimeout(ossClientProperties.getSocketTimeout());
        ossClientConfiguration.setConnectionRequestTimeout(ossClientProperties.getConnectionRequestTimeout());
        ossClientConfiguration.setConnectionTimeout(ossClientProperties.getConnectionTimeout());
        return ossClientConfiguration;
    }

    /**
     * <p>创建线程池</p>
     * @param cosClientProperties       COS客户端属性
     * @return                          线程池
     */
    public static ExecutorService newThreadPoolExecutor(COSClientProperties cosClientProperties)
    {
        return new ThreadPoolExecutor(
                cosClientProperties.getTHREAD_POOL_CORE_SIZE(),cosClientProperties.getTHREAD_POOL_MAX_SIZE(),
                cosClientProperties.getTHREAD_KEEPALIVE(), TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>(),
                cosClientProperties.getDEFAULT_THREAD_FACTORY(),cosClientProperties.getDEFAULT_REJECTED_POLICY());
    }

    /**
     * <p>检查对象大小是否在限制范围内</p>
     * @param objectStream        对象流
     * @param uploadLimitSize     限制大小
     * @return                    对象大小
     */
    public static int checkObjectInputStream(InputStream objectStream , int uploadLimitSize) throws IOException
    {
        if(objectStream == null)
        {
            throw new IllegalArgumentException("object stream can not be null");
        }
        int objectSize = objectStream.available();
        if(objectSize <= 0)
        {
            throw new IllegalArgumentException("object size is out of range, should be between 1 - " + uploadLimitSize + "m");
        }
        else if ((float)objectSize / 1024 / 1024 > uploadLimitSize)
        {
            throw new IllegalArgumentException("object size is out of range, should be between 1 - " + uploadLimitSize + "m");
        }
        return objectSize;
    }

    /**
     * <p>判断dir是否以 分隔符 / 结尾,如果不是,那么抛出异常,允许dir为空串</p>
     * @param cosDir    对象在COS服务器上的目录
     */
    public static void checkCOSDir(String cosDir)
    {
        if(cosDir == null)
        {
            throw new IllegalArgumentException("cos directory cannot be null");
        }
        if(!cosDir.isBlank() && !cosDir.endsWith("/"))
        {
            throw new IllegalArgumentException("cos directory must be a directory");
        }
    }

    /**
     * <p>判断localDir是否存在并且的确为一个文件夹</p>
     * @param localDir  本地文件夹
     */
    public static void checkLocalDir(String localDir)
    {
        if(localDir == null)
        {
            throw new IllegalArgumentException("local directory  cannot be null");
        }
        Path path = Path.of(localDir);
        if(Files.notExists(path))
        {
            throw new IllegalArgumentException("local directory is not exist");
        }
        if(!Files.isDirectory(path))
        {
            throw new IllegalArgumentException("local directory must be a directory,can not be a file");
        }
    }

    /**
     * <p>判断指定的文件路径是否存在并且是否为一个文件</p>
     * @param filePath  文件路径
     * @return          文件路径是否存在并且是否为一个文件
     */
    public static Path checkLocalFile(String filePath)
    {
        if(filePath == null || filePath.isBlank())
        {
            throw new IllegalArgumentException("file cannot be null");
        }
        Path path = Path.of(filePath);
        if(Files.notExists(path))
        {
            throw new IllegalArgumentException("file is not exist");
        }
        if(Files.isDirectory(path))
        {
            throw new IllegalArgumentException("file must be a file,can not be directory");
        }
        return path;
    }

    /**
     * <p>判断对象名是否以 分隔符 / 结尾,如果是,那么抛出异常,允许objectName为空串</p>
     * @param objectName 对象名
     */
    public static void checkObjectName(String objectName)
    {
        if(objectName == null || objectName.isBlank())
        {
            throw new IllegalArgumentException("object name cannot be empty");
        }
        if(objectName.endsWith("/"))
        {
            throw new IllegalArgumentException("object name must be a name meaning it can not end with '/'");
        }
    }

    /**
     * 只判断文件是否不为目录
     * @param virtualFile   文件名
     */
    public static void checkVirtualFile(String virtualFile)
    {
        if(virtualFile == null)
        {
            throw new IllegalArgumentException("file can not be null");
        }
        if(Files.isDirectory(Path.of(virtualFile)))
        {
            throw new IllegalArgumentException("file must be a file");
        }
    }

    /**
     * 判断字符串是否以图片名结尾
     * @param str       字符串
     * @return          字符串
     */
    public static boolean endWithImgType(String str)
    {
        if(str != null)
        {
            return str.endsWith(".jpg") || str.endsWith(".png") || str.endsWith(".jpeg") || str.endsWith(".bmp") || str.endsWith(".gif") || str.endsWith(".tif")
                    || str.endsWith(".webp") || str.endsWith(".pcx") || str.endsWith(".tga") || str.endsWith(".sdr") || str.endsWith(".pcd") || str.endsWith(".dxf")
                    || str.endsWith(".WMF") || str.endsWith(".raw");
        }
        return false;
    }

    /**
     * 获取大文件操作分块大小
     * @return  大文件操作分块大小
     */
    public static long getLargeObjectPartSize()
    {
        return LARGE_OBJECT_PART_SIZE;
    }

    /**
     * 判断单个参数是否为null
     * @param arg 参数
     */
    public static void assertObjectNull(Object arg)
    {
        if(arg == null)
        {
            throw new NullPointerException("arg can not be null");
        }
    }

    /**
     * 判断多个参数是否为null
     * @param args 参数
     */
    public static void assertObjectsNull(Object ...args)
    {
        for (int i = 0 ; i < args.length ; ++i)
        {
            if(args[i] == null)
            {
                throw new NullPointerException("arg index of " + i + " can not be null");
            }
        }
    }

    /**
     * 判断集合是否为空
     * @param collection    集合
     */
    public static void assertListEmpty(Collection collection)
    {
        if(collection == null || collection.size() <=0)
        {
            throw new NullPointerException("collection can not be empty");
        }
    }


    /**
     * <p>创建文件</p>
     * @param file  文件路径
     * @return      创建好的文件
     */
    public static File createFile(String file) throws IOException
    {
        Path path = Path.of(file);
        Path parent = path.getParent();
        //如果父目录存在就直接创建当前文件
        if(Files.exists(parent))
        {
            Files.createFile(path);
        }
        //如果父目录不存在就先创建父目录再创建当前文件
        else
        {
            Files.createDirectories(parent);
            Files.createFile(path);
        }
        return path.toFile();
    }
}
