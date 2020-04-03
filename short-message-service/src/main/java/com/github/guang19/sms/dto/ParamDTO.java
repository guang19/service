package com.github.guang19.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>发送短信时,参数模型,此类很简单</p>
 */
@Getter
public class ParamDTO implements Serializable
{
    //序列化ID
    private static final long serialVersionUID = -5118597682444371148L;

    //存储桶参数
    private final Map<String,String> map = new HashMap<>();

    /**
     * 放入参数
     * @param key       参数名,如:code
     * @param value     参数值,如:123456
     * @return          this:ParamDTO
     */
    public ParamDTO put(String key,String value)
    {
        this.map.put(key,value);
        return this;
    }

    /**
     * 获取参数对应的值
     * @param key       参数名
     * @return         参数值
     */
    public String get(String key)
    {
        return this.map.get(key);
    }

    /**
     * toString
     * @return  json表示形式
     */
    @Override
    public String toString()
    {
        return map.toString();
    }
}
