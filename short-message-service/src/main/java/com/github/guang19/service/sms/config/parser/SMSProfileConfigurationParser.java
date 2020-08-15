package com.github.guang19.service.sms.config.parser;

import com.github.guang19.service.sms.config.SMSProfileConfiguration;

/**
 * @author yangguang
 * @date 2020/2/9
 * @description <p>短信服务配置解析器</p>
 */
public interface SMSProfileConfigurationParser
{
    /**
     * 解析短信服务配置
     * @param config     配置文件
     * @return           短信服务配置
     */
    public abstract SMSProfileConfiguration parse(String config);
}
