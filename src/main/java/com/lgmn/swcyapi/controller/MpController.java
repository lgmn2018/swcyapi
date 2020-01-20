package com.lgmn.swcyapi.controller;

import com.lgmn.common.result.Result;
import com.lgmn.swcyapi.dto.login.LoginDto;
import com.lgmn.swcyapi.dto.login.MpRegisterDto;
import com.lgmn.swcyapi.dto.login.WxLoginDto;
import com.lgmn.swcyapi.dto.mp.CommendQrcodeDto;
import com.lgmn.swcyapi.service.LoginService;
import com.lgmn.swcyapi.service.MpUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/mp")
public class MpController {

//    @Value("${wx.mp.appid}")
//    private String appid;
//
//    @Value("${wx.mp.secret}")
//    private String appSecret;

    @Value("${wx.mp.redirect-uri}")
    private String redirectUri;

    @Autowired
    WxMpService wxMpService;
    @Autowired
    WxMpMessageRouter messageRouter;

    @Autowired
    MpUserService mpUserService;

    @Autowired
    LoginService loginService;

    @GetMapping("/oauth")
    public void login(HttpServletResponse response) throws IOException {
        String qrUrl = wxMpService.oauth2buildAuthorizationUrl(redirectUri,"snsapi_userinfo",AuthStateUtils.createState());
        response.sendRedirect(qrUrl);
    }

    @PostMapping(produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("openid") String openid,
                       @RequestParam(name = "encrypt_type", required = false) String encType,
                       @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        log.info("\n接收微信请求：[openid=[{}], [signature=[{}], encType=[{}], msgSignature=[{}],"
                        + " timestamp=[{}], nonce=[{}], requestBody=[\n{}\n] ",
                openid, signature, encType, msgSignature, timestamp, nonce, requestBody);

        String appid = "wxc16c319d5aee2286";
        if (!this.wxMpService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }

        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toXml();
        } else if ("aes".equalsIgnoreCase(encType)) {
            // aes加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxMpService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.route(inMessage);
            if (outMessage == null) {
                return "";
            }

            out = outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage());
        }

        log.debug("\n组装回复信息：{}", out);
        return out;
    }

    private WxMpXmlOutMessage route(WxMpXmlMessage message) {
        try {
            return this.messageRouter.route(message);
        } catch (Exception e) {
            log.error("路由消息时出现异常！", e);
        }

        return null;
    }

    @PostMapping("/applogin")
    public Result applogin(@RequestBody WxLoginDto wxLoginDto){
        Result result = Result.serverError("服务端错误");
        WxMpUser user;
        LoginDto loginDto = new LoginDto();
        String appid = "wx6135b0ad2c35654c";
        if (!this.wxMpService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }
        WxMpConfigStorage configStorage = wxMpService.getWxMpConfigStorage();
        try {
            WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(wxLoginDto.getCode());
            user = wxMpService.oauth2getUserInfo(accessToken, null);
            String unionid = user.getUnionId();
            loginDto.setPhone(unionid);
            loginDto.setPassword(unionid);
            if(mpUserService.hadReg(user.getUnionId())){
                result = loginService.login(loginDto);
            }else{
                MpRegisterDto registerDto = new MpRegisterDto();
                registerDto.setAccount(unionid);
                registerDto.setNikeName(user.getNickname());
                registerDto.setPassword(unionid);
                registerDto.setPuid("");
                registerDto.setType(0);
                registerDto.setAvatar(user.getHeadImgUrl());
                registerDto.setGender(user.getSex());
                result = mpUserService.register(registerDto);
                if(result.getCode().equals("200")) {
                    result = loginService.login(loginDto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Result.serverError(e.getMessage());
        }finally {
            return result;
        }
    }


   @PostMapping("/commendQrcode")
   public String genCommendQrcode(@RequestBody CommendQrcodeDto commendQrcodeDto){
        String qrcodeUrl = "";
       try {
           String appid = "wxc16c319d5aee2286";
           if (!this.wxMpService.switchover(appid)) {
               throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
           }
           WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(commendQrcodeDto.getUid());
           qrcodeUrl = wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket(),true);
       } catch (WxErrorException e) {
           e.printStackTrace();
       }
       return qrcodeUrl;
   }

    @GetMapping(produces = "text/plain;charset=utf-8")
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

        log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature,
                timestamp, nonce, echostr);
        String token = wxMpService.getWxMpConfigStorage().getToken();
        log.info("token:{}",token);
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }
}