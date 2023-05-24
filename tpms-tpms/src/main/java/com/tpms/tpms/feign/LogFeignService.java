package com.tpms.tpms.feign;

import com.tpms.common.web.bean.sys.OperateLog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author wld
 * @date 2023/5/22 - 15:09
 */
@ComponentScan
@FeignClient(name = "tpms-sys",path = "/sys/log")
public interface LogFeignService {
    /**
     * 记录日志
     * @param operateLog
     */
    @PostMapping("/log")
    void log(@RequestBody OperateLog operateLog);
}
