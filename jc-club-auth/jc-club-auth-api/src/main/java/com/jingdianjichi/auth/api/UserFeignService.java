package com.jingdianjichi.auth.api;

import com.jingdianjichi.auth.entity.AuthUserDTO;
import com.jingdianjichi.auth.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 用户服务feign
 *
 * @author: ChickenWing
 * @date: 2023/12/3
 */
@FeignClient("jc-club-auth-dev")
public interface UserFeignService {

    @RequestMapping("/user/getUserInfo")
    Result<AuthUserDTO> getUserInfo(@RequestBody AuthUserDTO authUserDTO);

    @RequestMapping("/user/listByIds")
    Result<List<AuthUserDTO>> listUserInfoByIds(@RequestBody List<String> userNameList);

    @RequestMapping("/user/listByUserIds")
    Result<List<AuthUserDTO>> listUserInfoByUserIds(@RequestBody List<Long> userIdList);
}
