package com.github.guang19.service.sms.config.parser;

import com.github.guang19.service.common.ServiceType;
import com.github.guang19.service.sms.config.AliyunSMSProfileProperties;
import com.github.guang19.service.sms.config.SMSProfileConfiguration;
import com.github.guang19.service.sms.config.SMSProfileProperties;
import com.github.guang19.service.sms.config.exceptions.ParseSMSProfileConfigurationException;
import com.github.guang19.service.sms.config.exceptions.SMSProfileConfigurationException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author yangguang
 * @date 2020/2/9
 * @description <p>默认短信服务配置解析器</p>
 */
public class DefaultSMSProfileConfigurationParser implements SMSProfileConfigurationParser
{
    /**
     * 解析短信服务配置
     *
     * @param config 配置文件
     * @return 短信服务配置
     */
    @Override
    public SMSProfileConfiguration parse(String config)
    {
        return this.parseSMSProfileConfiguration(config);
    }

    //解析sms短信服务配置的核心方法
    private SMSProfileConfiguration parseSMSProfileConfiguration(String config)
    {
       if(config == null)
       {
           throw new NullPointerException("sms configuration can not be null.");
       }
        Properties properties = new Properties();
       try(InputStreamReader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(config), StandardCharsets.UTF_8))
       {
           properties.load(reader);
           return new SMSProfileConfiguration(parseSMSProfileProperties(properties));
       }
       catch (IOException | NullPointerException e)
       {
           throw new ParseSMSProfileConfigurationException("an error occurred while parse sms profile configuration.",e);
       }
    }

    //解析sms配置属性
    private SMSProfileProperties parseSMSProfileProperties(Properties properties)
    {
        if(properties.getProperty("sms.secret-id") == null || properties.getProperty("sms.secret-key") == null)
        {
            throw new SMSProfileConfigurationException("property of secret can not be missing.");
        }
        if(properties.getProperty("sms.sign-name") == null)
        {
            throw new SMSProfileConfigurationException("property of sign name can not be missing.");
        }

        String smsServiceType = properties.getProperty("sms.type");
        if(smsServiceType == null)
        {
            throw new SMSProfileConfigurationException("short message service type can not be null or can not identify short message service type.");
        }
        SMSProfileProperties smsProfileProperties = null;
        if(smsServiceType.equals(ServiceType.ALIYUN.getServiceTYpe()))
        {
            if(properties.getProperty("sms.region") == null)
            {
                throw new SMSProfileConfigurationException("property of region can not be missing.");
            }

            smsProfileProperties = new AliyunSMSProfileProperties(
                    properties.getProperty("sms.secret-id"),
                    properties.getProperty("sms.secret-key"),
                    properties.getProperty("sms.region"),
                    properties.getProperty("sms.sign-name").split(","));
        }
        else
        {
            throw new SMSProfileConfigurationException("can not identify short message service type.");
        }
        String property = null;
        try
        {
            if((property = properties.getProperty("sms.query-page-size")) != null)
            {
                smsProfileProperties.setQueryPageSize(Long.parseLong(property));
            }
            if((property = properties.getProperty("sms.template")) != null)
            {
                smsProfileProperties.setTemplate(property);
            }
        }
        catch (Exception e)
        {
            throw new SMSProfileConfigurationException(e);
        }

        return smsProfileProperties;
    }
}
