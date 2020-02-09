package com.github.guang19.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yangguang
 * @date 2020/2/9
 * @description <p>消息服务配置属性</p>
 */
@Getter
@Setter
public class SMSProfileProperties
{
    //region地域
    private final String region;

    //主要属性构造
    protected SMSProfileProperties(String region)
    {
        this.region =region;
    }
}
