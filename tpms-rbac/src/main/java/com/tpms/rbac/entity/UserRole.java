package com.tpms.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户角色关系表
@author wld
@date 2023/5/13 - 21:20
*/

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user_role")
public class UserRole {
    /**
     * 角色ID
     */
    private String roleId;

    /**
    * 用户ID
    */
    private String userId;
}
