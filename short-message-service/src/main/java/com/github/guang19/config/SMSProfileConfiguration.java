package com.github.guang19.config;

import lombok.Getter;

/**
 * @author yangguang
 * @date 2020/2/9
 * @description <p>消息服务配置属性</p>
 */
@Getter
public class SMSProfileConfiguration
{
    //消息服务配置属性
    private final SMSProfileProperties smsProfileProperties;

    /**
     * 消息服务配置属性构造
     * @param smsProfileProperties  消息服务配置属性
     */
    public SMSProfileConfiguration(SMSProfileProperties smsProfileProperties)
    {
        this.smsProfileProperties = smsProfileProperties;
    }
}
