package com.lgmn.swcyapi.service;

import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.qiniu.starter.service.QiNiu_UpLoad_Img_StarterService;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcy.basic.entity.SwcyAppUserEntity;
import com.lgmn.swcyapi.dto.person.UpdateMailboxDto;
import com.lgmn.swcyapi.service.ad.AdService;
import com.lgmn.swcyapi.service.appuser.AppUserService;
import com.lgmn.swcyapi.service.user.UserService;
import com.lgmn.swcyapi.vo.home.HomeAdVo;
import com.lgmn.swcyapi.vo.person.PersonAndAdVo;
import com.lgmn.userservices.basic.entity.LgmnUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonService {

    @Autowired
    AdService adService;

    @Autowired
    UserService userService;

    @Autowired
    AppUserService appUserService;

    @Autowired
    QiNiu_UpLoad_Img_StarterService qiNiu_upLoad_img_starterService;

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
}
