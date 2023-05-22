package com.tpms.common.web.bean.query;

import com.tpms.common.web.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wld
 * @date 2023/5/16 - 18:09
 */
@Getter
@Setter
@ToString(callSuper = true)
public class UserQuery extends PageQuery {
    /**
     * 用户名称
     */
    private String username;

    /**
     * 状态 0锁定 1有效
     */
    private String userStatus;

    /**
     * 真实名称
     */
    private String trueName;

    /**
     * 性别 0男 1女 2保密
     */
    private String userSex;

    public UserQuery(Integer pageNum, Integer pageSize, String username, String userStatus, String trueName, String userSex) {
        super(pageNum, pageSize);
        this.username = username;
        this.userStatus = userStatus;
        this.trueName = trueName;
        this.userSex = userSex;
    }
}
