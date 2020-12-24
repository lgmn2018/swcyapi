package com.lgmn.swcyapi.service;

import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.swcy.basic.entity.SwcyAppUserEntity;
import com.lgmn.swcyapi.dto.login.MpRegisterDto;
import com.lgmn.swcyapi.service.appuser.AppUserService;
import com.lgmn.swcyapi.service.user.UserService;
import com.lgmn.userservices.basic.entity.LgmnUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Component
@Slf4j
public class MpUserService {

    @Autowired
    UserService userService;

    @Autowired
    AppUserService appUserService;

    public Result register (MpRegisterDto registerDto) throws Exception {
        String account = registerDto.getAccount();
        String password = registerDto.getPassword();

        List<LgmnUserEntity> lgmnUserEntities = userService.getUserByPhone(account);
        LgmnUserEntity lgmnUserEntity = null;
        String msg = "注册成功";
        if (lgmnUserEntities.size() > 0) {
            if (registerDto.getType() == 0) {
                return Result.error(ResultEnum.DATA_EXISTS);
            }
        } else {
            lgmnUserEntity = getUser(account, password,registerDto.getAvatar(),registerDto.getNikeName());
        }

        Integer gender=0;

        switch (registerDto.getGender()){
            case 0:
            case 1:
                gender=1;
                break;
            case 2:
                gender=0;
                break;
        }

        // 保存用户
        saveUser(lgmnUserEntity, registerDto.getPuid(), registerDto.getType(),gender);
        log.info("\n\n注册用户成功.info....\n\n");
        log.error("\n\n注册用户成功.error....\n\n");
        return Result.success(msg);
    }

    public boolean hadReg(String unionid) throws Exception {
        List<LgmnUserEntity> lgmnUserEntities = userService.getUserByPhone(unionid);
        if (lgmnUserEntities.size() > 0) {
          return true;
        } else {
           return false;
        }
    }

    private void saveUser (LgmnUserEntity lgmnUserEntity, String puid, Integer type, Integer gender) {
        LgmnUserEntity userEntity = userService.save(lgmnUserEntity);
        if (type == 0) {
            SwcyAppUserEntity swcyAppUserEntity = getAppUser(userEntity.getId(), puid);
            swcyAppUserEntity.setPersonalCommission(new BigDecimal(0));
            swcyAppUserEntity.setTeamCommission(new BigDecimal(0));
            swcyAppUserEntity.setConsumptionAmount(new BigDecimal(0));
            swcyAppUserEntity.setGender(gender);
            appUserService.save(swcyAppUserEntity);
        }
    }

    private LgmnUserEntity getUser (String phone, String password,String avatar, String nikeName) {
        LgmnUserEntity lgmnUserEntity = new LgmnUserEntity();
        lgmnUserEntity.setAvatar(avatar);
        lgmnUserEntity.setAccount(phone);
        lgmnUserEntity.setNikeName(nikeName);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        lgmnUserEntity.setPassword(bCryptPasswordEncoder.encode(password));
        lgmnUserEntity.setSalt("123456");
        lgmnUserEntity.setUserType(0);
        lgmnUserEntity.setRegTime(new Timestamp(System.currentTimeMillis()));
        return lgmnUserEntity;
    }

    private SwcyAppUserEntity getAppUser (String uid, String puid) {
        SwcyAppUserEntity swcyAppUserEntity = new SwcyAppUserEntity();
        swcyAppUserEntity.setUid(uid);
        swcyAppUserEntity.setPuid(puid);
        swcyAppUserEntity.setGroupPower(new BigDecimal(0));
        swcyAppUserEntity.setPersonPower(new BigDecimal(0));
        swcyAppUserEntity.setScore(0);
        swcyAppUserEntity.setUserType(0);
        swcyAppUserEntity.setStar(0);
        return swcyAppUserEntity;
    }

}