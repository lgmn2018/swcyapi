package com.lgmn.swcyapi.handler;

import com.lgmn.swcyapi.builder.TextBuilder;
import com.lgmn.swcyapi.dto.login.MpRegisterDto;
import com.lgmn.swcyapi.service.MpUserService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class SubscribeHandler extends AbstractHandler {

    @Autowired
    private MpUserService mpUserService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        String openid = wxMessage.getFromUser();
        this.logger.info("新关注用户 OPENID: " + openid);
        try {
            // 获取微信用户基本信息
            WxMpUser userWxInfo = weixinService.getUserService()
                .userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
//                this.logger.info("user_info"+userWxInfo);
                // TODO 可以添加关注用户到本地数据库
                //获取推荐人unionid
                String recommender = userWxInfo.getQrSceneStr();
                String unionid = userWxInfo.getUnionId();
                MpRegisterDto registerDto = new MpRegisterDto();
                registerDto.setAccount(unionid);
                registerDto.setPassword(unionid);
                registerDto.setNikeName(userWxInfo.getNickname());
                registerDto.setGender(userWxInfo.getSex());
                registerDto.setPuid(recommender);
                registerDto.setType(0);
                registerDto.setAvatar(userWxInfo.getHeadImgUrl());
                mpUserService.register(registerDto);
            }
        } catch (WxErrorException e) {
            if (e.getError().getErrorCode() == 48001) {
                this.logger.info("该公众号没有获取用户信息权限！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("注册账号错误");
        }


        WxMpXmlOutMessage responseResult = null;
        try {
            responseResult = this.handleSpecial(wxMessage);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        if (responseResult != null) {
            return responseResult;
        }

        try {
            return new TextBuilder().build("感谢关注", wxMessage, weixinService);
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage)
        throws Exception {
        //TODO
        return null;
    }

}
