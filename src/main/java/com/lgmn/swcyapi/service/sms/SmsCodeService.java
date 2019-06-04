package com.lgmn.swcyapi.service.sms;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.lgmn.basicservices.basic.dto.LgmnSmsCodeDto;
import com.lgmn.basicservices.basic.entity.LgmnSmsCodeEntity;
import com.lgmn.basicservices.basic.service.LgmnSmsCodeService;
import com.lgmn.common.domain.LgmnOrder;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.swcyapi.sms.SmsKit;
import com.lgmn.yzx.starter.model.SendSmsEntity;
import com.lgmn.yzx.starter.service.Yzx_SMS_StarterService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class SmsCodeService {

    static String regxPhone = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    @Reference(version = "${demo.service.version}")
    private LgmnSmsCodeService lgmnSmsCodeService;

    @Autowired
    private Yzx_SMS_StarterService yzx_sms_starterService;

    public Result sendSmsCode (String phone) throws Exception {
        if(!Pattern.matches(regxPhone, phone)) {
            return Result.error(ResultEnum.PHONE_ERROR);
        }
        List<LgmnSmsCodeEntity> lgmnSmsCodeEntities = getByPhone(phone);
        if (lgmnSmsCodeEntities.size() > 0 && new Timestamp(System.currentTimeMillis()).before(lgmnSmsCodeEntities.get(0).getExpireTime())) {
            return Result.error(ResultEnum.OPERATION_FREQUENTLY_ERROR);
        }

        String smsCode = SmsKit.randomSMSCode(6);
        SendSmsEntity sendSmsEntity = new SendSmsEntity();
        sendSmsEntity.setMobile(phone);
        sendSmsEntity.setParam(smsCode);
        JSONObject jsonObject = yzx_sms_starterService.sendSms(sendSmsEntity);
        String code = jsonObject.get("code").toString();
        if ("000000".equals(code)) {
            save(phone, smsCode);
            return Result.success("发送成功");
        }
        return Result.serverError("发送失败");
    }

    public LgmnSmsCodeEntity save (String phone, String smsCode) {
        long thisTime = System.currentTimeMillis();
        LgmnSmsCodeEntity lgmnSmsCodeEntity = new LgmnSmsCodeEntity();
        lgmnSmsCodeEntity.setCode(smsCode);
        lgmnSmsCodeEntity.setPhone(phone);
        lgmnSmsCodeEntity.setSendTime(new Timestamp(thisTime));
        lgmnSmsCodeEntity.setExpireTime(new Timestamp(thisTime + ((10 * 60) * 1000)));
        return lgmnSmsCodeService.saveEntity(lgmnSmsCodeEntity);
    }

    public LgmnSmsCodeEntity saveBySmsCode (LgmnSmsCodeEntity lgmnSmsCodeEntity) {
        return lgmnSmsCodeService.saveEntity(lgmnSmsCodeEntity);
    }

    public List<LgmnSmsCodeEntity> getByPhone (String phone) throws Exception {
        LgmnSmsCodeDto lgmnSmsCodeDto = new LgmnSmsCodeDto();
        lgmnSmsCodeDto.setPhone(phone);
        LgmnOrder order=new LgmnOrder(Sort.Direction.DESC,"sendTime");
        List<LgmnOrder> orders = new ArrayList<>();
        orders.add(order);
        lgmnSmsCodeDto.setOrders(orders);
        return lgmnSmsCodeService.getListByDto(lgmnSmsCodeDto);
    }

}
