package com.jingdianjichi.circle.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@Data
public class AuthorVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String avatar;

    private Long id;

    private String userName;

    private String nickName;

}
