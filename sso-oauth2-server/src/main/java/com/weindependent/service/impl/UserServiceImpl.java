package com.weindependent.service.impl;

import com.weindependent.entity.SysSob;
import com.weindependent.entity.SysUser;
import com.weindependent.repository.SysSobRepository;
import com.weindependent.repository.SysUserRepository;
import com.weindependent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zetor
 * @date 2019-02-12
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysSobRepository sysSobRepository;

    @Override
    public SysUser getByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }

    @Override
    public List<SysSob> getSobs() {
        return sysSobRepository.findSobs();
    }
}
