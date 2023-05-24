package com.tpms.tpms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.common.web.bean.tp.Plan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:46
 */

@Mapper
public interface PlanMapper extends BaseMapper<Plan> {
    @Select("select tp.*, tpp.program_name, bm.major_name from tp_plan tp " +
            "left join tp_program tpp on tp.program_id = tpp.program_id" +
            "left join base_major bm on tp.major_id = bm.major_id ${ew.customSqlSegment}")
    Page<Plan> getPage(Page page, @Param("ew") Wrapper<Plan> wrapper);

    @Select("select tp.*, tpp.program_name, bm.major_name from tp_plan tp " +
            "left join tp_program tpp on tp.program_id = tpp.program_id " +
            "left join base_major bm on tp.major_id = bm.major_id " +
            "where tp.plan_id = #{pageId} and tp.del_flag = '0'")
    Plan getByPlanId(String pageId);

    @Select("select tp.*, tpp.program_name, bm.major_name from tp_plan tp " +
            "left join tp_program tpp on tp.program_id = tpp.program_id" +
            "left join base_major bm on tp.major_id = bm.major_id ${ew.customSqlSegment}")
    List<Plan> getList(@Param("ew") Wrapper<Plan> wrapper);
}
