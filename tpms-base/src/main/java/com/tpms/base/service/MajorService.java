package com.tpms.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.base.Major;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/16 - 14:57
 */
public interface MajorService extends IService<Major> {
    boolean checkDuplicate(Major major);

    boolean checkDuplicate(Major major, String majorId);

    Page<Major> getPage(Page<Major> page, Wrapper<Major> wrapper);

    List<Major> getList(Wrapper<Major> wrapper);
}
