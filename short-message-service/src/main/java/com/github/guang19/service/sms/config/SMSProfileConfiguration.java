package com.github.guang19.service.sms.config;

import lombok.Getter;

/**
 * @author yangguang
 * @date 2020/2/9
 * @description <p>短信服务配置属性</p>
 */
@Getter
public class SMSProfileConfiguration
{
    //短信服务配置属性
    private final SMSProfileProperties smsProfileProperties;

    /**
     * 短信服务配置属性构造
     * @param smsProfileProperties 短信服务配置属性
     */
    public SMSProfileConfiguration(SMSProfileProperties smsProfileProperties)
    {
        this.smsProfileProperties = smsProfileProperties;
    }
}
