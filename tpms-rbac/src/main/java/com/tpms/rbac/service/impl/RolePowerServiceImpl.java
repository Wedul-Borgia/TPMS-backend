package com.tpms.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.rbac.entity.RolePower;
import com.tpms.rbac.mapper.RolePowerMapper;
import com.tpms.rbac.service.RolePowerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wld
 * @date 2023/5/13 - 21:27
 */
@Service
public class RolePowerServiceImpl extends ServiceImpl<RolePowerMapper, RolePower> implements RolePowerService {

    @Resource
    private RolePowerMapper rolePowerMapper;

    @Override
    public void delByRoleId(String roleId) {
        QueryWrapper<RolePower> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        rolePowerMapper.delete(wrapper);
    }

    @Override
    public void delByPowerId(String powerId) {
        QueryWrapper<RolePower> wrapper = new QueryWrapper<>();
        wrapper.eq("power_id", powerId);
        rolePowerMapper.delete(wrapper);
    }

    @Override
    public List<String> getByRoleId(String roleId) {
        return rolePowerMapper.getByRoleId(roleId);
    }
}
