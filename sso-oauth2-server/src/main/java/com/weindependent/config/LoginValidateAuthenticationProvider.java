package com.weindependent.config;

import com.alibaba.fastjson.JSON;
import com.weindependent.domain.SysUserDto;
import com.weindependent.entity.SysPermission;
import com.weindependent.entity.SysUser;
import com.weindependent.repository.SysUserRepository;
import com.weindependent.service.PermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author zetor
 */
@Component
public class LoginValidateAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SysUserRepository sysUserRepository;

    private static final String SPLIT_STR = "&&&";

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取输入的用户名
        String username = authentication.getName();
        //获取输入的明文
        String rawPassword = (String) authentication.getCredentials();
        if (StringUtils.isBlank(username)) {
            throw new BadCredentialsException("用户名不能为空");
        }
        if (StringUtils.isBlank(rawPassword)) {
            throw new BadCredentialsException("密码不能为空");
        }
        if (username.indexOf(SPLIT_STR) == -1) {
            throw new BadCredentialsException("账套不能为空");
        }
        String[] usernameSob = StringUtils.split(
                username, SPLIT_STR);
        if (StringUtils.isBlank(usernameSob[1])) {
            throw new BadCredentialsException("账套不能为空.");
        }
        username = usernameSob[0];

        //查询用户是否存在
        SysUser sysUser = sysUserRepository.qryUser(username, Integer.parseInt(usernameSob[1]));
        if (null == sysUser) {
            throw new BadCredentialsException("用户不存在");
        }
        SysUserDto sysUserDto = new SysUserDto();
        sysUserDto.setUsername(sysUser.getUsername());
        sysUserDto.setRealName(sysUser.getRealName());
        sysUserDto.setSob(usernameSob[1]);
        sysUserDto.setStatus(sysUser.getIsDelete());
//        String userJson = JSON.toJSONString(sysUserDto);
        //验证密码
//        if (!Md5Util.isMatchPassword(rawPassword, sysUser.getPassword())) {
//            throw new BadCredentialsException("输入密码错误");
//        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<SysPermission> list = permissionService.findByAccount(sysUser.getUsername());
        List<String> collect = list.stream().map(SysPermission::getCode).collect(Collectors.toList());
        for (String authority : collect) {
            if (!("").equals(authority) & authority != null) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
                grantedAuthorities.add(grantedAuthority);
            }
        }
        return new UsernamePasswordAuthenticationToken(sysUserDto, rawPassword, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //确保authentication能转成该类
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
