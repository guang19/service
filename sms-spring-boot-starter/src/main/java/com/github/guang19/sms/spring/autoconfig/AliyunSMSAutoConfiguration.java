package com.github.guang19.sms.spring.autoconfig;

import com.github.guang19.sms.config.AliyunSMSProfileProperties;
import com.github.guang19.sms.template.AliyunSMSTemplate;
import com.github.guang19.sms.template.DefaultAliyunSMSTemplate;
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
 * @description <p>阿里云短信服务自动配置类</p>
 */
@SpringBootConfiguration
@EnableConfigurationProperties(SMSProperties.class)
@ConditionalOnClass(AliyunSMSTemplate.class)
public class AliyunSMSAutoConfiguration
{
    /**
     * <p>
     *     使用 SMSProperties 创建 AliyunSMSProfileProperties
     * </p>
     * @param smsProperties 短信服务配置属性
     * @return              阿里云短信服务配置属性
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.sm.service",value = "type",havingValue = "aliyun")
    public AliyunSMSProfileProperties smsProfileProperties(@Autowired SMSProperties smsProperties)
    {
        AliyunSMSProfileProperties aliyunSMSProfileProperties = new AliyunSMSProfileProperties(smsProperties.getSecretId(),smsProperties.getSecretKey()
        ,smsProperties.getRegion(),smsProperties.getSignNames().split(","));
        aliyunSMSProfileProperties.setMessageTemplate(smsProperties.getMessageTemplate());
        if(smsProperties.getQueryPageSize() != null)
        {
            aliyunSMSProfileProperties.setQueryPageSize(smsProperties.getQueryPageSize());
        }
        return aliyunSMSProfileProperties;
    }

    /**
     * <p>
     *     自动配置 : spring.sm.service.type=aliyun
     * </p>
     * @param aliyunSMSProfileProperties        阿里云短信服务配置属性
     * @return                                  阿里云短信服务操作模板
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(AliyunSMSProfileProperties.class)
    @ConditionalOnProperty(prefix = "spring.sm.service",value = "type",havingValue = "aliyun")
    public AliyunSMSTemplate aliyunSMSTemplate(@Autowired AliyunSMSProfileProperties aliyunSMSProfileProperties)
    {
        return new DefaultAliyunSMSTemplate(aliyunSMSProfileProperties);
    }
}