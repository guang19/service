package com.github.guang19.service.service.cos.template.builder;

import com.github.guang19.service.service.cos.template.objecttemplate.COSObjectTemplate;

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
