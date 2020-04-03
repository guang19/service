package com.github.guang19.sms.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>发送短信后返回的结果的JSONBean</p>
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO implements Serializable
{
    //序列化ID
    private static final long serialVersionUID = -7698237212118488143L;

    //请求状态码，如果请求正常,为OK
    private String Code;

    //状态码的描述,如果请求正常,为OK
    private String Message;

    //流水ID,即回执ID
    private String BizId;

    //请求ID
    private String RequestId;

    //短信明细的数组
    private SmsSendDetailDTOs SmsSendDetailDTOs;

    //发送短信的总条数
    private String TotalCount;
}
