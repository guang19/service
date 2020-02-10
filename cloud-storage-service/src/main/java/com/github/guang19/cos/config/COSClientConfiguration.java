package com.github.guang19.cos.config;

import lombok.Getter;

;

/**
 * @author yangguang
 * @date 2020/2/3
 * @description <p>COS客户端配置</p>
 */

@Getter
public class COSClientConfiguration
{
    //COS客户端属性
    private final COSClientProperties cosClientProperties;

    /**
     * COS客户端属性构造
     * @param cosClientProperties   COS客户端属
     */
    public COSClientConfiguration(COSClientProperties cosClientProperties)
    {
        this.cosClientProperties = cosClientProperties;
    }
}
