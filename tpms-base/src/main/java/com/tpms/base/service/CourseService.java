package com.tpms.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.base.Course;

/**
 * @author wld
 * @date 2023/5/16 - 14:57
 */
public interface CourseService extends IService<Course> {
    boolean checkDuplicate(Course course);

    boolean checkDuplicate(Course course, String courseId);
}
