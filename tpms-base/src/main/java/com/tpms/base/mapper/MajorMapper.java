package com.tpms.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.common.web.bean.base.Major;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/16 - 14:55
 */
@Mapper
public interface MajorMapper extends BaseMapper<Major> {

    @Select("select bm.*, bc.college_name from base_major bm " +
            "left join base_college bc on bm.college_id = bc.college_id ${ew.customSqlSegment}")
    Page<Major> getPage(Page page, @Param("ew") Wrapper<Major> wrapper);

    @Select("select bm.*, bc.college_name from base_major bm " +
            "left join base_college bc on bm.college_id = bc.college_id ${ew.customSqlSegment}")
    List<Major> getList(@Param("ew") Wrapper<Major> wrapper);
}
