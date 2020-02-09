package com.github.guang19.cosservice.template.builder;

import com.github.guang19.cosservice.config.COSClientConfiguration;
import com.github.guang19.cosservice.config.TenCloudCOSClientProperties;
import com.github.guang19.cosservice.config.exception.UnknownTemplateException;
import com.github.guang19.cosservice.config.parser.COSClientConfigurationParser;
import com.github.guang19.cosservice.template.objecttemplate.COSObjectTemplate;
import com.github.guang19.cosservice.template.objecttemplate.DefaultAliyunOSSObjectTemplate;
import com.github.guang19.cosservice.template.objecttemplate.DefaultTenCloudCOSObjectTemplate;
import com.github.guang19.cosservice.config.AliyunOSSClientProperties;
import com.github.guang19.cosservice.config.parser.DefaultCOSClientConfigurationParser;

/**
 * @author yangguang
 * @date 2020/2/6
 * @description <p>
 *                 COS对象操作模板构造器
 *              </p>
 */
public class COSObjectTemplateBuilder
{
    //COS客户端文件解析器
    private COSClientConfigurationParser cosClientConfigurationParser;

    /**
     * 构造函数
     */
    public COSObjectTemplateBuilder()
    {
        this.cosClientConfigurationParser = new DefaultCOSClientConfigurationParser();
    }

    /**
     * <p>构造COS对象操作模板</p>
     * @return      COS对象操作模板
     */
    public COSObjectTemplate build(String config)
    {
        return this.buildCOSObjectTemplate(config);
    }

    //构造COS对象模板的核心方法
    private COSObjectTemplate buildCOSObjectTemplate(String config)
    {
        COSClientConfiguration cosClientConfiguration = cosClientConfigurationParser.parse(config);
        if(cosClientConfiguration.getCosClientProperties() instanceof TenCloudCOSClientProperties)
        {
            return new DefaultTenCloudCOSObjectTemplate((TenCloudCOSClientProperties) cosClientConfiguration.getCosClientProperties());
        }
        else if(cosClientConfiguration.getCosClientProperties() instanceof AliyunOSSClientProperties)
        {
            return new DefaultAliyunOSSObjectTemplate((AliyunOSSClientProperties) cosClientConfiguration.getCosClientProperties());
        }
        throw new UnknownTemplateException("can not identify cos service type");
    }
}
