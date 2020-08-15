package com.github.guang19.service.sms.template.builder;

import com.github.guang19.service.sms.config.AliyunSMSProfileProperties;
import com.github.guang19.service.sms.config.SMSProfileConfiguration;
import com.github.guang19.service.sms.config.parser.DefaultSMSProfileConfigurationParser;
import com.github.guang19.service.sms.template.AliyunSMSTemplate;
import com.github.guang19.service.sms.template.DefaultAliyunSMSTemplate;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description
 * <p>
 *     阿里云SMS短信模板构造器
 * </p>
 */
public class AliyunSMSTemplateBuilder implements SMSTemplateBuilder
{
    //配置文件
    private String configuration;

    /**
     * 配置文件构造
     * @param configuration 配置文件
     */
    public AliyunSMSTemplateBuilder(String configuration)
    {
        this.configuration = configuration;
    }


    /**
     * 构造阿里云SMS短信服务操作模板
     * @return          SMS操作模板
     */
    public AliyunSMSTemplate build()
    {
        return this.buildAliyunSMSTemplate(configuration);
    }

    //构造阿里云SMS短信服务操作模板的核心方法
    private AliyunSMSTemplate buildAliyunSMSTemplate(String configuration)
    {
        SMSProfileConfiguration smsProfileConfiguration = new DefaultSMSProfileConfigurationParser().parse(configuration);
        return new DefaultAliyunSMSTemplate((AliyunSMSProfileProperties) smsProfileConfiguration.getSmsProfileProperties());
    }


}
