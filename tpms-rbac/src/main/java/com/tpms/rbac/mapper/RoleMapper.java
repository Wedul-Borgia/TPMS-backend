package com.tpms.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tpms.common.web.bean.sys.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
@author wld
@date 2023/5/13 - 21:00
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select * from sys_role where role_id = #{roleId} and del_flag = '0'")
    Role selectByRoleId(@Param("roleId") String roleId);
}
