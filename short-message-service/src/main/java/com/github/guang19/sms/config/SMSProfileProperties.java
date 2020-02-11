package com.github.guang19.sms.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yangguang
 * @date 2020/2/9
 * @description <p>短信服务配置属性</p>
 */
@Getter
@Setter
public class SMSProfileProperties
{
    //region地域
    private final String region;

    //短信签名
    private final String[] signNames;

    //短信模板
    private String messageTemplate;

    //默认查询数据的page size
    private final long DEFAULT_QUERY_PAGE_SIZE = 50L;
    //查询数据的 page size
    private long queryPageSize = DEFAULT_QUERY_PAGE_SIZE;


    /**
     * 全参构造
     * @param region            地域
     * @param signNames          短信签名
     */
    protected SMSProfileProperties(String region, String[] signNames)
    {
        this.region = region;
        this.signNames = signNames;
    }
}
