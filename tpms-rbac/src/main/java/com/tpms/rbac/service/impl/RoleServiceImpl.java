package com.tpms.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.common.web.bean.sys.Role;
import com.tpms.rbac.entity.RolePower;
import com.tpms.rbac.entity.UserRole;
import com.tpms.rbac.mapper.RoleMapper;
import com.tpms.rbac.service.RolePowerService;
import com.tpms.rbac.service.RoleService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wld
 * @date 2023/5/13 - 21:27
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RolePowerService rolePowerService;

    /**
     * 分配权限
     * @param roleId    角色id
     * @param powerIds   权限id列表
     */
    public void assignPowers(String roleId, List<String> powerIds) {
        //获取分配的角色对象
//        role检验
        Role role = roleMapper.selectByRoleId(roleId);
        //构造角色的权限集合
        List<RolePower> rolePowerList = new ArrayList<>();
        for (String powerId : powerIds) {
            RolePower rolePower = RolePower.builder().powerId(powerId).roleId(roleId).build();

            rolePowerList.add(rolePower);//当前菜单或按钮的权限
        }
        //设置角色和权限的关系
        rolePowerService.saveBatch(rolePowerList);
        //更新修改时间
//        roleMapper.updateById();
    }

    @Override
    public List<Role> getByRoleIds(List<UserRole> roleIdList) {
        List<Role> res = new ArrayList<>();
        for (UserRole ur: roleIdList) {
            Role role = roleMapper.selectByRoleId(ur.getRoleId());
            if (!ObjectUtils.isEmpty(role)){
                res.add(role);
            }
        }
        return res;
    }

    @Override
    public boolean checkDuplicate(Role role) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Role::getRoleName, role.getRoleName());
        if (roleMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkDuplicate(Role role, String roleId) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Role::getRoleName, role.getRoleName())
                .ne(Role::getRoleId,roleId);
        if (roleMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }
}
