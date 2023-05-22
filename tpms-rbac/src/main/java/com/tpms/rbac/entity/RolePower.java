package com.tpms.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色权限关系表
@author wld
@date 2023/5/13 - 21:20
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_role_power")
public class RolePower {
    /**
    * 角色ID
    */
    private String roleId;

    /**
    * 权限ID
    */
    private String powerId;
}
