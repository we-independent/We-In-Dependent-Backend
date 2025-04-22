package com.weindependent.app.components;

import cn.dev33.satoken.stp.StpInterface;
import com.weindependent.app.database.mapper.weindependent.PermissionMapper;
import com.weindependent.app.database.mapper.weindependent.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限验证接口
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    PermissionMapper permissionMapper;


    /**
     * 返回一个账号所拥有的权限码集合
     * @return list of t_sys_permission.code
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return permissionMapper.getPermissionListByLoginId((String) loginId);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     * @return list of t_sys_role.role_code
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return roleMapper.getRoleListByLoginId((String) loginId);
    }
}