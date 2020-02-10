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
     *     发送短信,不过此方法只支持一个参数和一个手机号
     * </p>
     * @param phoneNumber       手机号
     * @param param             参数,关于 Param ,可以参考: {@link ParamDTO}
     * @return                  如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     *
     */
    public ResponseDTO sendMessage(String phoneNumber, ParamDTO param);

    /**
     * <p>
     *      发送短信,不过此方法只支持一个参数
     *      并且在多手机号的情况下较单手机号会有延迟
     *      此方法与 send batch 方法不同,此方法只会使用一个短信模板
     * </p>
     * @param phoneNumbers     手机号,允许一个或多个手机号
     * @param param            参数
     * @return                 如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     *
     */
    public ResponseDTO sendMessage(String[] phoneNumbers, ParamDTO param);


    /**
     * <p>
     *     发送短信,虽然此方法只支持一个手机号,但是允许多个参数
     *     关于 Param ,可以参考: {@link ParamDTO}
     * </p>
     * @param phoneNumber   手机号
     * @param params        参数
     * @return              如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     *
     *
     */
    public ResponseDTO sendBatchMessage(String phoneNumber, ParamDTO[] params);

    /**
     * <p>
     *     发送短信,允许多个手机号和多个参数
     *     关于 Param ,可以参考: {@link ParamDTO}
     * </p>
     * @param phoneNumbers      手机号
     * @param params            参数
     * @return                  如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     *
     */
    public ResponseDTO sendBatchMessage(String[] phoneNumbers, ParamDTO[] params);
}
