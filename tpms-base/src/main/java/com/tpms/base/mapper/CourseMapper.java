package com.tpms.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tpms.common.web.bean.base.Course;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wld
 * @date 2023/5/16 - 14:55
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
