package com.github.guang19.cos.template.builder;


import com.github.guang19.cos.template.buckettemplate.COSBucketTemplate;

/**
 * @author yangguang
 * @date 2020/2/6
 * @description
 * <p>
 *   COS存储桶模板构造器
 * </p>
 */
public interface COSBucketTemplateBuilder
{

    /**
     *
     * <p>构造COS存储桶操作模板</p>
     * @return      COS存储桶操作模板
     */
    public abstract COSBucketTemplate build();
}
