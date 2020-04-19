package com.example.demo.service;

import java.util.Map;

public interface UnionpayService {

    String pay(String orderId,String txnAmt);    //支付接口
    String pay2(String orderId,String txnAmt);   //在线网关支付

    Map<String,String> rollBack(String orderId,String txnTime);    //取消支付超时或者支付结果未知的交易，交易时间昨日23:00-当天23:00,支付异常时使用

    Map<String,String> cancel(String orderId,String queryId);      //撤销当日已完成的交易，全额退款
    Map<String,String> refund(String orderId,String queryId);      //退款，支持部分退款、全额退款

    Map<String,String> query(String orderId,String txnTime);       //查询交易状态
}
