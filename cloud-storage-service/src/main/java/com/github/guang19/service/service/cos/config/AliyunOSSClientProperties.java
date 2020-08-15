package com.github.guang19.service.service.cos.config;

import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import lombok.Getter;

/**
 * @author yangguang
 * @date 2020/2/6
 * @description <p>
 *                  阿里云OSS客户端配置属性
 *              </p>
 */
@Getter
public class AliyunOSSClientProperties extends COSClientProperties
{
    //阿里云OSS对外访问的域名
    private final String endpoint;

    //阿里云OSS自定义域名
    private final String cname;

    //图片上传时的样式
    private final String uploadImgStyle;

    //阿里云OSS客户端凭证
    private final CredentialsProvider credentialsProvider;

    /**
     * 主要属性构造
     *
     * @param secretId  secret id
     * @param secretKey secret key
     */
    public AliyunOSSClientProperties(String secretId, String secretKey, String endpoint,String cname,String uploadImgStyle)
    {
        this.endpoint = endpoint;
        this.cname = cname;
        this.uploadImgStyle = uploadImgStyle;
        this.credentialsProvider = new DefaultCredentialProvider(secretId,secretKey);
    }

    //全参构造
    public AliyunOSSClientProperties(String secretId, String secretKey, String objectTemplateBucket, int uploadLimitSize, String protocol, int socketTimeout, int connectionTimeout, int connectionRequestTimeout, int maxConnections, String proxyIP, int proxyPort, String proxyUsername, String proxyPassword, String endpoint,String cname,String uploadImgStyle)
    {
        super(objectTemplateBucket, uploadLimitSize, protocol, socketTimeout, connectionTimeout, connectionRequestTimeout, maxConnections, proxyIP, proxyPort, proxyUsername, proxyPassword);
        this.endpoint = endpoint;
        this.cname = cname;
        this.uploadImgStyle = uploadImgStyle;
        this.credentialsProvider = new DefaultCredentialProvider(secretId,secretKey);
    }
}
