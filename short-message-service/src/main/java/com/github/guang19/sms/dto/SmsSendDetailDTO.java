package com.github.guang19.sms.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>查询发送短信的明细，此 DTO 根据 阿里云短信服务提供的文档封装的</p>
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailDTO implements Serializable
{
    //序列化ID
    private static final long serialVersionUID = -2333956853332333683L;

    //短信内容,也就是短信模板填充参数后的内容，如: 您的验证码为 : 123456, 请在5分钟内完成验证 !
    private String Content;

    //运营商短信状态码.
    private String ErrCode;

    //外部流水扩展字段
    private String OutId;

    //接收短信的手机号
    private String PhoneNum;

    //短信接受日期和时间
    private String ReceiveDate;

    //短信发送日期和时间
    private String SendDate;

    //短信发送状态,包括: 1. 等待回执　2.发送失败  3.发送成功
    private Long SendStatus;

    //模板code
    private String TemplateCode;
}
