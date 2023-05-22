package com.tpms.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.base.mapper.MajorMapper;
import com.tpms.base.service.MajorService;
import com.tpms.common.web.bean.base.Major;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wld
 * @date 2023/5/16 - 14:59
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {
    @Resource
    private MajorMapper majorMapper;

    @Override
    public boolean checkDuplicate(Major major) {
        LambdaQueryWrapper<Major> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Major::getMajorCode, major.getMajorCode())
                .or().eq(Major::getMajorName, major.getMajorName());
        if (majorMapper.selectOne(wrapper) == null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean checkDuplicate(Major major, String majorId) {
        LambdaQueryWrapper<Major> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(Major::getMajorId, majorId)
                .and(i -> i.eq(Major::getMajorName, major.getMajorName())
                        .or().eq(Major::getMajorCode, major.getMajorCode()));
        if (majorMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

    @Override
    public Page<Major> getPage(Page<Major> page, Wrapper<Major> wrapper) {
        return majorMapper.getPage(page,wrapper);
    }

    @Override
    public List<Major> getList(Wrapper<Major> wrapper) {
        return majorMapper.getList(wrapper);
    }
}
