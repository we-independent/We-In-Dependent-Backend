package com.weindependent.service;

import com.weindependent.entity.SysPermission;

import java.util.List;

/**
 * @author zetor
 * @date 2019-02-12
 */
public interface PermissionService {

    List<SysPermission> findByUserId(Integer userId);

    List<SysPermission> findByAccount(String account);

}
