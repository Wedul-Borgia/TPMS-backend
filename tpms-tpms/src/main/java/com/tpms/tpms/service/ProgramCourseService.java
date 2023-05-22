package com.tpms.tpms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.tp.ProgramCourse;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:47
 */
public interface ProgramCourseService extends IService<ProgramCourse> {
    List<Course> getCourses(String programId);
}
