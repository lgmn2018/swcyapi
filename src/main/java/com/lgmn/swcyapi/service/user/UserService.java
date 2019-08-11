package com.lgmn.swcyapi.service.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.userservices.basic.dto.LgmnUserDto;
import com.lgmn.userservices.basic.entity.LgmnUserEntity;
import com.lgmn.userservices.basic.service.LgmnUserService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserService {

    @Reference(version = "${demo.service.version}")
    private LgmnUserService lgmnUserEntityService;

    public List<LgmnUserEntity> getUserByPhone (String phone) throws Exception {
        LgmnUserDto lgmnUserDto = new LgmnUserDto();
        lgmnUserDto.setAccount(phone);
        return lgmnUserEntityService.getListByDto(lgmnUserDto);
    }

    public LgmnUserEntity save (LgmnUserEntity lgmnUserEntity) {
        return lgmnUserEntityService.saveEntity(lgmnUserEntity);
    }

    public LgmnUserEntity getUserById (String id) {
        return lgmnUserEntityService.findById(id);
    }
}
