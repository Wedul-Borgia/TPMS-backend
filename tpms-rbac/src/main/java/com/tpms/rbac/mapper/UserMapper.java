package com.tpms.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tpms.common.web.bean.sys.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

/**
@author wld
@date 2023/5/3 - 22:37
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("update sys_user set last_login_time = #{now} where user_id = #{userId}")
    void updateLogintime(String userId, LocalDateTime now, String officeId);
}
