package com.tpms.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.rbac.entity.RolePower;

/**
 * @author wld
 * @date 2023/5/13 - 21:26
 */
public interface RolePowerService extends IService<RolePower> {
    void delByRoleId(String roleId);
    void delByPowerId(String powerId);
}
