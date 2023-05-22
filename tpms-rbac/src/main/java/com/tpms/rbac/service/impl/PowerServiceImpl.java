package com.tpms.rbac.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.common.web.bean.sys.Power;
import com.tpms.rbac.mapper.PowerMapper;
import com.tpms.rbac.service.PowerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author wld
 * @date 2023/5/13 - 21:27
 */
@Service
public class PowerServiceImpl extends ServiceImpl<PowerMapper, Power> implements PowerService {
    @Resource
    PowerMapper powerMapper;

    public List<Power> findAll(Map<String, Object> map) {

        LambdaQueryWrapper<Power> wrapper = Wrappers.lambdaQuery();

        if (StringUtils.isNotBlank(String.valueOf(map.get("level")))) {
            System.out.println(map.get("level"));
            wrapper.eq(Power::getLevel, map.get("level"));
        }

        return powerMapper.selectList(wrapper);
    }

    @Override
    public List<Power> findUserPower(String userId) {
        return powerMapper.selectByUserId(userId);
    }

    @Override
    public boolean checkDuplicate(Power power) {
        LambdaQueryWrapper<Power> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Power::getPowerName, power.getPowerName())
                .or().eq(Power::getPowerCode, power.getPowerCode());
        if (powerMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkDuplicate(Power power, String powerId) {
        LambdaQueryWrapper<Power> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(Power::getPowerId, powerId)
                .and(i -> i.eq(Power::getPowerName, power.getPowerName())
                        .or().eq(Power::getPowerCode, power.getPowerCode()));
        if (powerMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }
}
