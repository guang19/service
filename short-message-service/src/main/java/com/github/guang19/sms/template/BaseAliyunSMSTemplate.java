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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
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

    //阿里云短信服务配置属性
    private final AliyunSMSProfileProperties aliyunSMSProfileProperties;

    //LOGGER
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseAliyunSMSTemplate.class);

    /**
     * 阿里云sms配置属性构造
     * @param aliyunSMSProfileProperties  阿里云短信服务配置属性
     */
    protected BaseAliyunSMSTemplate(AliyunSMSProfileProperties aliyunSMSProfileProperties)
    {
        this.acsClient = new DefaultAcsClient(aliyunSMSProfileProperties.getDefaultProfile());
        this.aliyunSMSProfileProperties = aliyunSMSProfileProperties;
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
        throw new UnsupportedOperationException("can not send message with base template.");
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
        throw new UnsupportedOperationException("can not send message with base template.");
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
        throw new UnsupportedOperationException("can not send message with base template.");
    }

    /**
     * 查询手机号在指定日期内的发送短信的明细
     *
     * @param phoneNumber 手机号
     * @param sendDate    发送时间.比如查询今天发送的 LocalDate.now(Clock.systemDefaultZone())
     * @param currentPage 当前页码.当前页显示的数量参见 sms.query-page-size 参数
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
     * @param currentPage 当前页码.当前页显示的数量参见 sms.query-page-size 参数
     * @param bizId       发送的回执ID
     * @return 短信的明细. {@link ResponseDTO}
     */
    @Override
    public ResponseDTO querySendDetail(String phoneNumber, LocalDate sendDate, long currentPage, String bizId)
    {
        return queryMessageDetails(phoneNumber,sendDate,currentPage,bizId);
    }


    /**
     * <p>查询短信明细</p>
     * @param phoneNumber       手机号
     * @param sendDate          短信的发送日期
     * @param currentPage       当前页码
     * @param bizId             发送流水号
     * @return                  响应体数据
     */
    private  ResponseDTO queryMessageDetails(String phoneNumber, LocalDate sendDate, long currentPage, String bizId)
    {
        CommonRequest request = newAliyunSMSRequest(QUERY);
        request.putQueryParameter("PageSize",String.valueOf(aliyunSMSProfileProperties.getQueryPageSize()));
        request.putQueryParameter("CurrentPage",String.valueOf(currentPage));
        request.putQueryParameter("PhoneNumber",phoneNumber);
        request.putQueryParameter("SendDate",sendDate.toString().replaceAll("-",""));
        request.putQueryParameter("BizId",bizId);
        return smsRequest(request);
    }


    /**
     * <p>设置短信模板</p>
     * @param template 短信模板
     */
    public void setTemplate(String template)
    {
        aliyunSMSProfileProperties.setTemplate(template);
    }


    /**
     * 获取查询 page size
     * @return      page size
     */
    public long getQueryPageSize()
    {
        return aliyunSMSProfileProperties.getQueryPageSize();
    }

    /**
     * 设置查询时的page size
     * @param queryPageSize page size
     */
    public void setQueryPageSize(long queryPageSize)
    {
        this.aliyunSMSProfileProperties.setQueryPageSize(queryPageSize);
    }

    //创建新请求
    protected final CommonRequest newAliyunSMSRequest(SMSAction smsAction)
    {
        return SMSUtil.newAliyunSMSRequest(smsAction,aliyunSMSProfileProperties);
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
            ResponseDTO response =  SMSUtil.parseAliyunSMSResponseBizId(acsClient.getCommonResponse(request));
            if(!response.getCode().equals("OK"))
            {
                LOGGER.error("current message {} sending error due to {}",response.getRequestId() ,response.getCode());
            }
            return response;
        }
        catch (ClientException e)
        {
            LOGGER.error("error during request short message service : {}", e.getMessage());
            return null;
        }
    }
}
