package com.tpms.common.web.bean.base;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 课程
 * @author wld
 * @date 2023/5/16 - 14:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "base_course")
public class Course {
    /**
     * 课程ID
     */
    @TableId(value = "course_id", type = IdType.ASSIGN_UUID)
    private String courseId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程编号
     */
    private String courseCode;

    /**
     * 学分
     */
    private Float credit;

    /**
     * 学时
     */
    private Float xueshi;

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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime modifyTime;

    /**
     * 修改者
     */
    @TableField(fill = FieldFill.INSERT)
    private String modifyBy;

    /**
     * 机构id
     */
    private String officeId;

    /**
     * 课程类型 0 公共必修课 1 专业必修课 2 公共选修课 3 专业选修课
     */
    private String courseType;

    /**
     * 是否停用 0 启用 1停用
     */
    private String isStop;
}

