package com.tpms.common.web.bean.query;

import com.tpms.common.web.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wld
 * @date 2023/5/17 - 22:08
 */
@Getter
@Setter
@ToString(callSuper = true)
public class PlanQuery extends PageQuery {
    /**
     * 培养计划名称
     */
    private String planName;

    /**
     * 培养方案ID
     */
    private String programId;

    /**
     * 专业ID
     */
    private String majorId;

    /**
     * 状态 0 未提交 1 已提交 2 已通过 3 未通过
     */
    private String status;

    /**
     * 年份
     */
    private String theYear;

    public PlanQuery(Integer pageNum, Integer pageSize, String planName, String programId, String majorId, String theYear) {
        super(pageNum, pageSize);
        this.planName = planName;
        this.programId = programId;
        this.majorId = majorId;
        this.theYear = theYear;
    }
}
