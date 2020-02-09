package com.github.guang19.config;

import com.aliyuncs.profile.DefaultProfile;
import lombok.Getter;

/**
 * @author yangguang
 * @date 2020/2/9
 * @description <p>阿里云消息服务属性配置</p>
 */
@Getter
public class AliyunSMSProfileProperties extends SMSProfileProperties
{
    //短信发送的域名
    private final String domain = "dysmsapi.aliyuncs.com";

    //配置版本,阿里云提供的固定值
    private final String version = "2017-05-25";

    private DefaultProfile defaultProfile;

    public AliyunSMSProfileProperties(String region, String secretId, String secretKey)
    {
        super(region);
        this.defaultProfile = DefaultProfile.getProfile(region,secretId,secretKey);
    }
}

