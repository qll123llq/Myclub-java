package com.jingdianjichi.auth.domain.service;


import com.jingdianjichi.auth.domain.entity.AuthUserBO;

import java.util.List;

/**
 * 用户领域service
 * 
 * @author: ChickenWing
 * @date: 2023/11/1
 */
public interface AuthUserDomainService {

    /**
     * 注册
     */
    Boolean register(AuthUserBO authUserBO);

    /**
     * 更新用户信息
     */
    Boolean update(AuthUserBO authUserBO);

    /**
     * 更新用户信息
     */
    Boolean delete(AuthUserBO authUserBO);

}
