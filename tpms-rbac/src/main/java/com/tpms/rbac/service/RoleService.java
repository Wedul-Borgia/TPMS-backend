package com.tpms.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.sys.Role;
import com.tpms.rbac.entity.UserRole;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/13 - 21:26
 */
public interface RoleService extends IService<Role> {
    void assignPowers(String roleId, List<String> powerIds);

    List<Role> getByRoleIds(List<UserRole> roleIdList);

    boolean checkDuplicate(Role role);

    boolean checkDuplicate(Role role, String roleId);
}
