package com.tpms.common.web.bean;

import com.tpms.common.web.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@AllArgsConstructor
public class PageQuery {
    /**
     * 页码
     */
    private Integer pageNo;
    /**
     * 页面大小
     */
    private Integer pageSize;

    public Integer getPageNo() {
        return pageNo == null ? 1 : pageNo;
    }

    public Integer getPageSize() {
        return pageSize == null ? Constant.PAGE_SIZE : pageSize;
    }
}
