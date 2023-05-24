package com.tpms.common.web.bean.query;

import com.tpms.common.web.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wld
 * @date 2023/5/22 - 14:17
 */
@Getter
@Setter
@ToString(callSuper = true)
public class OfficeQuery extends PageQuery {
    /**
     * 机构名称
     */
    private String officeName;

    /**
     * 机构编码
     */
    private String officeCode;

    /**
     * 是否停用
     */
    private String isStop;

    public OfficeQuery(Integer pageNum, Integer pageSize, String officeName, String officeCode, String isStop) {
        super(pageNum, pageSize);
        this.officeName = officeName;
        this.officeCode = officeCode;
        this.isStop = isStop;
    }
}
