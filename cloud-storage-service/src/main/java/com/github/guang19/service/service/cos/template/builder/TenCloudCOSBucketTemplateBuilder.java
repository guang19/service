package com.github.guang19.service.service.cos.template.builder;

import com.github.guang19.service.service.cos.config.COSClientConfiguration;
import com.github.guang19.service.service.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.service.service.cos.config.parser.DefaultCOSClientConfigurationParser;
import com.github.guang19.service.service.cos.template.buckettemplate.TenCloudCOSBucketTemplate;
import com.github.guang19.service.service.cos.template.buckettemplate.impl.DefaultTenCloudCOSBucketTemplate;

/**
 * @author yangguang
 * @date 2020/4/2
 *
 * <p>
 *  腾讯云COS存储桶模板构造器
 * </p>
 */
public class TenCloudCOSBucketTemplateBuilder implements COSBucketTemplateBuilder
{
    //配置文件
    private String configuration;

    /**
     * 构造函数
     * @param configuration 配置文件
     */
    public TenCloudCOSBucketTemplateBuilder(String configuration)
    {
        this.configuration = configuration;
    }

    /**
     * <p>构造腾讯云COS存储桶操作模板</p>
     * @return      COS存储桶操作模板
     */
    @Override
    public TenCloudCOSBucketTemplate build()
    {
        return this.buildTencCloudCOSBucketTemplate(configuration);
    }

    //构造腾讯云COS存储桶模板的核心方法
    private TenCloudCOSBucketTemplate buildTencCloudCOSBucketTemplate(String configuration)
    {
        COSClientConfiguration cosClientConfiguration = new DefaultCOSClientConfigurationParser().parse(configuration);
        return new DefaultTenCloudCOSBucketTemplate((TenCloudCOSClientProperties) cosClientConfiguration.getCosClientProperties());
    }
}
