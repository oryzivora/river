package com.oryzivora.river.login.service.impl;

import com.oryzivora.river.login.models.SysUser;
import com.oryzivora.river.login.dao.SysUserMapper;
import com.oryzivora.river.login.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author liuqi
 * @since 2024-02-04
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
