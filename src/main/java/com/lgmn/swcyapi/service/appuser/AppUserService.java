package com.lgmn.swcyapi.service.appuser;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.common.domain.LgmnPage;
import com.lgmn.swcy.basic.dto.SwcyAppUserDto;
import com.lgmn.swcy.basic.entity.SwcyAppUserEntity;
import com.lgmn.swcy.basic.service.SwcyAppUserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppUserService {

    @Reference(version = "${demo.service.version}")
    private SwcyAppUserService swcyAppUserService;

    public SwcyAppUserEntity save (SwcyAppUserEntity swcyAppUserEntity) {
        return swcyAppUserService.saveEntity(swcyAppUserEntity);
    }

    public SwcyAppUserEntity getAppUserByUid (String userId) throws Exception {
        SwcyAppUserDto swcyAppUserDto = new SwcyAppUserDto();
        swcyAppUserDto.setUid(userId);
        return swcyAppUserService.getListByDto(swcyAppUserDto).get(0);
    }

    public List<SwcyAppUserEntity> getAppUserListByPuid (String puid) throws Exception {
//        SwcyAppUserDto swcyAppUserDto = new SwcyAppUserDto();
//        swcyAppUserDto.setPuid(puid);
//        return swcyAppUserService.getListByDto(swcyAppUserDto);
        return swcyAppUserService.getUnderUsers(puid);
    }

    public LgmnPage<SwcyAppUserEntity> getAppUserPageByPuid (String puid, Integer pageNumber, Integer pageSize) throws Exception {
        SwcyAppUserDto swcyAppUserDto = new SwcyAppUserDto();
        swcyAppUserDto.setPuid(puid);
        swcyAppUserDto.setPageNumber(pageNumber);
        swcyAppUserDto.setPageSize(pageSize);
        return swcyAppUserService.getPageByDtoWithPageRequet(swcyAppUserDto);
    }

    public List<SwcyAppUserEntity> getAppUserByPhone(String phone) throws Exception {
        SwcyAppUserDto swcyAppUserDto = new SwcyAppUserDto();
        swcyAppUserDto.setPhone(phone);
        return swcyAppUserService.getListByDto(swcyAppUserDto);
    }
}
