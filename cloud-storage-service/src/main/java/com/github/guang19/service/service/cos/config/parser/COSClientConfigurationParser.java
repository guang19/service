package com.github.guang19.service.service.cos.config.parser;

import com.github.guang19.service.service.cos.config.COSClientConfiguration;

/**
 * @author yangguang
 * @date 2020/2/5
 * @description <p>
 *                  COS客户端配置解析器
 *              </p>
 */
@FunctionalInterface
public interface COSClientConfigurationParser
{
    /**
     * <p>解析COS客户端配置</p>
     * @param configuration       配置文件
     * @return             COS客户端配置
     */
    public abstract COSClientConfiguration parse(String configuration);
}
