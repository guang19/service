package com.github.guang19.cos.config;

import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import lombok.Getter;


/**
 * @author yangguang
 * @date 2020/2/3
 * @description <p>
 *                  腾讯云COS客户端属性
 *              </p>
 */
@Getter
public class TenCloudCOSClientProperties extends COSClientProperties
{
    //region
    private final String region;

    //app id
    private final String appId;

    //腾讯云客户端凭证
    private final COSCredentials cosCredentials;

    /**
     * 主要属性构造
     *
     * @param secretId  secret id
     * @param secretKey secret key
     * @param region    region
     */
    public TenCloudCOSClientProperties(String secretId, String secretKey, String region, String appId)
    {
        this.region = region;
        this.appId = appId;
        this.cosCredentials = new BasicCOSCredentials(secretId,secretKey);
    }

    //全参构造函数
    public TenCloudCOSClientProperties(String secretId, String secretKey, String objectTemplateBucket, int uploadLimitSize, String protocol, int socketTimeout, int connectionTimeout, int connectionRequestTimeout, int maxConnections, String proxyIP, int proxyPort, String proxyUsername, String proxyPassword,String region, String appId)
    {
        super(objectTemplateBucket, uploadLimitSize, protocol, socketTimeout, connectionTimeout, connectionRequestTimeout, maxConnections, proxyIP, proxyPort, proxyUsername, proxyPassword);
        this.region = region;
        this.appId = appId;
        this.cosCredentials = new BasicCOSCredentials(secretId,secretKey);
    }
}
