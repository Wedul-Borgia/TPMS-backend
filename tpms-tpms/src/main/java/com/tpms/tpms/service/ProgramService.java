package com.tpms.tpms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tpms.common.web.bean.query.Option;
import com.tpms.common.web.bean.tp.Program;

import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:47
 */
public interface ProgramService extends IService<Program> {
    boolean checkDuplicate(Program program);

    boolean checkDuplicate(Program program, String programId);

    List<Program> getList(Wrapper<Program> wrapper);

    Page<Program> getPage(Page<Program> page, Wrapper<Program> wrapper);

    List<Option> getOption(QueryWrapper<Program> wrapper);
}
