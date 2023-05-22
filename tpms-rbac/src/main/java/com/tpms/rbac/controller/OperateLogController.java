package com.tpms.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.query.OperateLogQuery;
import com.tpms.common.web.bean.sys.OperateLog;
import com.tpms.rbac.service.OperateLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author wld
 * @date 2023/5/22 - 14:46
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/log")
public class OperateLogController {

    @Resource
    private OperateLogService operateLogService;

    /**
     * 记录日志
     *
     * @param operateLog
     */
    @PostMapping("/log")
    public void log(@RequestBody OperateLog operateLog) {

        operateLog.setLogTime(String.valueOf(LocalDateTime.now()));
        operateLogService.save(operateLog);
    }

    /**
     * 分页查询
     *
     * @param operateLogQuery
     * @return
     */
    @PostMapping("/page")
    public Result page(@RequestBody OperateLogQuery operateLogQuery) {
        Page<OperateLog> page = new Page<>(operateLogQuery.getPageNum(), operateLogQuery.getPageSize());

        QueryWrapper<OperateLog> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(operateLogQuery.getLogModule())) {
            wrapper.eq("log_module", operateLogQuery.getLogModule());
        }
        if (StringUtils.isNotBlank(operateLogQuery.getLogEvent())) {
            wrapper.eq("log_event", operateLogQuery.getLogEvent());
        }
        if (StringUtils.isNotBlank(operateLogQuery.getLogUser())) {
            wrapper.like("log_user", operateLogQuery.getLogUser());
        }
        if (StringUtils.isNotBlank(operateLogQuery.getLogTime())) {
            wrapper.likeRight("log_time",String.valueOf(operateLogQuery.getLogTime()));
        }
        wrapper.orderByAsc("log_time");
        page = operateLogService.page(page, wrapper);
        PageResult<OperateLog> pageBean = PageResult.init(page);

        return ResultUtil.success().buildData("page", pageBean);
    }
}
