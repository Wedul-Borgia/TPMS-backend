package com.tpms.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tpms.rbac.entity.RolePower;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
@author wld
@date 2023/5/13 - 21:20
*/
@Mapper
public interface RolePowerMapper extends BaseMapper<RolePower> {
    @Select("select power_id from sys_role_power where role_id = #{roleId} ")
    List<String> getByRoleId(String roleId);
}
