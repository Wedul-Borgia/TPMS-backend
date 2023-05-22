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
public class CollegeQuery extends PageQuery {
    /**
     * 学院名称
     */
    private String collegeName;

    /**
     * 院校编号
     */
    private String collegeCode;

    /**
     * 是否停用 0 启用 1停用
     */
    private String isStop;

    public CollegeQuery(Integer pageNum, Integer pageSize, String collegeName, String collegeCode, String isStop) {
        super(pageNum, pageSize);
        this.collegeName = collegeName;
        this.collegeCode = collegeCode;
        this.isStop = isStop;
    }
}
