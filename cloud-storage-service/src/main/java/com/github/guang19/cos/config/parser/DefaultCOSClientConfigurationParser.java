package com.github.guang19.cos.config.parser;

import com.github.guang19.common.ServiceType;
import com.github.guang19.cos.config.COSClientConfiguration;
import com.github.guang19.cos.config.COSClientProperties;
import com.github.guang19.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.cos.config.AliyunOSSClientProperties;
import com.github.guang19.cos.config.exceptions.COSClientConfigurationException;
import com.github.guang19.cos.config.exceptions.ParseCOSClientConfigurationException;
import com.github.guang19.exception.UnknownServiceTypeException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author yangguang
 * @date 2020/2/5
 * @description <p>默认COS客户端配置解析器</p>
 */
public class DefaultCOSClientConfigurationParser implements COSClientConfigurationParser
{

    /**
     * <p>解析COS客户端配置</p>
     *
     * @param configuration 配置文件
     * @return       COS客户端配置
     */
    @Override
    public COSClientConfiguration parse(String configuration)
    {
        return this.parseCOSClientConfiguration(configuration);
    }



    /**
     * <p>解析基础配置</p>
     * @param configuration 配置文件
     */
    private COSClientConfiguration parseCOSClientConfiguration(String configuration)
    {
        if(configuration == null)
        {
            throw new NullPointerException("cos client configuration can not be null.");
        }
        Properties properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(configuration), StandardCharsets.UTF_8))
        {
            properties.load(reader);
            return new COSClientConfiguration(parseCOSClientProperties(properties));
        }
        catch (IOException | NullPointerException | COSClientConfigurationException e)
        {
            throw new ParseCOSClientConfigurationException("an error occurred while parse cos client configuration." ,e);
        }
    }

    /**
     * <p>解析COS客户端基础属性</p>
     * @param properties  配置属性
     */
    private COSClientProperties parseCOSClientProperties(Properties properties)
    {
        //判断COS的secret属性
        if (properties.getProperty("cos.service.secret-id") == null ||
                properties.getProperty("cos.service.secret-key") == null)
        {
            throw new COSClientConfigurationException("property of secret can not be missing.");
        }

        //判断COS服务类型
        String  cosServiceType = properties.getProperty("cos.service.type");
        if(cosServiceType == null)
        {
            throw new COSClientConfigurationException("cos service type can not be null or can not identify cos service type.");
        }
        COSClientProperties cosClientProperties = null;
        //构造腾讯云COS客户端配置属性
        if(cosServiceType.equals(ServiceType.TENCENT_CLOUD.getServiceTYpe()))
        {
            //判断region地域属性
            if(properties.getProperty("cos.service.region") == null)
            {
                throw new COSClientConfigurationException("property of region can not be missing.");
            }
            //判断 app id
            if(properties.getProperty("cos.service.app-id") == null)
            {
                throw new COSClientConfigurationException("property of app id can not be missing.");
            }
            //创建COS客户端属性
            cosClientProperties =
                    new TenCloudCOSClientProperties(properties.getProperty("cos.service.secret-id"),
                                                    properties.getProperty("cos.service.secret-key"),
                                                    properties.getProperty("cos.service.region"),
                                                    properties.getProperty("cos.service.app-id"));
        }
        //构造阿里云OSS客户端配置属性
        else if(cosServiceType.equals(ServiceType.ALIYUN.getServiceTYpe()))
        {
            if(properties.getProperty("cos.service.endpoint") == null && properties.getProperty("cos.service.cname") == null)
            {
                throw new COSClientConfigurationException("properties of endpoint and cname must be configured at least one.");
            }
            cosClientProperties = new AliyunOSSClientProperties(properties.getProperty("cos.service.secret-id"),
                                                                properties.getProperty("cos.service.secret-key"),
                                                                properties.getProperty("cos.service.endpoint"),
                                                                properties.getProperty("cos.service.cname"),
                                                                properties.getProperty("cos.service.upload-img-style"));
        }
        else
        {
            throw new UnknownServiceTypeException("can not identify cos service type.");
        }
        //设置其它属性
        try
        {
            String property = null;
            if((property = properties.getProperty("cos.service.object-template-bucket")) != null)
            {
                cosClientProperties.setObjectTemplateBucket(property);
            }
            if((property = properties.getProperty("cos.service.upload-limit-size")) != null)
            {
                cosClientProperties.setUploadLimitSize(
                        Integer.parseInt(property));
            }
            if((property = properties.getProperty("cos.service.protocol")) != null)
            {
                cosClientProperties.setProtocol(property);
            }
            if((property = properties.getProperty("cos.service.socket-timeout")) != null)
            {
                cosClientProperties.setSocketTimeout(Integer.parseInt(property));
            }
            if((property = properties.getProperty("cos.service.connection-timeout")) != null)
            {
                cosClientProperties.setConnectionTimeout(Integer.parseInt(property));
            }
            if((property = properties.getProperty("cos.service.connection-request-timeout")) != null)
            {
                cosClientProperties.setConnectionRequestTimeout(Integer.parseInt(property));
            }
            if((property = properties.getProperty("cos.service.max-connections")) != null)
            {
                cosClientProperties.setMaxConnections(Integer.parseInt(property));
            }
            if((property = properties.getProperty("cos.service.proxy-ip")) != null)
            {
                cosClientProperties.setProxyIP(property);
            }
            if((property = properties.getProperty("cos.service.proxy-port")) != null)
            {
                cosClientProperties.setProxyPort(Integer.parseInt(property));
            }
            if((property = properties.getProperty("cos.service.proxy-username")) != null)
            {
                cosClientProperties.setProxyUsername(property);
            }
            if((property = properties.getProperty("cos.service.proxy-password")) != null)
            {
                cosClientProperties.setProxyPassword(property);
            }
        }
        catch (Exception e)
        {
            throw new COSClientConfigurationException(e);
        }
        return cosClientProperties;
    }
}
