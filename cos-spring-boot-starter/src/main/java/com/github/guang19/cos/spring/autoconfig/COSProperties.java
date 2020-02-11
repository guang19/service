package com.github.guang19.cos.spring.autoconfig;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yangguang
 * @date 2020/2/11
 * @description <p>COS服务配置属性,此类用于构造原生COSClientProperties</p>
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "spring.cos.service",ignoreInvalidFields = true)
public class COSProperties
{
    //SecretId / AccesskeyId
    private String secretId;

    //SecretKey / Access Key Secret
    private String secretKey;

    //腾讯云的region
    private String region;

    //腾讯云的app id
    private String appId;

    //阿里云OSS对外访问的域名
    private String endpoint;

    //阿里云OSS自定义域名
    private String cname;

    //阿里云图片上传时的样式
    private String uploadImgStyle;

    /********************************以下为通用配置 common properties **********************************/

    //object template
    private String objectTemplateBucket;

    //上传对象的限制大小
    private Integer uploadLimitSize;

    //transfer protocol
    private String protocol;

    //socket数据传输超时时间
    private Integer socketTimeout;

    //客户端与cos服务器建立连接的超时时间
    private Integer connectionTimeout;

    //从连接池中获取连接的超时时间
    private Integer connectionRequestTimeout;

    //允许打开的最大http连接数
    private Integer maxConnections;

    //代理服务器的主机地址
    private String proxyIp;

    //代理服务器的主机端口
    private Integer proxyPort;

    //代理服务器验证的用户名
    private String proxyUsername;

    //代理服务器验证的密码
    private String proxyPassword;
}
