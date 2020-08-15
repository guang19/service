package com.github.guang19.service.sms.template;

import com.github.guang19.service.sms.dto.ResponseDTO;

import java.time.LocalDate;

/**
 * @author yangguang
 * @date 2020/2/10
 * @description <p>阿里云短信服务操作模板</p>
 */
public interface AliyunSMSTemplate extends SMSTemplate
{
    /**
     * 查询手机号在指定日期内的发送短信的明细
     * @param phoneNumber       手机号
     * @param sendDate          发送时间.比如查询今天发送的 LocalDate.now(Clock.systemDefaultZone())
     * @param currentPage       当前页码.当前页显示的数量参见 sms.query-page-size 参数
     * @return                  短信的明细. {@link ResponseDTO}
     */
    public ResponseDTO querySendDetails(String phoneNumber, LocalDate sendDate, long currentPage);

    /**
     * 查询手机号在指定日期内的发送短信的明细,由于带有bizId参数,所以只查询有bizId流水的那一条短信
     * @param phoneNumber       手机号
     * @param sendDate          发送时间.比如查询今天发送的 LocalDate.now(Clock.systemDefaultZone())
     * @param currentPage       当前页码.当前页显示的数量参见 sms.query-page-size 参数
     * @param bizId             发送的回执ID
     * @return                  短信的明细. {@link ResponseDTO}
     */
    public ResponseDTO querySendDetail(String phoneNumber, LocalDate sendDate, long currentPage,String bizId);
}
