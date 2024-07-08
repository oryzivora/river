package com.oryzivora.river.login.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oryzivora.river.login.model.SysRole;
import com.oryzivora.river.login.model.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author liuqi
 * @since 2024-02-04
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    @InterceptorIgnore(tenantLine = "1")
    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    Optional<SysUser> findByUsername(String username);

    @InterceptorIgnore(tenantLine = "1")
    @Select("SELECT COUNT(*) FROM sys_user WHERE username = #{username}")
    boolean existsByUsername(String username);
    @InterceptorIgnore(tenantLine = "1")
    @Select("SELECT COUNT(*) FROM sys_user WHERE email = #{email}")
    Boolean existsByEmail(String email);

    @Select("SELECT r.* FROM sys_role r " +
            "INNER JOIN sys_user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId}")
    List<SysRole> getUserRoles(@Param("userId") Long userId);
}
