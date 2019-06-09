package com.lgmn.swcyapi.vo.person;

import com.lgmn.common.domain.LgmnVo;
import com.lgmn.swcy.basic.entity.SwcyMessageEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class MessageVo extends LgmnVo<SwcyMessageEntity, MessageVo> {
    private Integer id;
    private String title;
    private String content;
    private Integer readingVolume;
    private Timestamp createTime;
    private Integer hadRead;
}
