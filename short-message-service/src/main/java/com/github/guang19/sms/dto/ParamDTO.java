package com.github.guang19.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>发送短信时,参数模型,此类很简单</p>
 */
@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ParamDTO implements Serializable
{
    //序列化ID
    private static final long serialVersionUID = -5118597682444371148L;

    //参数名:name
    private String name;

    //参数值:value
    private String value;

    /**
     * toString
     * @return  json表示形式
     */
    @Override
    public String toString()
    {
        return ("{").concat(name.concat((":").concat(value.concat("}"))));
    }
}
