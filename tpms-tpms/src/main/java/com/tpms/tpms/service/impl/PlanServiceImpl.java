package com.tpms.tpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.common.web.bean.tp.Plan;
import com.tpms.tpms.mapper.PlanMapper;
import com.tpms.tpms.service.PlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wld
 * @date 2023/5/17 - 20:48
 */
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {
    @Resource
    private PlanMapper planMapper;

    @Override
    public boolean checkDuplicate(Plan plan) {
        LambdaQueryWrapper<Plan> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Plan::getPlanName, plan.getPlanName());
        if (planMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkYearDuplicate(Plan plan) {
        LambdaQueryWrapper<Plan> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Plan::getTheYear, plan.getTheYear())
                .eq(Plan::getMajorId, plan.getMajorId());
        if (planMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkDuplicate(Plan plan, String planId) {
        LambdaQueryWrapper<Plan> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Plan::getPlanName, plan.getPlanName())
                .ne(Plan::getPlanId, planId);
        if (planMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

    @Override
    public Page<Plan> getPage(Page<Plan> page, Wrapper<Plan> wrapper) {
        return planMapper.getPage(page, wrapper);
    }

    @Override
    public Plan getByPlanId(String planId) {
        return planMapper.getByPlanId(planId);
    }
}
