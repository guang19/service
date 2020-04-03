package com.github.guang19.cos.template.builder;

import com.github.guang19.cos.config.COSClientConfiguration;
import com.github.guang19.cos.config.TenCloudCOSClientProperties;
import com.github.guang19.cos.config.parser.COSClientConfigurationParser;
import com.github.guang19.cos.template.objecttemplate.COSObjectTemplate;
import com.github.guang19.cos.template.objecttemplate.impl.DefaultAliyunOSSObjectTemplate;
import com.github.guang19.cos.template.objecttemplate.impl.DefaultTenCloudCOSObjectTemplate;
import com.github.guang19.cos.config.AliyunOSSClientProperties;
import com.github.guang19.cos.config.parser.DefaultCOSClientConfigurationParser;
import com.github.guang19.exception.UnknownServiceTypeException;

/**
 * @author yangguang
 * @date 2020/2/6
 * @description
 *   <p>
 *   COS对象模板构造器
 *  </p>
 */
public interface COSObjectTemplateBuilder
{

    /**
     * <p>构造COS对象操作模板</p>
     * @return      COS对象操作模板
     */
    public abstract COSObjectTemplate build();
}
