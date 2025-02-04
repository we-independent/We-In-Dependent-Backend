package com.weindependent.service;

import com.weindependent.entity.SysSob;
import com.weindependent.entity.SysUser;

import java.util.List;

/**
 * @author zetor
 * @date 2019-02-12
 */
public interface UserService {

    SysUser getByUsername(String username);

    List<SysSob> getSobs();
}
