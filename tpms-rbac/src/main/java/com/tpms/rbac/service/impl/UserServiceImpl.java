package com.tpms.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.common.web.bean.sys.User;
import com.tpms.rbac.mapper.UserMapper;
import com.tpms.rbac.service.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author wld
 * @date 2023/5/3 - 19:54
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean generateAdmin(String office_id,String officeName) {
        String password = new Md5Hash(office_id, "ADMIN", 3).toString();
        User admin = User.builder().username("ADMIN")
                .officeId(office_id).trueName("初始管理员")
                .password(password).userStatus("1")
                .officeName(officeName)
                .level("1").delFlag("0").build();
        userMapper.insert(admin);
        return true;
    }

    @Override
    public User getByUsername(String username, String office_id) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getOfficeId, office_id).eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void updateLogintime(String userId, LocalDateTime now, String officeId) {
        userMapper.updateLogintime(userId,now,officeId);
    }

    @Override
    public boolean checkDuplicate(User user) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, user.getUsername());
        if (userMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

}
