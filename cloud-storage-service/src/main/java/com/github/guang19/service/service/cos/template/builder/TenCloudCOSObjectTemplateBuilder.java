package com.github.guang19.service.service.cos.template.builder;

import com.github.guang19.service.service.cos.config.COSClientConfiguration;
import com.github.guang19.service.service.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.service.service.cos.config.parser.DefaultCOSClientConfigurationParser;
import com.github.guang19.service.service.cos.template.objecttemplate.TenCloudCOSObjectTemplate;
import com.github.guang19.service.service.cos.template.objecttemplate.impl.DefaultTenCloudCOSObjectTemplate;

/**
 * @author yangguang
 * @date 2020/4/2
 *
 * <p>
 *   腾讯云COS对象模板构造器
 * </p>
 */
public class TenCloudCOSObjectTemplateBuilder implements COSObjectTemplateBuilder
{

    //配置文件
    private String configuration;

    /**
     * 构造函数
     * @param configuration 配置文件
     */
    public TenCloudCOSObjectTemplateBuilder(String configuration)
    {
        this.configuration = configuration;
    }

    /**
     * <p>构造腾讯云COS对象操作模板</p>
     * @return      COS对象操作模板
     */
    public TenCloudCOSObjectTemplate build()
    {
        return this.buildTenCloudCOSObjectTemplate(configuration);
    }

    //构造腾讯云COS对象操作模板的核心方法
    private TenCloudCOSObjectTemplate buildTenCloudCOSObjectTemplate(String configuration)
    {
        COSClientConfiguration cosClientConfiguration = new DefaultCOSClientConfigurationParser().parse(configuration);
        return new DefaultTenCloudCOSObjectTemplate((TenCloudCOSClientProperties) cosClientConfiguration.getCosClientProperties());
    }
}
