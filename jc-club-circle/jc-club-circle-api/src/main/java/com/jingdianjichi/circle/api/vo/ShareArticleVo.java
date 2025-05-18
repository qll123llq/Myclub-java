package com.jingdianjichi.circle.api.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ShareArticleVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String title;
    private String avatar;
    private String summary;
    private Long circleId;

    private String userName;
    private String userAvatar;


    private String content;

    private AuthorVo authorVo;

    private Date updateTime;
}
