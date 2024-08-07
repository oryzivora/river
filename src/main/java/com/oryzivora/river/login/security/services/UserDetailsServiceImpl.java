package com.oryzivora.river.login.security.services;

import com.oryzivora.river.login.dao.SysRoleMapper;
import com.oryzivora.river.login.dao.SysUserMapper;
import com.oryzivora.river.login.model.SysRole;
import com.oryzivora.river.login.model.SysUser;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Resource
  SysUserMapper sysUserMapper;
  @Resource
  SysRoleMapper sysRoleMapper;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SysUser user = sysUserMapper.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    List<SysRole> sysRoles = sysRoleMapper.selectRoleListByUserId(user.getId());
    user.setRoles(sysRoles);
    return UserDetailsImpl.build(user);
  }

}
