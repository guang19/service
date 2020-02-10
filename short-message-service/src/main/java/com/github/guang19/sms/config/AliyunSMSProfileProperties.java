package com.github.guang19.sms.config;

import com.aliyuncs.profile.DefaultProfile;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yangguang
 * @date 2020/2/9
 * @description <p>阿里云短信服务属性配置</p>
 */
@Getter
@Setter
public class AliyunSMSProfileProperties extends SMSProfileProperties
{
    //短信发送的域名
    private final String domain = "dysmsapi.aliyuncs.com";

    //配置版本,阿里云提供的固定值
    private final String version = "2017-05-25";

    //短信配置
    private final DefaultProfile defaultProfile;

    /**
     * 重要属性构造
     * @param region        地域
     * @param signName      短信签名
     * @param secretId      secret id
     * @param secretKey     secret key
     */
    public AliyunSMSProfileProperties(String region, String[] signName,String secretId,String secretKey)
    {
        super(region, signName);
        this.defaultProfile = DefaultProfile.getProfile(region,secretId,secretKey);
    }
}
