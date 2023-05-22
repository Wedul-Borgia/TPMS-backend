package com.tpms.common.web.bean.query;

import com.tpms.common.web.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wld
 * @date 2023/5/22 - 16:35
 */
@Getter
@Setter
@ToString(callSuper = true)
public class OperateLogQuery extends PageQuery {
    public OperateLogQuery(Integer pageNum, Integer pageSize, String logModule, String logUser, String logTime, String logEvent) {
        super(pageNum, pageSize);
        this.logModule = logModule;
        this.logUser = logUser;
        this.logTime = logTime;
        this.logEvent = logEvent;
    }

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
}
