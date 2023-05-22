package com.tpms.tpms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.tp.ProgramCourse;
import com.tpms.tpms.mapper.ProgramCourseMapper;
import com.tpms.tpms.service.ProgramCourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:48
 */
@Service
public class ProgramCourseServiceImpl extends ServiceImpl<ProgramCourseMapper, ProgramCourse> implements ProgramCourseService {
    @Resource
    private ProgramCourseMapper programCourseMapper;

    @Override
    public List<Course> getCourses(String programId) {
        return programCourseMapper.selectCourses(programId);
    }
}
