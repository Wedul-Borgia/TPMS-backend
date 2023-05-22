package com.tpms.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.rbac.entity.UserRole;
import com.tpms.rbac.mapper.UserRoleMapper;
import com.tpms.rbac.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @author wld
 * @date 2023/5/13 - 21:27
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
