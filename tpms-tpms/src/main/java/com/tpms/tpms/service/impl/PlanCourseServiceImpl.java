package com.tpms.tpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.tp.PlanCourse;
import com.tpms.tpms.mapper.PlanCourseMapper;
import com.tpms.tpms.service.PlanCourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:48
 */
@Service
public class PlanCourseServiceImpl extends ServiceImpl<PlanCourseMapper, PlanCourse> implements PlanCourseService {
    @Resource
    private PlanCourseMapper planCourseMapper;

    @Override
    public List<Course> getCourses(String planId) {
        return planCourseMapper.getCourses(planId);
    }
}
