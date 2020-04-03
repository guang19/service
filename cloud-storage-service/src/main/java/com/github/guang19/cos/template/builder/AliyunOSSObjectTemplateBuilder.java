package com.github.guang19.cos.template.builder;

import com.github.guang19.cos.config.AliyunOSSClientProperties;
import com.github.guang19.cos.config.COSClientConfiguration;
import com.github.guang19.cos.config.parser.DefaultCOSClientConfigurationParser;
import com.github.guang19.cos.template.objecttemplate.AliyunOSSObjectTemplate;
import com.github.guang19.cos.template.objecttemplate.impl.DefaultAliyunOSSObjectTemplate;

/**
 * @author yangguang
 * @date 2020/4/2
 *
 * <p>
 *  阿里云OSS对象模板构造器
 * </p>
 */
public class AliyunOSSObjectTemplateBuilder implements COSObjectTemplateBuilder
{
    //配置文件
    private String configuration;

    /**
     * 构造函数
     * @param configuration 配置文件
     */
    public AliyunOSSObjectTemplateBuilder(String configuration)
    {
        this.configuration = configuration;
    }

    /**
     * <p>构造阿里云OSS对象操作模板</p>
     * @return      OSS对象操作模板
     */
    public AliyunOSSObjectTemplate build()
    {
        return this.buildAliyunOSSObjectTemplate(configuration);
    }

    //构造阿里云OSS对象操作模板的核心方法
    private AliyunOSSObjectTemplate buildAliyunOSSObjectTemplate(String configuration)
    {
        COSClientConfiguration cosClientConfiguration = new DefaultCOSClientConfigurationParser().parse(configuration);
        return new DefaultAliyunOSSObjectTemplate((AliyunOSSClientProperties) cosClientConfiguration.getCosClientProperties());
    }
}
