package com.tpms.tpms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.common.web.bean.tp.Program;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:46
 */

@Mapper
public interface ProgramMapper extends BaseMapper<Program> {
    @Select("select tp.*, bm.major_name from tp_program tp " +
            "left join base_major bm on tp.major_id = bm.major_id ${ew.customSqlSegment}")
    Page<Program> getPage(Page page, @Param("ew") Wrapper<Program> wrapper);

    @Select("select tp.*, bm.major_name from tp_program tp " +
            "left join base_major bm on tp.major_id = bm.major_id ${ew.customSqlSegment}")
    List<Program> getList(@Param("ew") Wrapper<Program> wrapper);
}
