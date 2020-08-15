package com.github.guang19.service.cos.spring.autoconfig;

import com.github.guang19.service.service.cos.config.AliyunOSSClientProperties;
import com.github.guang19.service.service.cos.template.buckettemplate.AliyunOSSBucketTemplate;
import com.github.guang19.service.service.cos.template.buckettemplate.impl.DefaultAliyunOSSBucketTemplate;
import com.github.guang19.service.service.cos.template.objecttemplate.AliyunOSSObjectTemplate;
import com.github.guang19.service.service.cos.template.objecttemplate.impl.DefaultAliyunOSSObjectTemplate;
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
 * @description <p>阿里云OSS服务自动配置</p>
 */
@SpringBootConfiguration
@EnableConfigurationProperties(COSProperties.class)
@ConditionalOnClass({AliyunOSSObjectTemplate.class, AliyunOSSBucketTemplate.class})
public class AliyunOSSAutoConfiguration
{
    /**
     * <p>
     *     使用 COSProperties 创建 AliyunOSSClientProperties
     * </p>
     * @param cosProperties   cosProperties
     * @return                aliyunOSSClientProperties
     */
    @Bean("aliyunOSSClientProperties")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.cos.service",value = "type",havingValue = "aliyun")
    public AliyunOSSClientProperties aliyunOSSClientProperties(@Autowired COSProperties cosProperties)
    {
        AliyunOSSClientProperties aliyunOSSClientProperties =
                new AliyunOSSClientProperties(cosProperties.getSecretId(),cosProperties.getSecretKey()
                        ,cosProperties.getEndpoint(),cosProperties.getCname(),cosProperties.getUploadImgStyle());
        aliyunOSSClientProperties.setObjectTemplateBucket(cosProperties.getObjectTemplateBucket());
        if(cosProperties.getUploadLimitSize() != null)
        {
            aliyunOSSClientProperties.setUploadLimitSize(cosProperties.getUploadLimitSize());
        }
        if(cosProperties.getProtocol() != null)
        {
            aliyunOSSClientProperties.setProtocol(cosProperties.getProtocol());
        }
        if(cosProperties.getSocketTimeout() != null)
        {
            aliyunOSSClientProperties.setSocketTimeout(cosProperties.getSocketTimeout());
        }
        if(cosProperties.getConnectionTimeout() != null)
        {
            aliyunOSSClientProperties.setConnectionTimeout(cosProperties.getConnectionTimeout());
        }
        if(cosProperties.getConnectionRequestTimeout() != null)
        {
            aliyunOSSClientProperties.setConnectionRequestTimeout(cosProperties.getConnectionRequestTimeout());
        }
        if(cosProperties.getMaxConnections() != null)
        {
            aliyunOSSClientProperties.setMaxConnections(cosProperties.getMaxConnections());
        }
        if(cosProperties.getProxyIp() != null)
        {
            aliyunOSSClientProperties.setProxyIP(cosProperties.getProxyIp());
        }
        if(cosProperties.getProxyPort() != null)
        {
            aliyunOSSClientProperties.setProxyPort(cosProperties.getProxyPort());
        }
        if(cosProperties.getProxyUsername() != null)
        {
            aliyunOSSClientProperties.setProxyUsername(cosProperties.getProxyUsername());
        }
        if(cosProperties.getProxyPassword() != null)
        {
            aliyunOSSClientProperties.setProxyPassword(cosProperties.getProxyPassword());
        }
        return aliyunOSSClientProperties;
    }

    /**
     * 自动配置: spring.cos.service.enabled=true
     * @param aliyunOSSClientProperties 阿里云OSS客户端配置属性
     * @return  阿里云OSS对象操作模板
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(AliyunOSSClientProperties.class)
    @ConditionalOnProperty(prefix = "spring.cos.service",value = "type",havingValue = "aliyun")
    public AliyunOSSObjectTemplate aliyunOSSObjectTemplate(@Autowired AliyunOSSClientProperties aliyunOSSClientProperties)
    {
        return new DefaultAliyunOSSObjectTemplate(aliyunOSSClientProperties);
    }

    /**
     * 自动配置: spring.cos.service.enabled=true
     * @param aliyunOSSClientProperties 阿里云OSS客户端配置属性
     * @return  阿里云OSS存储桶个操作模板
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(AliyunOSSClientProperties.class)
    @ConditionalOnProperty(prefix = "spring.cos.service",value = "type",havingValue = "aliyun")
    public AliyunOSSBucketTemplate aliyunOSSBucketTemplate(@Autowired AliyunOSSClientProperties aliyunOSSClientProperties)
    {
        return new DefaultAliyunOSSBucketTemplate(aliyunOSSClientProperties);
    }
}
