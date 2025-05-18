package com.jingdianjichi.circle.server.rpc;

import com.jingdianjichi.auth.api.UserFeignService;
import com.jingdianjichi.auth.entity.AuthUserDTO;
import com.jingdianjichi.auth.entity.Result;
import com.jingdianjichi.circle.server.entity.dto.UserInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class UserRpc {

    @Resource
    private UserFeignService userFeignService;

    public UserInfo getUserInfo(String userName) {
        AuthUserDTO authUserDTO = new AuthUserDTO();
        authUserDTO.setUserName(userName);
        Result<AuthUserDTO> result = userFeignService.getUserInfo(authUserDTO);
        UserInfo userInfo = new UserInfo();
        if (!result.getSuccess()) {
            return userInfo;
        }
        AuthUserDTO data = result.getData();

        userInfo.setId(data.getId());
        userInfo.setUserName(data.getUserName());
        userInfo.setNickName(data.getNickName());
        userInfo.setAvatar(data.getAvatar());
        return userInfo;
    }

    public UserInfo getUserById(Long id) {
        AuthUserDTO authUserDTO = new AuthUserDTO();
        authUserDTO.setId(id);
        Result<AuthUserDTO> result = userFeignService.getUserInfo(authUserDTO);
        UserInfo userInfo = new UserInfo();
        if (!result.getSuccess()) {
            return userInfo;
        }
        AuthUserDTO data = result.getData();

        userInfo.setId(data.getId());
        userInfo.setUserName(data.getUserName());
        userInfo.setNickName(data.getNickName());
        userInfo.setAvatar(data.getAvatar());
        return userInfo;
    }

    public Map<Long, UserInfo> batchGetUserInfoById(List<Long> userIdList) {
        if (CollectionUtils.isEmpty(userIdList)) {
            return Collections.emptyMap();
        }
        Result<List<AuthUserDTO>> listResult = userFeignService.listUserInfoByUserIds(userIdList);
        if (Objects.isNull(listResult) || !listResult.getSuccess() || Objects.isNull(listResult.getData())) {
            return Collections.emptyMap();
        }
        Map<Long, UserInfo> result = new HashMap<>();
        for (AuthUserDTO data : listResult.getData()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(data.getUserName());
            userInfo.setNickName(data.getNickName());
            userInfo.setAvatar(data.getAvatar());
            result.put(data.getId(), userInfo);
        }
        return result;
    }

    public Map<String, UserInfo> batchGetUserInfo(List<String> userNameList) {
        if (CollectionUtils.isEmpty(userNameList)) {
            return Collections.emptyMap();
        }
        Result<List<AuthUserDTO>> listResult = userFeignService.listUserInfoByIds(userNameList);
        if (Objects.isNull(listResult) || !listResult.getSuccess() || Objects.isNull(listResult.getData())) {
            return Collections.emptyMap();
        }
        Map<String, UserInfo> result = new HashMap<>();
        for (AuthUserDTO data : listResult.getData()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(data.getUserName());
            userInfo.setNickName(data.getNickName());
            userInfo.setAvatar(data.getAvatar());
            result.put(userInfo.getUserName(), userInfo);
        }
        return result;
    }


}
