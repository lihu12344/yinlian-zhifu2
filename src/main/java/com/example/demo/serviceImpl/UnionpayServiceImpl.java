package com.example.demo.serviceImpl;

import com.example.demo.service.UnionpayService;
import com.unionpay.acp.demo.DemoBase;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.LogUtil;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKConstants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UnionpayServiceImpl implements UnionpayService {

    @Override
    public String pay(String orderId, String txnAmt) {
        Map<String, String> requestData = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        requestData.put("version", SDKConstants.VERSION_5_1_0);       //版本号，全渠道默认值
        requestData.put("encoding", SDKConstants.UTF_8_ENCODING);      //字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("signMethod", SDKConfig.getConfig().getSignMethod());  //签名方法
        requestData.put("txnType", "01");      //交易类型 ，01：消费
        requestData.put("txnSubType", "01");   //交易子类型， 01：自助消费
        requestData.put("bizType", "000201");  //业务类型，B2C网关支付，手机wap支付
        requestData.put("channelType", "07");  //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机


        /***商户接入参数***/
        requestData.put("merId", "777290058180063");         //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put("accessType", "0");                    //接入类型，0：直连商户
        requestData.put("orderId",orderId);      //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        requestData.put("txnTime", DemoBase.getCurrentTime());     //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put("currencyCode", "156");   //交易币种（境内商户一般是156 人民币）
        requestData.put("txnAmt", txnAmt);        //交易金额，单位分，不要带小数点

        requestData.put("accType","01");         //账号类型
        String accNo=AcpService.encryptData("6216261000000000018",SDKConstants.UTF_8_ENCODING);
        requestData.put("accNo",accNo);

        requestData.put("encryptCertId",AcpService.getEncryptCertId());       //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下

        Map<String,String> customerInfoMap=new HashMap<>();
        customerInfoMap.put("phoneNo","13552535506");
        String customerInfoStr = AcpService.getCustomerInfoWithEncrypt(customerInfoMap,null,DemoBase.encoding);
        requestData.put("customerInfo",customerInfoStr);


        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
        //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
        //异步通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
        requestData.put("frontUrl", DemoBase.frontUrl);

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知 2.http https均可 3.收单后台通知后需要10秒内返回http200或302状态码
        // 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        // 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        requestData.put("backUrl", DemoBase.backUrl);


        // 报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> submitFromData = AcpService.sign(requestData, SDKConstants.UTF_8_ENCODING);

        //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();

        //生成自动跳转的Html表单
        return AcpService.createAutoFormHtml(requestFrontUrl, submitFromData,SDKConstants.UTF_8_ENCODING);
    }

    @Override
    public String pay2(String orderId, String txnAmt) {
        Map<String, String> requestData = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        requestData.put("version", SDKConstants.VERSION_5_1_0);       //版本号，全渠道默认值
        requestData.put("encoding", SDKConstants.UTF_8_ENCODING);      //字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put("signMethod", SDKConfig.getConfig().getSignMethod());  //签名方法
        requestData.put("txnType", "01");      //交易类型 ，01：消费
        requestData.put("txnSubType", "01");   //交易子类型， 01：自助消费
        requestData.put("bizType", "000201");  //业务类型，B2C网关支付，手机wap支付
        requestData.put("channelType", "07");  //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机


        /***商户接入参数***/
        requestData.put("merId", "777290058180063");         //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put("accessType", "0");                    //接入类型，0：直连商户
        requestData.put("orderId",orderId);      //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        requestData.put("txnTime", DemoBase.getCurrentTime());     //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put("currencyCode", "156");   //交易币种（境内商户一般是156 人民币）
        requestData.put("txnAmt", txnAmt);        //交易金额，单位分，不要带小数点

        requestData.put("accType","01");         //账号类型
        String accNo=AcpService.encryptData("6216261000000000018",SDKConstants.UTF_8_ENCODING);
        requestData.put("accNo",accNo);

        requestData.put("encryptCertId",AcpService.getEncryptCertId());       //加密证书的certId，配置在acp_sdk.properties文件 acpsdk.encryptCert.path属性下
/*
        Map<String,String> customerInfoMap=new HashMap<>();
        customerInfoMap.put("phoneNo","13552535506");
        String customerInfoStr = AcpService.getCustomerInfoWithEncrypt(customerInfoMap,null,DemoBase.encoding);
        requestData.put("customerInfo",customerInfoStr);
*/

        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
        //如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
        //异步通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
        requestData.put("frontUrl", DemoBase.frontUrl);

        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
        //后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知 2.http https均可 3.收单后台通知后需要10秒内返回http200或302状态码
        // 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
        // 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
        requestData.put("backUrl", DemoBase.backUrl);


        // 报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
        /**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
        //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> submitFromData = AcpService.sign(requestData, SDKConstants.UTF_8_ENCODING);

        //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
        String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();

        //生成自动跳转的Html表单
        return AcpService.createAutoFormHtml(requestFrontUrl, submitFromData,SDKConstants.UTF_8_ENCODING);
    }

    @Override
    public Map<String, String> query(String orderId,String txnTime) {
        Map<String, String> data = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", SDKConstants.VERSION_5_1_0);    //版本号
        data.put("encoding", SDKConstants.UTF_8_ENCODING);  //字符集编码
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        data.put("txnType", "00");                             //交易类型
        data.put("txnSubType", "00");                          //交易子类型
        data.put("bizType", "000000");                         //业务类型

        /***商户接入参数***/
        data.put("merId","777290058180063");                  //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改
        /***要调通交易以下字段必须修改***/
        data.put("orderId", orderId);                //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
        data.put("txnTime", txnTime);                //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间


        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
        Map<String, String> reqData = AcpService.sign(data,DemoBase.encoding);                 //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getSingleQueryUrl();                                                     //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl

        Map<String, String> rspData = AcpService.post(reqData,url,DemoBase.encoding);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》

        if(!rspData.isEmpty()){
            if(AcpService.validate(rspData, DemoBase.encoding)){
                LogUtil.writeLog("验证签名成功");
                if(("00").equals(rspData.get("respCode"))){//如果查询交易成功
                    String origRespCode = rspData.get("origRespCode");
                    if(("00").equals(origRespCode)){
                        //交易成功，更新商户订单状态
                        //TODO

                        System.out.println("交易成功");
                    }else if(("03").equals(origRespCode)||
                            ("04").equals(origRespCode)||
                            ("05").equals(origRespCode)){
                        //订单处理中或交易状态未明，需稍后发起交易状态查询交易 【如果最终尚未确定交易是否成功请以对账文件为准】
                        //TODO

                        System.out.println("交易状态未知，请稍后重试");
                    }else{
                        //其他应答码为交易失败
                        //TODO

                        System.out.println("交易失败");
                    }
                }else if(("34").equals(rspData.get("respCode"))){
                    //订单不存在，可认为交易状态未明，需要稍后发起交易状态查询，或依据对账结果为准
                    System.out.println("订单不存在");

                }else{//查询交易本身失败，如应答码10/11检查查询报文是否正确
                    //TODO
                    System.out.println("查询失败");
                }
            }else{
                LogUtil.writeErrorLog("验证签名失败");
                //TODO 检查验证签名失败的原因

                System.out.println("验签失败");
            }
        }else{
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
            System.out.println("未获取到报文，或者http状态非200");
        }

        return rspData;
    }

    @Override
    public Map<String, String> rollBack(String orderId,String txnTime) {
        Map<String, String> data = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/

        data.put("version", SDKConstants.VERSION_5_1_0);    //版本号
        data.put("encoding", SDKConstants.UTF_8_ENCODING);  //字符集编码,可以使用Utf-8，GBK
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法

        data.put("txnType", "99");                           //交易类型 99-冲正
        data.put("txnSubType", "01");                        //交易子类型
        data.put("bizType", "000000");          		 	//填写000000
        data.put("channelType", "08");                       //渠道类型，07-PC，08-手机


        /***商户接入参数***/
        data.put("merId", "777290058180063");                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改
        data.put("orderId", orderId);        	 	       //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
        data.put("txnTime", txnTime);	   //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效


        // 请求方保留域，
        // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
        // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
        // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。

        //data.put("reqReserved", "透传信息1|透传信息2|透传信息3");
        // 2. 内容可能出现&={}[]"'符号时：
        // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
        // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
        //    注意控制数据长度，实际传输的数据长度不能超过1024位。
        //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
        //data.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));


        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

        Map<String, String> reqData  = AcpService.sign(data,DemoBase.encoding);		//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getBackRequestUrl();									//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData, url,DemoBase.encoding);//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过


        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》

        if(!rspData.isEmpty()){
            if(AcpService.validate(rspData, DemoBase.encoding)){
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode") ;
                if(("00").equals(respCode)){
                    //交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                    //TODO

                    System.out.println("交易已经受理，稍后请查询交易状态");
                }else if(("03").equals(respCode)||
                        ("04").equals(respCode)||
                        ("05").equals(respCode)){
                    //后续需发起交易状态查询交易确定交易状态
                    //TODO

                    System.out.println("稍后查询交易状态");
                }else{
                    //其他应答码为失败请排查原因
                    //TODO

                    System.out.println("冲正交易失败");
                }
            }else{
                LogUtil.writeErrorLog("验证签名失败");
                //TODO 检查验证签名失败的原因
            }
        }else{
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
        }

        Map<String,String> result=new HashMap<>();
        String reqMessage = DemoBase.genHtmlResult(reqData);
        String rspMessage = DemoBase.genHtmlResult(rspData);

        result.put("reqMessage",reqMessage);
        result.put("resMessage",rspMessage);

        return result;
    }

    @Override
    public Map<String, String> cancel(String orderId, String queryId) {
        Map<String, String> data = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", SDKConstants.VERSION_5_1_0);  //版本号
        data.put("encoding", DemoBase.encoding);          //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        data.put("txnType", "31");                        //交易类型 31-消费撤销
        data.put("txnSubType", "00");                     //交易子类型  默认00
        data.put("bizType", "000301");                    //业务类型
        data.put("channelType", "07");                    //渠道类型，07-PC，08-手机

        /***商户接入参数***/
        data.put("merId", "777290058180063");             //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                      //接入类型，商户接入固定填0，不需修改
        data.put("orderId", orderId);                    //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
        data.put("txnTime", DemoBase.getCurrentTime());    //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        data.put("txnAmt", "10000");                       //【撤销金额】，消费撤销时必须和原消费金额相同
        data.put("currencyCode", "156");                  //交易币种(境内商户一般是156 人民币)
        data.put("backUrl", DemoBase.backUrl);            //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费撤销交易 商户通知,其他说明同消费交易的商户通知


        /***要调通交易以下字段必须修改***/
        data.put("origQryId", queryId);                  //【原始交易流水号】，原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

        // 请求方保留域，
        // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
        // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
        // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
        //  data.put("reqReserved", "透传信息1|透传信息2|透传信息3");

        // 2. 内容可能出现&={}[]"'符号时：
        // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
        // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
        //    注意控制数据长度，实际传输的数据长度不能超过1024位。
        //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
        // data.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));


        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文**/
        Map<String, String> reqData  = AcpService.sign(data,DemoBase.encoding);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getBackRequestUrl();//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData, url,DemoBase.encoding);//发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》

        if(!rspData.isEmpty()){
            if(AcpService.validate(rspData, DemoBase.encoding)){
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode") ;
                if(("00").equals(respCode)){
                    //交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                    //TODO

                    System.out.println("撤销成功");
                }else if(("03").equals(respCode)||
                        ("04").equals(respCode)||
                        ("05").equals(respCode)){
                    //后续需发起交易状态查询交易确定交易状态
                    //TODO

                    System.out.println("交易状态不明，请稍后查询");
                }else{
                    //其他应答码为失败请排查原因
                    //TODO

                    System.out.println("撤销交易失败");
                }
            }else{
                LogUtil.writeErrorLog("验证签名失败");
                //TODO 检查验证签名失败的原因

                System.out.println("验证签名失败");
            }
        }else{
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");

            System.out.println("未收到报文");
        }

        Map<String,String> result=new HashMap<>();
        String reqMessage = DemoBase.genHtmlResult(reqData);
        String rspMessage = DemoBase.genHtmlResult(rspData);

        result.put("reqMessage",reqMessage);
        result.put("resMessage",rspMessage);
        return result;
    }

    @Override
    public Map<String, String> refund(String orderId,String queryId) {
        Map<String, String> data = new HashMap<>();

        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", SDKConstants.VERSION_5_1_0);               //版本号
        data.put("encoding", SDKConstants.UTF_8_ENCODING);             //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", SDKConfig.getConfig().getSignMethod()); //签名方法
        data.put("txnType", "04");                           //交易类型 04-退货
        data.put("txnSubType", "00");                        //交易子类型  默认00
        data.put("bizType", "000301");                       //业务类型
        data.put("channelType", "07");                       //渠道类型，07-PC，08-手机


        /***商户接入参数***/
        data.put("merId", "777290058180063");                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改
        data.put("orderId", orderId);          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
        data.put("txnTime", DemoBase.getCurrentTime());      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）
        data.put("txnAmt", "4000");                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额
        data.put("backUrl", SDKConfig.getConfig().getBackUrl());       //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知


        /***要调通交易以下字段必须修改***/
        data.put("origQryId", queryId);      //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

        // 请求方保留域，
        // 透传字段，查询、通知、对账文件中均会原样出现，如有需要请启用并修改自己希望透传的数据。
        // 出现部分特殊字符时可能影响解析，请按下面建议的方式填写：
        // 1. 如果能确定内容不会出现&={}[]"'等符号时，可以直接填写数据，建议的方法如下。
        //  data.put("reqReserved", "透传信息1|透传信息2|透传信息3");

        // 2. 内容可能出现&={}[]"'符号时：
        // 1) 如果需要对账文件里能显示，可将字符替换成全角＆＝｛｝【】“‘字符（自己写代码，此处不演示）；
        // 2) 如果对账文件没有显示要求，可做一下base64（如下）。
        //    注意控制数据长度，实际传输的数据长度不能超过1024位。
        //    查询、通知等接口解析时使用new String(Base64.decodeBase64(reqReserved), DemoBase.encoding);解base64后再对数据做后续解析。
        //   data.put("reqReserved", Base64.encodeBase64String("任意格式的信息都可以".toString().getBytes(DemoBase.encoding)));



        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
        Map<String, String> reqData  = AcpService.sign(data,SDKConstants.UTF_8_ENCODING);   //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getBackRequestUrl();                                                           //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData, url,DemoBase.encoding);//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过


        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if(!rspData.isEmpty()){
            if(AcpService.validate(rspData, DemoBase.encoding)){
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode") ;
                if(("00").equals(respCode)){
                    //交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                    //TODO

                    System.out.println("退款成功");

                }else if(("03").equals(respCode)||
                        ("04").equals(respCode)||
                        ("05").equals(respCode)){
                    //后续需发起交易状态查询交易确定交易状态
                    //TODO

                    System.out.println("请稍后查询退款状态");
                }else{
                    //其他应答码为失败请排查原因
                    //TODO

                    System.out.println("退款失败");
                }
            }else{
                LogUtil.writeErrorLog("验证签名失败");
                //TODO 检查验证签名失败的原因

                System.out.println("验证签名失败");
            }
        }else{
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");

            System.out.println("未收到报文");
        }

        return rspData;
    }
}
