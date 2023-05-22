package com.tpms.common.web.bean.tp;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tp_plan_course")
public class PlanCourse implements Serializable {
    /**
     * 年份
     */
    private String theYear;

    /**
     * 专业ID
     */
    private String majorId;

    /**
     * 培养计划ID
     */
    private String planId;

    /**
     * 课程ID
     */
    private String courseId;

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
     * 删除标志
     */
    @TableLogic(value = "0",delval = "1")
    private String delFlag;
}

