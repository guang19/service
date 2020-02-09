package com.github.guang19.cosservice.util;

/**
 * @author yangguang
 * @date 2020/2/5
 * @description <p>
 *                  cos服务类型
 *              </p>
 */
public enum COSServiceType
{
    //腾讯云COS服务类型
    TENCENT_CLOUD("tencent cloud"),
    //阿里云COS服务类型
    ALIYUN("aliyun");

    //          cos服务类型
    private final String cosServiceType;

    /**
     *
     * @param cosServiceType      cos服务类型
     */
    private COSServiceType(String cosServiceType)
    {
        this.cosServiceType = cosServiceType;
    }

    /**
     * getter
     * @return    cos服务类型
     */
    public String getCosServiceType()
    {
        return cosServiceType;
    }
}
