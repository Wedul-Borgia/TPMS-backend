package com.tpms.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.sys.User;

import java.time.LocalDateTime;

/**
 * @author wld
 * @date 2023/5/3 - 19:51
 */

public interface UserService extends IService<User> {
    boolean generateAdmin(String office_id);

    User getByUsername(String username,String office_id);

    void updateLogintime(String userId, LocalDateTime now, String officeId);

    boolean checkDuplicate(User user);
}
