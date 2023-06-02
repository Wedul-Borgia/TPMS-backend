package com.tpms.common.web.bean.query;

import com.tpms.common.web.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wld
 * @date 2023/5/16 - 23:00
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CourseQuery extends PageQuery {
    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程编号
     */
    private String courseCode;

    /**
     * 课程类型 0 公共必修课 1 专业必修课 2 公共选修课 3 专业选修课
     */
    private String courseType;

    private String orderBy;

    /**
     * 是否停用 0 启用 1停用
     */
    private String isStop;

    public CourseQuery(Integer pageNum, Integer pageSize, String courseName, String courseCode, String courseType, String isStop) {
        super(pageNum, pageSize);
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseType = courseType;
        this.isStop = isStop;
    }
}
