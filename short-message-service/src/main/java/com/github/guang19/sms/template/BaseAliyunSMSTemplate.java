package com.github.guang19.sms.template;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.github.guang19.sms.config.AliyunSMSProfileProperties;
import com.github.guang19.sms.dto.ParamDTO;
import com.github.guang19.sms.dto.ResponseDTO;
import com.github.guang19.sms.util.SMSAction;
import com.github.guang19.sms.util.SMSUtil;
import com.github.guang19.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.guang19.sms.util.SMSAction.*;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>阿里云短信服务基础操作模板</p>
 */
public abstract class BaseAliyunSMSTemplate implements AliyunSMSTemplate
{
    //阿里云短信服务客户端
    private final IAcsClient acsClient;

    //存储公用request
    private final Map<SMSAction, CommonRequest> requestMap = new ConcurrentHashMap<>();

    //查询发送短信明细的page size
    private long queryPageSize;

    //logger
    protected final Logger logger = LoggerFactory.getLogger(BaseAliyunSMSTemplate.class);

    /**
     * 阿里云sms配置属性构造
     * @param smsProfileProperties  阿里云短信服务配置属性
     */
    protected BaseAliyunSMSTemplate(AliyunSMSProfileProperties smsProfileProperties)
    {
        this.acsClient = new DefaultAcsClient(smsProfileProperties.getDefaultProfile());
        this.queryPageSize = smsProfileProperties.getQueryPageSize();
        this.requestMap.put(NORMAL,SMSUtil.newAliyunSMSRequest(NORMAL,smsProfileProperties));
        this.requestMap.put(BATCH,SMSUtil.newAliyunSMSRequest(BATCH,smsProfileProperties));
        this.requestMap.put(QUERY,SMSUtil.newAliyunSMSRequest(QUERY,smsProfileProperties));
    }

    /**
     * <p>
     * 发送短信,不过此方法只支持一个参数和一个手机号
     * </p>
     *
     * @param phoneNumber 手机号
     * @param param       参数,关于 Param ,可以参考: {@link ParamDTO}
     * @return           如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     */
    @Override
    public ResponseDTO sendMessage(String phoneNumber, ParamDTO param)
    {
        throw new UnsupportedOperationException("can not send message with base template");
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
     * @return      如果发送成功,则返回发送成功的响应体 : {@link ResponseDTO}
     *
     */
    @Override
    public ResponseDTO sendMessage(String[] phoneNumbers, ParamDTO param)
    {
        throw new UnsupportedOperationException("can not send message with base template");
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
        throw new UnsupportedOperationException("can not send message with base template");
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
        throw new UnsupportedOperationException("can not send message with base template");
    }

    /**
     * 查询手机号在指定日期内的发送短信的明细
     *
     * @param phoneNumber 手机号
     * @param sendDate    发送时间.比如查询今天发送的 LocalDate.now(Clock.systemDefaultZone())
     * @param currentPage 当前页码.当前页显示的数量参见 sm.service.query-page-size 参数
     * @return 短信的明细. {@link ResponseDTO}
     */
    @Override
    public ResponseDTO querySendDetails(String phoneNumber, LocalDate sendDate, long currentPage)
    {
        return queryMessageDetails(phoneNumber,sendDate,currentPage,null);
    }

    /**
     * 查询手机号在指定日期内的发送短信的明细,由于带有bizId参数,所以只查询有bizId流水的那一条短信
     *
     * @param phoneNumber 手机号
     * @param sendDate    发送时间.比如查询今天发送的 LocalDate.now(Clock.systemDefaultZone())
     * @param currentPage 当前页码.当前页显示的数量参见 sm.service.query-page-size 参数
     * @param bizId       发送的回执ID
     * @return 短信的明细. {@link ResponseDTO}
     */
    @Override
    public ResponseDTO querySendDetail(String phoneNumber, LocalDate sendDate, long currentPage, String bizId)
    {
        return queryMessageDetails(phoneNumber,sendDate,currentPage,bizId);
    }

    /**
     * <p>设置短信模板</p>
     * @param messageTemplate 短信模板
     */
    public final synchronized void setMessageTemplate(String messageTemplate)
    {
        CommonUtil.assertObjectNull("message template",messageTemplate);
        for (Map.Entry<SMSAction, CommonRequest> entry : this.requestMap.entrySet())
        {
            entry.getValue().putQueryParameter("TemplateCode", messageTemplate);
        }
    }


    /**
     * 获取查询 page size
     * @return      page size
     */
    public long getQueryPageSize()
    {
        return queryPageSize;
    }

    /**
     * 设置查询时的page size
     * @param queryPageSize page size
     */
    public void setQueryPageSize(long queryPageSize)
    {
        this.queryPageSize = queryPageSize;
    }


    //获取SMS请求
    protected final CommonRequest getSMSRequest(SMSAction action)
    {
        return requestMap.get(action);
    }

    /**
     * <p>查询短信明细</p>
     * @param phoneNumber       手机号
     * @param sendDate          短信的发送日期
     * @param currentPage       当前页码
     * @param bizId             发送流水号
     * @return                  响应体数据
     */
    private final ResponseDTO queryMessageDetails(String phoneNumber, LocalDate sendDate, long currentPage, String bizId)
    {
        CommonRequest request = getSMSRequest(QUERY);
        request.putQueryParameter("PageSize",String.valueOf(queryPageSize));
        request.putQueryParameter("CurrentPage",String.valueOf(currentPage));
        request.putQueryParameter("PhoneNumber",phoneNumber);
        request.putQueryParameter("SendDate",sendDate.toString().replaceAll("-",""));
        request.putQueryParameter("BizId",bizId);
        return smsRequest(request);
    }

    /**
     * <p>发送短信服务请求</p>
     * @param request     请求
     * @return            响应体
     */
    protected final ResponseDTO smsRequest(CommonRequest request)
    {
        try
        {
            return SMSUtil.parseAliyunSMSResponseBizId(acsClient.getCommonResponse(request));
        }
        catch (ClientException e)
        {
            logger.error("error during request short message service : " + e.getMessage());
        }
        return null;
    }
}
