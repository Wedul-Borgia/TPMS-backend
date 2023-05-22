package com.tpms.common.web.bean.query;

import com.tpms.common.web.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wld
 * @date 2023/5/17 - 21:38
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ProgramQuery extends PageQuery {
    /**
     * 培养方案名称
     */
    private String programName;

    /**
     * 专业ID
     */
    private String majorId;

    /**
     * 是否停用 0 启用 1停用
     */
    private String isStop;

    public ProgramQuery(Integer pageNum, Integer pageSize, String programName, String majorId, String isStop) {
        super(pageNum, pageSize);
        this.programName = programName;
        this.majorId = majorId;
        this.isStop = isStop;
    }

}
