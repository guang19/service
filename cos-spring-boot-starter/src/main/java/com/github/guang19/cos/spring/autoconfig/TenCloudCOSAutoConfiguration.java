package com.github.guang19.cos.spring.autoconfig;


import com.github.guang19.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.cos.template.buckettemplate.impl.DefaultTenCloudCOSBucketTemplate;
import com.github.guang19.cos.template.buckettemplate.TenCloudCOSBucketTemplate;
import com.github.guang19.cos.template.objecttemplate.impl.DefaultTenCloudCOSObjectTemplate;
import com.github.guang19.cos.template.objecttemplate.TenCloudCOSObjectTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author yangguang
 * @date 2020/2/11
 * @description <p>腾讯云COS服务自动配置</p>
 */
@SpringBootConfiguration
@EnableConfigurationProperties(COSProperties.class)
@ConditionalOnClass({TenCloudCOSObjectTemplate.class, TenCloudCOSBucketTemplate.class})
public class TenCloudCOSAutoConfiguration
{

    /**
     * <p>
     *     使用 COSProperties 创建 TenCloudCOSClientProperties
     * </p>
     * @param cosProperties   cosProperties
     * @return                tencloudCOSClientProperties
     */
    @Bean("tencloudCOSClientProperties")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.cos.service",value = "type",havingValue = "tencent cloud")
    public TenCloudCOSClientProperties tencloudCOSClientProperties(@Autowired COSProperties cosProperties)
    {
        TenCloudCOSClientProperties tenCloudCOSClientProperties =
                new TenCloudCOSClientProperties(cosProperties.getSecretId(),cosProperties.getSecretKey()
                        ,cosProperties.getRegion(),cosProperties.getAppId());
        tenCloudCOSClientProperties.setObjectTemplateBucket(cosProperties.getObjectTemplateBucket());
        if(cosProperties.getUploadLimitSize() != null)
        {
            tenCloudCOSClientProperties.setUploadLimitSize(cosProperties.getUploadLimitSize());
        }
        if(cosProperties.getProtocol() != null)
        {
            tenCloudCOSClientProperties.setProtocol(cosProperties.getProtocol());
        }
        if(cosProperties.getSocketTimeout() != null)
        {
            tenCloudCOSClientProperties.setSocketTimeout(cosProperties.getSocketTimeout());
        }
        if(cosProperties.getConnectionTimeout() != null)
        {
            tenCloudCOSClientProperties.setConnectionTimeout(cosProperties.getConnectionTimeout());
        }
        if(cosProperties.getConnectionRequestTimeout() != null)
        {
            tenCloudCOSClientProperties.setConnectionRequestTimeout(cosProperties.getConnectionRequestTimeout());
        }
        if(cosProperties.getMaxConnections() != null)
        {
            tenCloudCOSClientProperties.setMaxConnections(cosProperties.getMaxConnections());
        }
        if(cosProperties.getProxyIp() != null)
        {
            tenCloudCOSClientProperties.setProxyIP(cosProperties.getProxyIp());
        }
        if(cosProperties.getProxyPort() != null)
        {
            tenCloudCOSClientProperties.setProxyPort(cosProperties.getProxyPort());
        }
        if(cosProperties.getProxyUsername() != null)
        {
            tenCloudCOSClientProperties.setProxyUsername(cosProperties.getProxyUsername());
        }
        if(cosProperties.getProxyPassword() != null)
        {
            tenCloudCOSClientProperties.setProxyPassword(cosProperties.getProxyPassword());
        }
        return tenCloudCOSClientProperties;
    }

    /**
     * 自动配置: spring.cos.service.enabled=true
     * @param tenCloudCOSClientProperties 腾讯云COS客户端配置属性
     * @return  腾讯云COS对象操作模板
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(TenCloudCOSClientProperties.class)
    @ConditionalOnProperty(prefix = "spring.cos.service",value = "type",havingValue = "tencent cloud")
    public TenCloudCOSObjectTemplate tencloudCOSObjectTemplate(@Autowired TenCloudCOSClientProperties tenCloudCOSClientProperties)
    {
        return new DefaultTenCloudCOSObjectTemplate(tenCloudCOSClientProperties);
    }

    /**
     * 自动配置: spring.cos.service.enabled=true
     * @param tenCloudCOSClientProperties 腾讯云COS客户端配置属性
     * @return  腾讯云COS存储桶操作模板
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(TenCloudCOSClientProperties.class)
    @ConditionalOnProperty(prefix = "spring.cos.service",value = "type",havingValue = "tencent cloud")
    public TenCloudCOSBucketTemplate tencloudCOSBucketTemplate(@Autowired TenCloudCOSClientProperties tenCloudCOSClientProperties)
    {
        return new DefaultTenCloudCOSBucketTemplate(tenCloudCOSClientProperties);
    }

}
