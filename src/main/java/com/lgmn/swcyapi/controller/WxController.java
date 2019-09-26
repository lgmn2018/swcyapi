package com.lgmn.swcyapi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.swcyapi.dto.login.WxLoginDto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.nutz.lang.Lang;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.weixin.bean.WxPayUnifiedOrder;
import org.nutz.weixin.impl.WxApi2Impl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/wxApi")
public class WxController {

    // 微信登录
    @PostMapping("/wxLogin")
    public Result wxLogin(@RequestBody WxLoginDto wxLoginDto) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpResponse closeableHttpResponse;
        HttpGet httpGet = new HttpGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx6135b0ad2c35654c&secret=0ad25062707c8e928e590dbb57f27ea3&code=" + wxLoginDto.getCode() +"&grant_type=authorization_code");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
        httpGet.setConfig(requestConfig);
        try {
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
            //HttpCode
            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return Result.error(ResultEnum.SERVER_ERROR);
            }
            //此处处理状态码200
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            String isLogin = EntityUtils.toString(httpEntity, "UTF-8");
            JSONObject object = JSON.parseObject(isLogin);
            return Result.success(object);
        } catch (IOException e) {
            return Result.serverError(e.getMessage());
        }
    }

    // 统一下单
    @PostMapping("/unifiedOrder")
    public Result unifiedOrder(HttpServletRequest req) {
        String orderNo = "ZS" + System.currentTimeMillis();
        // 统一下单参数
        WxPayUnifiedOrder wxPayUnifiedOrder = new WxPayUnifiedOrder();
        wxPayUnifiedOrder.setAppid("wx6135b0ad2c35654c");                                       // APPID
        wxPayUnifiedOrder.setMch_id("1556355751");                                              // 商户号ID
        wxPayUnifiedOrder.setNonce_str(R.UU32());                                               // 随机字符串
        wxPayUnifiedOrder.setBody("测试支付");                                                   // 描述
        wxPayUnifiedOrder.setOut_trade_no(orderNo);                                             // 订单号
		wxPayUnifiedOrder.setTotal_fee(1);                                                      // 金额
//        wxPayUnifiedOrder.setTotal_fee(money);
        wxPayUnifiedOrder.setTrade_type("APP");                                                 // 交易类型
        wxPayUnifiedOrder.setNotify_url("http://www/baidu.com");                                // 回调URL
        wxPayUnifiedOrder.setSpbill_create_ip(Lang.getIP(req));                                 // 终端IP
        WxApi2Impl wxApi2Impl = new WxApi2Impl();
        NutMap nutMap = wxApi2Impl.pay_unifiedorder("sanweichuangyeapp201900000000000", wxPayUnifiedOrder);
        return Result.success(nutMap);
    }
}
