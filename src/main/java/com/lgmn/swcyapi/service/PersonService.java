package com.lgmn.swcyapi.service;

import com.alibaba.fastjson.JSONObject;
import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.common.result.ResultEnum;
import com.lgmn.juhe.starter.service.JuHe_IdCardQuery_SarterService;
import com.lgmn.qiniu.starter.service.QiNiu_UpLoad_Img_StarterService;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcy.basic.entity.SwcyAppUserEntity;
import com.lgmn.swcyapi.dto.login.LoginDto;
import com.lgmn.swcyapi.dto.person.AuthenticationDto;
import com.lgmn.swcyapi.dto.person.UpDatePasswordDto;
import com.lgmn.swcyapi.dto.person.UpdateMailboxDto;
import com.lgmn.swcyapi.service.ad.AdService;
import com.lgmn.swcyapi.service.appuser.AppUserService;
import com.lgmn.swcyapi.service.user.UserService;
import com.lgmn.swcyapi.vo.home.HomeAdVo;
import com.lgmn.swcyapi.vo.person.AuthenticationInfoVo;
import com.lgmn.swcyapi.vo.person.PersonAndAdVo;
import com.lgmn.swcyapi.vo.person.PersonInfoVo;
import com.lgmn.userservices.basic.entity.LgmnUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PersonService {

    static String regxPass = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,32}$";

    @Autowired
    AdService adService;

    @Autowired
    UserService userService;

    @Autowired
    AppUserService appUserService;

    @Autowired
    QiNiu_UpLoad_Img_StarterService qiNiu_upLoad_img_starterService;

    @Autowired
    JuHe_IdCardQuery_SarterService juHe_idCardQuery_sarterService;

    @Autowired
    LoginService loginService;

    public Result getPersonAndAdList(LgmnUserInfo lgmnUserInfo) throws Exception {
        List<SwcyAdEntity> swcyAdEntities = adService.getAdListByType(3);
        List<HomeAdVo> homeAdVos = new HomeAdVo().getVoList(swcyAdEntities, HomeAdVo.class);
        PersonAndAdVo personAndAdVo = new PersonAndAdVo();
        personAndAdVo.setHomeAdVo(homeAdVos);
        personAndAdVo.setLgmnUserInfo(lgmnUserInfo);
        return Result.success(personAndAdVo);
    }

    public Result upDateNikeName(LgmnUserInfo lgmnUserInfo, String nikeName) {
        LgmnUserEntity user = userService.getUserById(lgmnUserInfo.getId());
        user.setNikeName(nikeName);
        userService.save(user);
        return Result.success("修改成功");
    }

    public Result upDateAvatar (LgmnUserInfo lgmnUserInfo, MultipartFile avatar) throws IOException {
        List<String> path = new ArrayList<>();
        path.add("user");
        path.add("avatar");
        String kePath = qiNiu_upLoad_img_starterService.upLoadImg(avatar, path);
        LgmnUserEntity lgmnUserEntity = userService.getUserById(lgmnUserInfo.getId());
        lgmnUserEntity.setAvatar(kePath);
        userService.save(lgmnUserEntity);
        return Result.success("修改成功");
    }

    public Result upDateEmail (LgmnUserInfo lgmnUserInfo, UpdateMailboxDto updateMailboxDto) throws Exception {
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        swcyAppUserEntity.setEmail(updateMailboxDto.getEmail());
        appUserService.save(swcyAppUserEntity);
        return Result.success("修改成功");
    }

    public Result getPersonInfo (LgmnUserInfo lgmnUserInfo) throws Exception {
        PersonInfoVo personInfoVo = new PersonInfoVo();
        personInfoVo.setId(lgmnUserInfo.getId());
        personInfoVo.setAvatar(lgmnUserInfo.getAvatar());
        personInfoVo.setNikeName(lgmnUserInfo.getNikeName());
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        personInfoVo.setAuthentication(StringUtils.isEmpty(swcyAppUserEntity.getName()));
        personInfoVo.setEmail(swcyAppUserEntity.getEmail());
        return Result.success(personInfoVo);
    }

    public Result authentication (LgmnUserInfo lgmnUserInfo, AuthenticationDto authenticationDto) throws Exception {
        JSONObject jsonObject = juHe_idCardQuery_sarterService.postJuHeIdcard(authenticationDto.getIdNum(), authenticationDto.getName());
        if (Integer.parseInt(jsonObject.get("error_code").toString()) != 0) {
            return Result.error(ResultEnum.NOT_SCHEDULED_ERROR);
        }
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        swcyAppUserEntity.setName(authenticationDto.getName());
        swcyAppUserEntity.setIdNum(authenticationDto.getIdNum());
        appUserService.save(swcyAppUserEntity);
        return Result.success("认证成功");
    }

    public Result getAuthenticationInfo (LgmnUserInfo lgmnUserInfo) throws Exception {
        SwcyAppUserEntity swcyAppUserEntity = appUserService.getAppUserByUid(lgmnUserInfo.getId());
        if (StringUtils.isEmpty(swcyAppUserEntity.getName())) {
            return Result.error(ResultEnum.DATA_NOT_EXISTS);
        }
        AuthenticationInfoVo authenticationInfoVo = new AuthenticationInfoVo();
        authenticationInfoVo.setPhone(lgmnUserInfo.getAccount());
        StringBuffer sb = new StringBuffer(swcyAppUserEntity.getIdNum());
        sb.replace(6, 13, "********");
        authenticationInfoVo.setIdNum(sb.toString());
        authenticationInfoVo.setName(swcyAppUserEntity.getName());
        return Result.success(authenticationInfoVo);
    }

    public Result upDatePassword (LgmnUserInfo lgmnUserInfo, UpDatePasswordDto upDatePasswordDto) {
        if (!Pattern.matches(regxPass, upDatePasswordDto.getNewPassword()) || !upDatePasswordDto.getNewPassword().equals(upDatePasswordDto.getConfirmPassword())) return Result.error(ResultEnum.PASS_ERROR);
        LoginDto loginDto = new LoginDto();
        loginDto.setPhone(lgmnUserInfo.getAccount());
        loginDto.setPassword(upDatePasswordDto.getPassword());
        JSONObject responseResult = loginService.restTemplateLogin(loginDto);
        if (responseResult.containsKey("error_description")) {
            return Result.error(ResultEnum.PASS_ERROR);
        }
        LgmnUserEntity lgmnUserEntity = userService.getUserById(lgmnUserInfo.getId());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        lgmnUserEntity.setPassword(bCryptPasswordEncoder.encode(upDatePasswordDto.getNewPassword()));
        userService.save(lgmnUserEntity);
        return Result.success("修改成功");
    }
}
