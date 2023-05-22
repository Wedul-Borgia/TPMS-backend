package com.tpms.common.web.bean.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 角色
 *
 * @author wld
 * @date 2023/5/13 - 21:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_role")
public class Role {
    /**
     * 角色ID
     */
    @TableId(value = "role_id", type = IdType.ASSIGN_UUID)
    private String roleId;

    /**
     * 机构id
     */
    private String officeId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色备注
     */
    private String remark;

    /**
     * 删除标志
     */
    @TableLogic(value = "0",delval = "1")
    private String delFlag;

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
     * 角色与权限  多对多
     */
    @TableField(exist = false)
    private Set<Power> powers = new HashSet<Power>(0);

    @TableField(exist = false)
    private String powerIds;
}
