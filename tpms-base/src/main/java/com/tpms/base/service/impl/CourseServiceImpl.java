package com.tpms.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.base.mapper.CourseMapper;
import com.tpms.base.service.CourseService;
import com.tpms.common.web.bean.base.Course;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wld
 * @date 2023/5/16 - 14:59
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Resource
    private CourseMapper courseMapper;

    @Override
    public boolean checkDuplicate(Course course) {
        LambdaQueryWrapper<Course> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Course::getCourseName, course.getCourseName())
                .or().eq(Course::getCourseCode, course.getCourseCode());
        if (courseMapper.selectOne(wrapper) == null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean checkDuplicate(Course course, String courseId) {
        LambdaQueryWrapper<Course> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(Course::getCourseId, courseId)
                .and(i -> i.eq(Course::getCourseName, course.getCourseName())
                        .or().eq(Course::getCourseCode, course.getCourseCode()));
        if (courseMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

}
