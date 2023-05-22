package com.tpms.common.web.bean.query;

import com.tpms.common.web.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wld
 * @date 2023/5/16 - 23:00
 */
@Getter
@Setter
@ToString(callSuper = true)
public class MajorQuery extends PageQuery {
    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 专业编号
     */
    private String majorCode;

    /**
     * 学院ID
     */
    private String collegeId;

    /**
     * 是否停用 0 启用 1停用
     */
    private String isStop;

    public MajorQuery(Integer pageNum, Integer pageSize, String majorName, String majorCode, String collegeId, String isStop) {
        super(pageNum, pageSize);
        this.majorName = majorName;
        this.majorCode = majorCode;
        this.collegeId = collegeId;
        this.isStop = isStop;
    }
}
