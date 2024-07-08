package com.oryzivora.river.login.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author liuqi
 * @since 2024-02-04
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @NotBlank
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String email;

    @TableField(exist = false)
    List<SysRole> roles;

    public SysUser(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
