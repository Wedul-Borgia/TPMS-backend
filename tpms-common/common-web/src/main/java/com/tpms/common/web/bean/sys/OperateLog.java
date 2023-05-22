package com.tpms.common.web.bean.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_operate_log")
public class OperateLog {
    /**
     * 日志ID
     */
    @TableId(value = "log_id", type = IdType.ASSIGN_UUID)
    private String logId;

    /**
     * 机构ID
     */
    private String officeId;

    /**
     * 机构名称
     */
    private String officeName;

    /**
     * 日志模块
     */
    private String logModule;

    /**
     * 操作用户
     */
    private String logUser;

    /**
     * 操作时间
     */
    private String logTime;

    /**
     * 日志事件
     */
    private String logEvent;

    /**
     * 日志消息
     */
    private String logMessage;
}

