package com.github.guang19.config.parser;

import com.github.guang19.config.SMSProfileConfiguration;

/**
 * @author yangguang
 * @date 2020/2/9
 * @description <p>消息服务配置解析器</p>
 */
public interface SMSProfileConfigurationParser
{
    /**
     * 解析消息服务配置
     * @param config     配置文件
     * @return           消息服务配置
     */
    public abstract SMSProfileConfiguration parse(String config);
}
