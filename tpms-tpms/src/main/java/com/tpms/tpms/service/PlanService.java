package com.tpms.tpms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.tp.Plan;

/**
 * @author wld
 * @date 2023/5/17 - 20:47
 */
public interface PlanService extends IService<Plan> {
    boolean checkDuplicate(Plan plan);

    boolean checkYearDuplicate(Plan plan);

    boolean checkDuplicate(Plan plan, String planId);

    Page<Plan> getPage(Page<Plan> page, Wrapper<Plan> wrapper);

    Plan getByPlanId(String planId);
}
