package com.example.demo.controller;

import com.example.demo.service.UnionpayService;
import com.unionpay.acp.demo.DemoBase;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.SDKConstants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UnionpayController {

    @Resource
    private UnionpayService unionpayService;

    @RequestMapping("/pay")
    public void pay(HttpServletResponse response) throws Exception{
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.getWriter().write(unionpayService.pay(DemoBase.getOrderId(),"10000"));
    }

    @RequestMapping("/pay2")
    public void pay2(HttpServletResponse response) throws Exception{
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.getWriter().write(unionpayService.pay2(DemoBase.getOrderId(),"20000"));
    }

    @RequestMapping("/query")
    public Map<String,String> query(@RequestParam("orderId") String orderId,
                                    @RequestParam("txnTime") String txnTime){

        return unionpayService.query(orderId,txnTime);
    }

    @RequestMapping("/cancel")
    public Map<String,String> cancel(@RequestParam("queryId") String queryId){
        return unionpayService.cancel(DemoBase.getOrderId(),queryId);
    }

    @RequestMapping("/rollback")
    public Map<String,String> rollback(@RequestParam("orderId") String orderId,
                                       @RequestParam("txnTime") String txnTime){

        return unionpayService.rollBack(orderId,txnTime);
    }

    @RequestMapping("/refund")
    public Map<String,String> refund(@RequestParam("queryId") String queryId){
        return unionpayService.refund(DemoBase.getOrderId(),queryId);
    }

    @RequestMapping("/return")
    public String fun(HttpServletRequest request){
        Map<String,String> result=new HashMap<>();

        System.out.println("====== 前台通知 ======");
        Enumeration<String> names=request.getParameterNames();
        if (names!=null){
            while (names.hasMoreElements()){
                String name=names.nextElement();
                String value=request.getParameter(name);
                result.put(name,value);
                System.out.println(name+" ==> "+value);

                if (result.get(name)==null||"".equals(result.get(name))){
                    result.remove(name);
                }
            }
        }

        if (AcpService.validate(result, SDKConstants.UTF_8_ENCODING)){
            return "success";
        }else {
            return "验签失败";
        }
    }

    @RequestMapping("/notify")
    public void hello2(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Map<String,String> result=new HashMap<>();

        System.out.println("======= 后台通知 ========");
        Enumeration<String> names=request.getParameterNames();
        if (names!=null){
            while (names.hasMoreElements()){
                String name=names.nextElement();
                String value=request.getParameter(name);
                result.put(name,value);
                System.out.println(name+" ==> "+value);

                if (result.get(name)==null||"".equals(result.get(name))){
                    result.remove(name);
                }
            }
        }

        if (AcpService.validate(result, SDKConstants.UTF_8_ENCODING)){
            System.out.println("后台验签成功");
        }else {
            System.out.println("后台验签失败");
        }

        response.getWriter().print("ok");
    }
}
