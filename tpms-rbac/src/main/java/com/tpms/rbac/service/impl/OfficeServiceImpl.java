package com.tpms.rbac.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tpms.common.web.bean.sys.Office;
import com.tpms.rbac.mapper.OfficeMapper;
import com.tpms.rbac.service.OfficeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wld
 * @date 2023/5/3 - 19:54
 */
@Service
public class OfficeServiceImpl extends ServiceImpl<OfficeMapper, Office> implements OfficeService {
    @Resource
    private OfficeMapper officeMapper;
}
