package com.tpms.common.web.bean.tp;

import com.baomidou.mybatisplus.annotation.*;
import com.tpms.common.web.bean.base.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tp_program")
public class Program implements Serializable {
    /**
     * 培养方案ID
     */
    @TableId(value = "program_id", type = IdType.ASSIGN_UUID)
    private String programId;

    /**
     * 培养方案名称
     */
    private String programName;

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
     * 机构ID
     */
    private String officeId;

    /**
     * 专业ID
     */
    private String majorId;

    /**
     *培养目标
     */
    private String targets;

    /**
     *毕业要求
     */
    private String requires;

    /**
     * 必修要求学分
     */
    private Float bxCredit;

    /**
     * 选修要求学分
     */
    private Float xxCredit;

    /**
     * 学制
     */
    private String xuezhi;

    /**
     * 学位
     */
    private String xuewei;

    @TableField(exist = false)
    private String majorName;

    /**
     * 是否停用 0 启用 1停用
     */
    private String isStop;

    @TableField(exist = false)
    private List<String> courseIds;

    @TableField(exist = false)
    private String changeCourse;

    @TableField(exist = false)
    private List<Course> courses;
}

