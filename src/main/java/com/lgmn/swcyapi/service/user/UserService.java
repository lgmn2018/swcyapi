package com.lgmn.swcyapi.service.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.basicservices.basic.entity.LgmnSmsCodeEntity;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.swcyapi.dto.login.RegisterDto;
import com.lgmn.swcyapi.service.sms.SmsCodeService;
import com.lgmn.userservices.basic.dto.LgmnUserDto;
import com.lgmn.userservices.basic.entity.LgmnUserEntity;
import com.lgmn.userservices.basic.service.LgmnUserEntityService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserService {

    @Reference(version = "${demo.service.version}")
    private LgmnUserEntityService lgmnUserEntityService;

    @Autowired
    SmsCodeService smsCodeService;

    static String regxPhone = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
    static String regxPass = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,32}$";


    public Result register (RegisterDto registerDto) throws Exception {
        String phone = registerDto.getPhone();
        String password = registerDto.getPassword();
        String confirmPassword = registerDto.getConfirmPassword();
        if (!Pattern.matches(regxPhone, phone)) return Result.error(ResultEnum.PHONE_ERROR);
        if (!Pattern.matches(regxPass, password) || !password.equals(confirmPassword)) return Result.error(ResultEnum.PASS_ERROR);

        List<LgmnSmsCodeEntity> lgmnSmsCodeEntities = smsCodeService.getByPhone(phone);
        if (lgmnSmsCodeEntities.size() < 0) return Result.error(ResultEnum.MSG_CODE_ERROR);
        LgmnSmsCodeEntity lgmnSmsCodeEntity = lgmnSmsCodeEntities.get(0);
        if (lgmnSmsCodeEntity.getCode().equals(registerDto.getSmsCode()) && lgmnSmsCodeEntity.getIsExprie() == 1 || !new Timestamp(System.currentTimeMillis()).before(lgmnSmsCodeEntities.get(0).getExpireTime())) return Result.error(ResultEnum.MSG_CODE_ERROR);

        List<LgmnUserEntity> lgmnUserEntities = getUserByPhone(phone);
        if (lgmnUserEntities.size() > 0) return Result.error(ResultEnum.DATA_EXISTS);

        // 修改验证码状态并保存用户
        lgmnSmsCodeEntity.setIsExprie(1);
        LgmnUserEntity lgmnUserEntity = getUser(phone, password);
        saveUserAndUpdateSmsCode(lgmnUserEntity, lgmnSmsCodeEntity);

        return Result.success("注册成功");
    }

    public List<LgmnUserEntity> getUserByPhone (String phone) throws Exception {
        LgmnUserDto lgmnUserDto = new LgmnUserDto();
        lgmnUserDto.setAccount(phone);
        return lgmnUserEntityService.getListByDto(lgmnUserDto);
    }

    private LgmnUserEntity getUser (String phone, String password) {
        LgmnUserEntity lgmnUserEntity = new LgmnUserEntity();
        lgmnUserEntity.setAvatar("http://qncdn.gdsdec.com/default/avatar/man.png");
        lgmnUserEntity.setAccount(phone);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        lgmnUserEntity.setPassword(bCryptPasswordEncoder.encode(password));
        lgmnUserEntity.setSalt("123456");
        lgmnUserEntity.setUserType(0);
        lgmnUserEntity.setRegTime(new Timestamp(System.currentTimeMillis()));
        return lgmnUserEntity;
    }

    @GlobalTransactional
    private void saveUserAndUpdateSmsCode (LgmnUserEntity lgmnUserEntity, LgmnSmsCodeEntity lgmnSmsCodeEntity) {
        smsCodeService.saveBySmsCode(lgmnSmsCodeEntity);
        lgmnUserEntityService.saveEntity(lgmnUserEntity);
    }

}
