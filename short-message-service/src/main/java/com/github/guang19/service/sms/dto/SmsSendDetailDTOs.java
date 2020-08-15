package com.github.guang19.service.sms.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>此类是为了兼容短信服务返回的json数据,个人认为这点是阿里云的短信接口没写好,导致我这里必须写这个多余的类,来包含这个数组</p>
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailDTOs implements Serializable
{
    //序列化ID
    private static final long serialVersionUID = -6043353995258374977L;

    //短信明细数组
    private SmsSendDetailDTO[] SmsSendDetailDTO;
}
