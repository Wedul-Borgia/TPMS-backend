package com.tpms.tpms.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.common.web.bean.query.Option;
import com.tpms.common.web.bean.tp.Program;
import com.tpms.tpms.mapper.ProgramMapper;
import com.tpms.tpms.service.ProgramService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:48
 */
@Service
public class ProgramServiceImpl extends ServiceImpl<ProgramMapper, Program> implements ProgramService {
    @Resource
    private ProgramMapper programMapper;

    @Override
    public boolean checkDuplicate(Program program) {
        LambdaQueryWrapper<Program> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Program::getProgramName, program.getProgramName());
        if (programMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkDuplicate(Program program, String programId) {
        LambdaQueryWrapper<Program> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Program::getProgramName, program.getProgramName())
                .ne(Program::getProgramId, programId);
        if (programMapper.selectOne(wrapper) == null) {
            return true;
        }
        return false;
    }

    @Override
    public List<Program> getList(Wrapper<Program> wrapper) {
        return programMapper.getList(wrapper);
    }

    @Override
    public Page<Program> getPage(Page<Program> page, Wrapper<Program> wrapper) {
        return programMapper.getPage(page, wrapper);
    }

    @Override
    public List<Option> getOption(QueryWrapper<Program> wrapper) {
        return programMapper.getOption(wrapper);
    }
}
