package com.github.guang19.sms.spring.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yangguang
 * @date 2020/2/11
 * @description <p>SMS服务配置属性,此类用于构造原生 SMSProfileProperties</p>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.sm.service",ignoreInvalidFields = true)
public class SMSProperties
{
    //secret id
    private String secretId;

    //secret key
    private String secretKey;

    //region地域
    private String region;

    //短信签名
    private String signNames;

    //短信模板
    private String messageTemplate;

    //查询数据时的page size
    private Long queryPageSize;
}
