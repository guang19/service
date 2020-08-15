package com.github.guang19.service.sms.template.builder;

import com.github.guang19.service.sms.template.SMSTemplate;

/**
 * @author yangguang
 * @date 2020/4/2
 *
 * <p>
 *      短信服务模板构造器
 * </p>
 */
public interface SMSTemplateBuilder
{
    /**
     * 构造SMS短信服务操作模板
     * @return          SMS操作模板
     */
    public abstract SMSTemplate build();
}
