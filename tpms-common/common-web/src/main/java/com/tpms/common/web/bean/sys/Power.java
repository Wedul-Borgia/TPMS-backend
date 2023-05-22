package com.tpms.common.web.bean.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 权限
 *
 * @author wld
 * @date 2023/5/13 - 21:02
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_power")
public class Power {
    /**
     * 权限ID
     */
    @TableId(value = "power_id", type = IdType.ASSIGN_UUID)
    private String powerId;

    /**
     * 权限名称
     */
    private String powerName;

    /**
     * 权限类型 1 菜单 2功能 3 API
     */
    private String powerType;

    /**
     * 权限编码
     */
    private String powerCode;

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
     * 权限等级 0 普通 1 SAAS
     */
    private String level;
}
