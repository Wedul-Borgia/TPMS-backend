package com.tpms.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.base.College;

/**
 * @author wld
 * @date 2023/5/16 - 14:57
 */
public interface CollegeService extends IService<College> {
    boolean checkDuplicate(College college);
    boolean checkDuplicate(College college, String collegeId);
}
