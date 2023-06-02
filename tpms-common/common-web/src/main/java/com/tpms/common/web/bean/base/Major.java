package com.tpms.common.web.bean.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 专业
 * @author wld
 * @date 2023/5/16 - 14:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "base_major")
public class Major implements Serializable {
    /**
     * 专业ID
     */
    @TableId(value = "major_id", type = IdType.ASSIGN_UUID)
    private String majorId;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 专业编号
     */
    private String majorCode;

    /**
     * 学院ID
     */
    private String collegeId;

    @TableField(exist = false)
    private String collegeName;

    /**
     * 删除标志
     */
    @TableLogic(value = "0",delval = "1")
    private String delFlag;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 机构ID
     */
    private String officeId;

    /**
     * 是否停用 0 启用 1停用
     */
    private String isStop;
}

