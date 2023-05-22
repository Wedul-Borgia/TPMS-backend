package com.tpms.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.sys.Power;

import java.util.List;
import java.util.Map;

/**
 * @author wld
 * @date 2023/5/13 - 21:26
 */
public interface PowerService extends IService<Power> {
    List<Power> findAll(Map<String, Object> map);

    List<Power> findUserPower(String userId);

    boolean checkDuplicate(Power power);

    boolean checkDuplicate(Power power, String powerId);
}
