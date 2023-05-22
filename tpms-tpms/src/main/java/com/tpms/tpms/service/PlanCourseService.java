package com.tpms.tpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.tp.PlanCourse;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:47
 */
public interface PlanCourseService extends IService<PlanCourse> {
    List<Course> getCourses(String planId);
}
