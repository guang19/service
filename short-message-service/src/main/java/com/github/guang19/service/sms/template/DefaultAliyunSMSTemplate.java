package com.github.guang19.service.sms.template;

import com.aliyuncs.CommonRequest;
import com.github.guang19.service.sms.config.AliyunSMSProfileProperties;
import com.github.guang19.service.sms.dto.ParamDTO;
import com.github.guang19.service.sms.dto.ResponseDTO;
import com.github.guang19.service.sms.util.SMSAction;
import com.github.guang19.service.sms.util.SMSUtil;
import com.github.guang19.service.util.CommonUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

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
     * 发送短信,支持一个手机号
     * </p>
     *
     * @param phoneNumber 手机号
     * @param param       参数,关于 ParamDTO ,可以参考: {@link ParamDTO}
     * @return 如果发送成功, 则返回发送成功的响应体 : {@link ResponseDTO}
     */
    @Override
    public ResponseDTO sendMessage(String phoneNumber, ParamDTO param)
    {
        return requestSingleMessage(param,phoneNumber);
    }

    /**
     * <p>
     * 发送短信,支持一个手机号
     * 并且在多手机号的情况下较单手机号会有延迟
     * 此方法与 send batch 方法不同,此方法只会使用一个短信签名
     * </p>
     *
     * @param phoneNumbers 手机号,允许一个或多个手机号
     * @param param        参数,关于 ParamDTO ,可以参考: {@link ParamDTO}
     * @return 如果发送成功, 则返回发送成功的响应体 : {@link ResponseDTO}
     */
    @Override
    public ResponseDTO sendMessage(String[] phoneNumbers, ParamDTO param)
    {
        return requestSingleMessage(param,phoneNumbers);
    }

    /**
     * 发送普通短信的核心方法
     * @param param         参数
     * @param phoneNumbers  手机号
     * @return              响应体
     */
    private ResponseDTO requestSingleMessage(ParamDTO param,String ...phoneNumbers)
    {
        CommonUtil.assertObjectNull(param,"param cannot be null.");
        CommonUtil.assertArrayEmpty(phoneNumbers,"phone numbers cannot be empty.");
        if(phoneNumbers.length > 1000)
        {
            throw new IllegalArgumentException("send message to up to 1000 phoneNumbers at the same time.");
        }
        CommonRequest request = newAliyunSMSRequest(SMSAction.NORMAL);
        request.putQueryParameter("TemplateParam", SMSUtil.toJson(param.getMap()));
        request.putQueryParameter("PhoneNumbers",phoneNumbers.length == 1 ? phoneNumbers[0] : Arrays.toString(phoneNumbers).replaceAll("[\\[\\]]",""));
        return smsRequest(request);

    }

    /**
     * <p>
     * 发送短信,允许多个手机号和多个签名
     * 关于 ParamDTO ,可以参考: {@link ParamDTO}
     * 模板变量值的个数(参数值的个数)必须与手机号码、签名的个数相同、内容一一对应，
     * 表示向指定手机号码中发对应签名的短信
     * </p>
     *
     * @param phoneNumbers 手机号
     * @param params       参数
     * @return 如果发送成功, 则返回发送成功的响应体 : {@link ResponseDTO}
     */
    @Override
    public ResponseDTO batchSendMessage(String[] phoneNumbers, ParamDTO[] params)
    {
        return requestBatchMessage(phoneNumbers, params);
    }

    /**
     * <p>发送批量短信的核心方法</p>
     * @param phoneNumbers  手机号
     * @param params        参数
     * @return              响应体
     */
    private ResponseDTO requestBatchMessage(String[] phoneNumbers,ParamDTO[] params)
    {
        CommonUtil.assertArrayEmpty(phoneNumbers,"phone numbers cannot be empty.");
        CommonUtil.assertArrayEmpty(params,"params cannot be empty.");
        if(phoneNumbers.length > 100)
        {
            throw new IllegalArgumentException("send message to up to 100 phoneNumbers at the same time.");
        }
        CommonRequest request = newAliyunSMSRequest(SMSAction.BATCH);
        request.putQueryParameter("PhoneNumberJson",SMSUtil.toJson(phoneNumbers));
        request.putQueryParameter("TemplateParamJson",SMSUtil.toJson(Arrays.stream(params).map(ParamDTO::getMap).collect(Collectors.toList())));
        return smsRequest(request);
    }
}
