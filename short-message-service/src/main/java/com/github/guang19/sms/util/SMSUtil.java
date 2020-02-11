package com.github.guang19.sms.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.http.MethodType;
import com.github.guang19.sms.config.AliyunSMSProfileProperties;
import com.github.guang19.sms.dto.ResponseDTO;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;


/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>短信服务工具类</p>
 */
@Slf4j
public class SMSUtil
{
    private static final Gson gson = new Gson();

    /**
     * 创建阿里云action对应的服务请求
     * @param action                    服务类型
     * @param smsProfileProperties      阿里云短信服务配置属性
     * @return                          服务请求
     */
    public static CommonRequest newAliyunSMSRequest(SMSAction action,AliyunSMSProfileProperties smsProfileProperties)
    {
        CommonRequest request = new CommonRequest();
        //考虑到兼容性,此处使用正常的switch
        switch (action)
        {
            case NORMAL:
                request.setSysAction("SendSms");
                request.putQueryParameter("SignName",smsProfileProperties.getSignNames()[0]);
                break;
            case BATCH:
                request.setSysAction("SendBatchSms");
                request.putQueryParameter("SignNameJson",gson.toJson(smsProfileProperties.getSignNames()));
                break;
            case QUERY:
                request.setSysAction("QuerySendDetails");
                break;
        }
        request.setSysVersion(smsProfileProperties.getVersion());
        request.setSysDomain(smsProfileProperties.getDomain());
        request.setSysMethod(MethodType.POST);
        request.setSysRegionId(smsProfileProperties.getRegion());
        request.putQueryParameter("TemplateCode",smsProfileProperties.getMessageTemplate());
       return request;
    }

    /**
     * 解析阿里云发送短信的相应的 BizId
     * @param commonResponse    阿里云服务短信服务相应
     * @return                  发送的回执id,可根据此参数,查询具体的发送状态
     */
    public static ResponseDTO  parseAliyunSMSResponseBizId(CommonResponse commonResponse)
    {
        return  gson.fromJson(commonResponse.getData(), ResponseDTO.class);
    }

    /**
     * 将对象转为Json
     * @param obj   对象
     * @return      对象的json形式
     */
    public static String toJson(Object obj)
    {
        return gson.toJson(obj);
    }
}
