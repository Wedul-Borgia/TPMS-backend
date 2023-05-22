package com.tpms.common.web.bean.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户
 *
 * @author wld
 * @date 2023/5/3 - 22:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_UUID)
    private String userId;

    /**
     * 机构id
     */
    private String officeId;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 联系电话
     */
    private String userPhone;

    /**
     * 状态 0锁定 1有效
     */
    private String userStatus;

    /**
     * 真实名称
     */
    private String trueName;

    /**
     * 性别 0男 1女 2保密
     */
    private String userSex;

    /**
     * 最近访问时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 密码修改时间 用于判断密码是否过期
     */
    private LocalDateTime passwordUpdateTime;

    /**
     * 机构名称
     */
    private String officeName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 修改者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyBy;

    /**
     * 删除标志
     */
    @TableLogic(value = "0",delval = "1")
    private String delFlag;

    /**
     * 用户等级 0 用户 1 管理员 2 SAAS管理员
     */
    private String level;

    /**
     * 用户与角色 多对多
     */
    @TableField(exist = false)
    private Set<Role> roles = new HashSet<>();
}
