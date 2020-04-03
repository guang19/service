package com.github.guang19.cos.util;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.comm.Protocol;
import com.github.guang19.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.cos.config.AliyunOSSClientProperties;
import com.github.guang19.cos.config.COSClientProperties;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    //LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(COSUtil.class);

    /**
     *  <p>获取标准的腾讯云存储桶名</p>
     * @param bucketName       存储桶名
     * @param appId            app id
     * @return                 存储桶的标准名
     */
    public static String getTencloudStandardBucketName(String bucketName,String appId)
    {
        return bucketName + "-" + appId;
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
                Document document = SAX_READER.read(new ByteArrayInputStream(ossException.getRawResponseError().getBytes(StandardCharsets.UTF_8)));
                errorMessage = document.getRootElement().elementText("Message");
            }
        }
        catch (Throwable e)
        {
            LOGGER.error("an error occurred while parse aliyun xml response : {}" ,e.getMessage());
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
            throw new IllegalArgumentException("object stream can not be null.");
        }
        int objectSize = objectStream.available();
        if(objectSize <= 0)
        {
            throw new IllegalArgumentException(String.format("object size is out of range, should be between 1 - %d m.",uploadLimitSize));
        }
        else if ((float)objectSize / 1024 / 1024 > uploadLimitSize)
        {
            throw new IllegalArgumentException(String.format("object size is out of range, should be between 1 - %d m.",uploadLimitSize));
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
            throw new IllegalArgumentException("cos directory cannot be null.");
        }
        if((!cosDir.isEmpty() || !cosDir.trim().isEmpty()) && !cosDir.endsWith("/"))
        {
            throw new IllegalArgumentException("cos dir can be empty string , or cos directory must be end with '/'.");
        }
    }

    /**
     * <p>判断对象名是否以 分隔符 / 结尾,如果是,那么抛出异常,不允许objectName为空串</p>
     * @param objectName 对象名
     */
    public static void checkObjectName(String objectName)
    {
        if(objectName == null || objectName.isEmpty() || objectName.trim().isEmpty())
        {
            throw new IllegalArgumentException("object name cannot be empty.");
        }
        if(objectName.endsWith("/"))
        {
            throw new IllegalArgumentException("object name must be a name meaning it cannot end with '/'.");
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
            throw new IllegalArgumentException("file can not be null.");
        }
        if(Files.isDirectory(Paths.get(virtualFile)))
        {
            throw new IllegalArgumentException("file cannot be a directory.");
        }
    }

    /**
     * 获取大文件操作分块大小
     * @return  大文件操作分块大小
     */
    public static long getLargeObjectPartSize()
    {
        return LARGE_OBJECT_PART_SIZE;
    }
}
