package com.tpms.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tpms.common.web.bean.sys.Power;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/13 - 21:02
 */
@Mapper
public interface PowerMapper extends BaseMapper<Power> {

    @Select("select power_code, power_type, parent_code from sys_power sp " +
            "left join sys_role_power srp on srp.power_id = sp.power_id " +
            "left join sys_user_role sur on sur.role_id = srp.role_id " +
            "where sur.user_id = #{userId} and sp.del_flag = '0'")
    List<Power> selectByUserId(String userId);
}
