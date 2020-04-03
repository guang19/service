package com.github.guang19.cos.template.builder;

import com.github.guang19.cos.config.AliyunOSSClientProperties;
import com.github.guang19.cos.config.COSClientConfiguration;
import com.github.guang19.cos.config.parser.DefaultCOSClientConfigurationParser;
import com.github.guang19.cos.template.buckettemplate.AliyunOSSBucketTemplate;
import com.github.guang19.cos.template.buckettemplate.impl.DefaultAliyunOSSBucketTemplate;

/**
 * @author yangguang
 * @date 2020/4/2
 *
 * <p>
 *   阿里云OSS存储桶模板构造器
 * </p>
 */
public class AliyunOSSBucketTemplateBuilder implements COSBucketTemplateBuilder
{
    //配置文件
    private String configuration;

    /**
     * 构造函数
     * @param configuration 配置文件
     */
    public AliyunOSSBucketTemplateBuilder(String configuration)
    {
        this.configuration = configuration;
    }

    /**
     * <p>构造阿里云OSS存储桶操作模板</p>
     * @return      OSS存储桶操作模板
     */
    @Override
    public AliyunOSSBucketTemplate build()
    {
        return this.buildAliyunOSSBucketTemplate(configuration);
    }

    //构造阿里云OSS存储桶操作模板的核心方法
    private AliyunOSSBucketTemplate buildAliyunOSSBucketTemplate(String configuration)
    {
        COSClientConfiguration cosClientConfiguration = new DefaultCOSClientConfigurationParser().parse(configuration);
        return new DefaultAliyunOSSBucketTemplate((AliyunOSSClientProperties)cosClientConfiguration.getCosClientProperties());
    }
}
