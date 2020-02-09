package com.github.guang19.util;

/**
 * @author yangguang
 * @date 2020/2/5
 * @description <p>
 *                  sm短信服务类型
 *              </p>
 */
public enum SMServiceType
{
    //阿里云COS服务类型
    ALIYUN("aliyun");

    //       sms短信服务类型
    private final String smServiceType;

    /**
     *
     * @param smServiceType      sms短信服务类型
     */
    private SMServiceType(String smServiceType)
    {
        this.smServiceType = smServiceType;
    }

    /**
     * getter
     * @return    sms短信服务类型
     */
    public String getSmServiceType()
    {
        return smServiceType;
    }
}
