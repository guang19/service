package com.github.guang19.sms.template.builder;

import com.github.guang19.exception.UnknownServiceTypeException;
import com.github.guang19.sms.config.AliyunSMSProfileProperties;
import com.github.guang19.sms.config.SMSProfileConfiguration;
import com.github.guang19.sms.config.parser.DefaultSMSProfileConfigurationParser;
import com.github.guang19.sms.config.parser.SMSProfileConfigurationParser;
import com.github.guang19.sms.template.DefaultAliyunSMSTemplate;
import com.github.guang19.sms.template.SMSTemplate;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>短信模板构造器</p>
 */
public class SMSTemplateBuilder
{
    //短信配置解析器
    private final SMSProfileConfigurationParser smsProfileConfigurationParser;

    //构造
    public SMSTemplateBuilder()
    {
        this.smsProfileConfigurationParser = new DefaultSMSProfileConfigurationParser();
    }


    /**
     * 构造SMS短信服务操作模板
     * @param config    配置文件
     * @return          SMS操作模板
     */
    public SMSTemplate build(String config)
    {
        return this.buildSMSTemplate(config);
    }

    //构造SMS操作模板的核心方法
    private SMSTemplate buildSMSTemplate(String config)
    {
        SMSProfileConfiguration smsProfileConfiguration = smsProfileConfigurationParser.parse(config);
        if(smsProfileConfiguration.getSmsProfileProperties() instanceof AliyunSMSProfileProperties)
        {
            return new DefaultAliyunSMSTemplate((AliyunSMSProfileProperties) smsProfileConfiguration.getSmsProfileProperties());
        }
        throw new UnknownServiceTypeException("can not identify short message service type");
    }


}
