package com.github.guang19.cos.template.builder;

import com.github.guang19.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.cos.config.AliyunOSSClientProperties;
import com.github.guang19.cos.config.COSClientConfiguration;
import com.github.guang19.cos.config.parser.COSClientConfigurationParser;
import com.github.guang19.cos.config.parser.DefaultCOSClientConfigurationParser;
import com.github.guang19.cos.template.buckettemplate.COSBucketTemplate;
import com.github.guang19.cos.template.buckettemplate.DefaultAliyunOSSBucketTemplate;
import com.github.guang19.cos.template.buckettemplate.DefaultTenCloudCOSBucketTemplate;
import com.github.guang19.exception.UnknownServiceTypeException;

/**
 * @author yangguang
 * @date 2020/2/6
 * @description <p>
 *                  COS存储桶操作模板
 *              </p>
 */
public class COSBucketTemplateBuilder
{
    //COS客户端文件解析器
    private COSClientConfigurationParser cosClientConfigurationParser;

    /**
     * 构造函数
     */
    public COSBucketTemplateBuilder()
    {
        this.cosClientConfigurationParser = new DefaultCOSClientConfigurationParser();
    }

    /**
     * <p>构造COS存储桶操作模板</p>
     * @return      COS存储桶操作模板
     */
    public COSBucketTemplate build(String config)
    {
        return this.buildCOSBucketTemplate(config);
    }

    //构造COS模板的核心方法
    private COSBucketTemplate buildCOSBucketTemplate(String config)
    {
        COSClientConfiguration cosClientConfiguration = cosClientConfigurationParser.parse(config);
        if(cosClientConfiguration.getCosClientProperties() instanceof TenCloudCOSClientProperties)
        {

            return new DefaultTenCloudCOSBucketTemplate((TenCloudCOSClientProperties) cosClientConfiguration.getCosClientProperties());
        }
        else if (cosClientConfiguration.getCosClientProperties() instanceof AliyunOSSClientProperties)
        {
            return new DefaultAliyunOSSBucketTemplate((AliyunOSSClientProperties)cosClientConfiguration.getCosClientProperties());
        }
        throw new UnknownServiceTypeException("can not identify cos service type");
    }
}
