package com.lgmn.swcyapi.service.follow;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lgmn.swcy.basic.dto.SwcyFollowDto;
import com.lgmn.swcy.basic.entity.SwcyFollowEntity;
import com.lgmn.swcy.basic.service.SwcyFollowService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SwcyFollowApiService {
    @Reference(version = "${demo.service.version}")
    private SwcyFollowService swcyFollowService;

    public boolean isFollow(String userId, Integer storeId) throws Exception {
        SwcyFollowDto swcyFollowDto = getSwcyFollowDto(userId, storeId);
        List<SwcyFollowEntity> list = swcyFollowService.getListByDto(swcyFollowDto);
        if (list.size() <= 0) {
            return false;
        }
        return true;
    }

    public boolean followAndCancelFollow(String userId, Integer storeId) throws Exception {
        SwcyFollowDto swcyFollowDto = getSwcyFollowDto(userId, storeId);
        List<SwcyFollowEntity> list = swcyFollowService.getListByDto(swcyFollowDto);
        if (list.size() <= 0) {
            SwcyFollowEntity swcyFollowEntity = new SwcyFollowEntity();
            swcyFollowEntity.setUid(userId);
            swcyFollowEntity.setStoreId(storeId);
            swcyFollowService.saveEntity(swcyFollowEntity);
            return true;
        }
        SwcyFollowEntity swcyFollowEntity = list.get(0);
        swcyFollowService.deleteByEntity(swcyFollowEntity);
        return false;
    }

    private SwcyFollowDto getSwcyFollowDto(String userId, Integer storeId) {
        SwcyFollowDto swcyFollowDto = new SwcyFollowDto();
        swcyFollowDto.setUid(userId);
        swcyFollowDto.setStoreId(storeId);
        return swcyFollowDto;
    }
}
