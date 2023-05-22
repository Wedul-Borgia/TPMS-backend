package com.tpms.tpms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.tp.PlanCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:46
 */

@Mapper
public interface PlanCourseMapper extends BaseMapper<PlanCourse> {
    @Select("select bc.* from tp_plan_course tpc " +
            "left join base_course bc on tpc .course_id = bc.course_id " +
            "where tpc.plan_id = #{planId}")
    List<Course> getCourses(String planId);
}
