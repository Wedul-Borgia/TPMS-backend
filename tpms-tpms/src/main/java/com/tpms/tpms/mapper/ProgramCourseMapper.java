package com.tpms.tpms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.tp.ProgramCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:46
 */

@Mapper
public interface ProgramCourseMapper extends BaseMapper<ProgramCourse> {
    @Select("select bc.* from tp_program_course tpc " +
            "left join base_course bc on tpc .course_id = bc.course_id " +
            "where tpc.program_id = #{programId} and bc.del_flag = '0' and bc.is_stop = '0'")
    List<Course> selectCourses(String programId);
}
