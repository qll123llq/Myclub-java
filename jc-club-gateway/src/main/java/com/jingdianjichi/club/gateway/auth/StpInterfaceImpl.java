package com.jingdianjichi.club.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 *
 * @author: ChickenWing
 * @date: 2023/10/28
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        // 1、直接跟数据库交互
        // 2、redis缓存
        // 3、redis缓存，没有的话，去调用我们的微服务去获取
        List<String> permissionList = new LinkedList<>();
        permissionList.add("subject:add");
        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = new LinkedList<>();
        roleList.add("admin");
        return roleList;
    }

}
