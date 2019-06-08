package com.lgmn.swcyapi.service.appuser;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnUserInfo;
import com.lgmn.common.result.Result;
import com.lgmn.common.utils.ObjectTransfer;
import com.lgmn.swcy.basic.dto.SwcyAppUserDto;
import com.lgmn.swcy.basic.entity.SwcyAdEntity;
import com.lgmn.swcy.basic.entity.SwcyAppUserEntity;
import com.lgmn.swcy.basic.service.SwcyAppUserService;
import com.lgmn.swcyapi.service.ad.AdService;
import com.lgmn.swcyapi.vo.home.HomeAdVo;
import com.lgmn.swcyapi.vo.home.HomePageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppUserService {

    @Reference(version = "${demo.service.version}")
    private SwcyAppUserService swcyAppUserService;

    @Autowired
    AdService adService;

    public SwcyAppUserEntity save (SwcyAppUserEntity swcyAppUserEntity) {
        return swcyAppUserService.saveEntity(swcyAppUserEntity);
    }

    public SwcyAppUserEntity getAppUserById (String userId) throws Exception {
        SwcyAppUserDto swcyAppUserDto = new SwcyAppUserDto();
        swcyAppUserDto.setUid(userId);
        return swcyAppUserService.getListByDto(swcyAppUserDto).get(0);
    }

    public Result getHomePage (LgmnUserInfo lgmnUserInfo) throws Exception {
        HomePageVo homePageVo = new HomePageVo();
        SwcyAppUserEntity swcyAppUserEntity = getAppUserById(lgmnUserInfo.getId());
        List<SwcyAdEntity> homeAdType0 = adService.getAdListByType(0);
        List<HomeAdVo> homeAdType0Vo = new HomeAdVo().getVoList(homeAdType0, HomeAdVo.class);
        List<SwcyAdEntity> homeAdType1 = adService.getAdListByType(1);
        List<HomeAdVo> homeAdType1Vo = new HomeAdVo().getVoList(homeAdType1, HomeAdVo.class);
        homePageVo.setHomeAdType0Vos(homeAdType0Vo);
        homePageVo.setHomeAdType1Vos(homeAdType1Vo);
        ObjectTransfer.transValue(swcyAppUserEntity, homePageVo);
        ObjectTransfer.transValue(lgmnUserInfo, homePageVo);
        return Result.success(homePageVo);
    }

}
