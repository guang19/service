package com.github.guang19.sms.template;

import com.aliyuncs.CommonRequest;
import com.github.guang19.sms.config.AliyunSMSProfileProperties;
import com.github.guang19.sms.dto.ParamDTO;
import com.github.guang19.sms.dto.ResponseDTO;
import com.github.guang19.sms.util.SMSUtil;
import com.github.guang19.util.CommonUtil;

import java.util.Arrays;
import java.util.StringJoiner;

import static com.github.guang19.sms.util.SMSAction.BATCH;
import static com.github.guang19.sms.util.SMSAction.NORMAL;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>默认阿里云短信服务操作模板</p>
 */
public class DefaultAliyunSMSTemplate extends BaseAliyunSMSTemplate
{
    /**
     * 阿里云sms配置属性构造
     * @param smsProfileProperties  阿里云短信服务配置属性
     */
    public DefaultAliyunSMSTemplate(AliyunSMSProfileProperties smsProfileProperties)
    {
        super(smsProfileProperties);
    }

    /**
     * <p>
     * 发送短信,不过此方法只支持一个参数和一个手机号
     * </p>
     *
     * @param phoneNumber 手机号
     * @param param       参数,关于 Param ,可以参考: {@link ParamDTO}
     * @return          如果发送成功, 则返回BizId, 发送的回执id, 可根据此参数, 查询具体的发送状态
     *
     */
    @Override
    public ResponseDTO sendMessage(String phoneNumber, ParamDTO param)
    {
        return sendNormalMessage(param,phoneNumber);
    }

    /**
     * <p>
     * 发送短信,不过此方法只支持一个参数
     * 并且在多手机号的情况下较单手机号会有延迟
     * 此方法与 send batch 方法不同,此方法只会使用一个短信模板
     * </p>
     *
     * @param phoneNumbers 手机号,允许一个或多个手机号
     * @param param        参数
     * @return            如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     * *
     */
    @Override
    public ResponseDTO sendMessage(String[] phoneNumbers, ParamDTO param)
    {
        return sendNormalMessage(param,phoneNumbers);
    }

    /**
     * <p>
     *     发送普通短信的核心方法
     * </p>
     * @param param             参数
     * @param phoneNumbers       手机号
     * @return             如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     *
     */
    private ResponseDTO sendNormalMessage(ParamDTO param, String ...phoneNumbers)
    {
        CommonUtil.assertObjectNull("param",param);
        CommonUtil.assertArrayEmpty("phoneNumbers",  phoneNumbers);
        CommonRequest request = getSMSRequest(NORMAL);
        request.putQueryParameter("TemplateParam",param.toString());
        if(phoneNumbers.length == 1)
        {
            request.putQueryParameter("PhoneNumbers",phoneNumbers[0]);
        }
        else
        {
            StringJoiner stringJoiner = new StringJoiner(",");
            for(String phoneNumber : phoneNumbers)
            {
                stringJoiner.add(phoneNumber);
            }
            request.putQueryParameter("PhoneNumbers",stringJoiner.toString());
        }
        return smsRequest(request);
    }

    /**
     * <p>
     * 发送短信,虽然此方法只支持一个手机号,但是允许多个参数
     * 关于 Param ,可以参考: {@link ParamDTO}
     * </p>
     *
     * @param phoneNumber 手机号
     * @param params      参数
     * @return            如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     */
    @Override
    public ResponseDTO sendBatchMessage(String phoneNumber, ParamDTO[] params)
    {
        return sendBatchMessage(params,phoneNumber);
    }

    /**
     * <p>
     * 发送短信,允许多个手机号和多个参数
     * 关于 Param ,可以参考: {@link ParamDTO}
     * </p>
     *
     * @param phoneNumbers 手机号
     * @param params       参数
     * @return            如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     */
    @Override
    public ResponseDTO sendBatchMessage(String[] phoneNumbers, ParamDTO[] params)
    {
        return  sendBatchMessage(params,phoneNumbers);
    }

    /**
     * 批量发送短信
     * @param params            参数
     * @param phoneNumbers      手机号
     * @return                  BizId
     */
    private ResponseDTO sendBatchMessage(ParamDTO[] params, String ...phoneNumbers)
    {
        CommonUtil.assertArrayEmpty("params",params);
        CommonUtil.assertArrayEmpty("phoneNumbers",phoneNumbers);
        CommonRequest request = getSMSRequest(BATCH);
        request.putQueryParameter("PhoneNumberJson",SMSUtil.toJson(Arrays.toString(phoneNumbers)));
        request.putQueryParameter("TemplateParamJson",SMSUtil.toJson(Arrays.toString(params)));
        return smsRequest(request);
    }
}
