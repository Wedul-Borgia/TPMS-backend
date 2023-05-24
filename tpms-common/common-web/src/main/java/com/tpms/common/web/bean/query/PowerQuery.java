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
public class PowerQuery extends PageQuery {
    /**
     * 权限名称
     */
    private String powerName;

    /**
     * 权限类型 1 菜单 2功能 3 API
     */
    private String powerType;

    /**
     * 权限编码
     */
    private String powerCode;

    public PowerQuery(Integer pageNo, Integer pageSize, String powerName, String powerType, String powerCode) {
        super(pageNo, pageSize);
        this.powerName = powerName;
        this.powerType = powerType;
        this.powerCode = powerCode;
    }
}
