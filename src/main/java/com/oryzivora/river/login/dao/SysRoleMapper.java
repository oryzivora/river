package com.oryzivora.river.login.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.oryzivora.river.login.models.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oryzivora.river.login.constant.ERole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author liuqi
 * @since 2024-02-04
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    @InterceptorIgnore(tenantLine = "1")
    @Select("SELECT * FROM sys_role WHERE name = #{name}")
    Optional<SysRole> findByName(ERole name);
}
