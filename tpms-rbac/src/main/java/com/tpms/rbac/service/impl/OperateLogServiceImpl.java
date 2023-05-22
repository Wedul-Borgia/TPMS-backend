package com.tpms.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.common.web.bean.sys.OperateLog;
import com.tpms.rbac.mapper.OperateLogMapper;
import com.tpms.rbac.service.OperateLogService;
import org.springframework.stereotype.Service;

/**
 * @author wld
 * @date 2023/5/13 - 21:27
 */
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {


}
