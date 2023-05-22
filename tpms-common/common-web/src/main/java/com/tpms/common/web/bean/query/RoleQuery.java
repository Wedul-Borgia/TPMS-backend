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
public class RoleQuery extends PageQuery {
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色备注
     */
    private String remark;

    public RoleQuery(Integer pageNum, Integer pageSize, String roleName, String remark) {
        super(pageNum, pageSize);
        this.roleName = roleName;
        this.remark = remark;
    }
}
