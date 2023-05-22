package com.tpms.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.base.mapper.CollegeMapper;
import com.tpms.base.service.CollegeService;
import com.tpms.common.web.bean.base.College;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wld
 * @date 2023/5/16 - 14:59
 */
@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {
    @Resource
    private CollegeMapper collegeMapper;

    @Override
    public boolean checkDuplicate(College college) {
        LambdaQueryWrapper<College> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(College::getCollegeCode, college.getCollegeName())
                .or().eq(College::getCollegeName, college.getCollegeName());
        if (collegeMapper.selectOne(wrapper) == null) {
            return true;
        }

        return false;
    }

    @Override
    public boolean checkDuplicate(College college, String collegeId) {
        LambdaQueryWrapper<College> wrapper = Wrappers.lambdaQuery();
        wrapper.ne(College::getCollegeId, collegeId)
                .and(i -> i.eq(College::getCollegeName, college.getCollegeName())
                        .or().eq(College::getCollegeCode, college.getCollegeCode()));
        if (collegeMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }
}
