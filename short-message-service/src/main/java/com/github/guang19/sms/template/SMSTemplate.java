package com.github.guang19.sms.template;

import com.github.guang19.sms.dto.ParamDTO;
import com.github.guang19.sms.dto.ResponseDTO;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>SMS短息服务操作模板</p>
 */
public interface SMSTemplate
{
    /**
     * <p>
     *     发送短信,支持一个手机号
     * </p>
     * @param phoneNumber       手机号
     * @param param             参数,关于 ParamDTO ,可以参考: {@link ParamDTO}
     * @return                  如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     *
     */
    public ResponseDTO sendMessage(String phoneNumber, ParamDTO param);

    /**
     * <p>
     *      发送短信,支持一个手机号
     *      并且在多手机号的情况下较单手机号会有延迟
     *      此方法与 send batch 方法不同,此方法只会使用一个短信签名
     * </p>
     * @param phoneNumbers     手机号,允许一个或多个手机号
     * @param param            参数,关于 ParamDTO ,可以参考: {@link ParamDTO}
     * @return                 如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     *
     */
    public ResponseDTO sendMessage(String[] phoneNumbers, ParamDTO param);




    /**
     * <p>
     *     发送短信,允许多个手机号和多个签名
     *     关于 ParamDTO ,可以参考: {@link ParamDTO}
     *     模板变量值的个数(参数值的个数)必须与手机号码、签名的个数相同、内容一一对应，
     *     表示向指定手机号码中发对应签名的短信
     * </p>
     * @param phoneNumbers      手机号
     * @param params            参数
     * @return                  如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     *
     */
    public ResponseDTO sendBatchMessage(String[] phoneNumbers, ParamDTO[] params);
}
