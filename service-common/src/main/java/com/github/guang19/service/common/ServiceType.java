package com.github.guang19.service.common;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>服务类型</p>
 */
public enum ServiceType
{
    //腾讯云服务
    TENCENT_CLOUD("tencent cloud"),

    //阿里云服务
    ALIYUN("aliyun");

    //          服务类型
    private final String serviceTYpe;

    /**
     *
     * @param serviceTYpe      服务类型
     */
    private ServiceType(String serviceTYpe)
    {
        this.serviceTYpe = serviceTYpe;
    }

    /**
     * getter
     * @return    服务类型
     */
    public String getServiceTYpe()
    {
        return serviceTYpe;
    }
}
