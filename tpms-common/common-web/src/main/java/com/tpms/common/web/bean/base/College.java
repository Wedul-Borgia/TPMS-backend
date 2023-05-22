package com.tpms.common.web.bean.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学院
 * @author wld
 * @date 2023/5/16 - 14:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "base_college")
public class College {
    /**
     * 学院ID
     */
    @TableId(value = "college_id", type = IdType.ASSIGN_UUID)
    private String collegeId;

    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 院校编号
     */
    private String collegeCode;

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
     * 机构id
     */
    private String officeId;

    /**
     * 是否停用 0 启用 1停用
     */
    private String isStop;
}

