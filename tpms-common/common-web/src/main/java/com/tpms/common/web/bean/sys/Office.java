package com.tpms.common.web.bean.sys;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 机构
@author wld
@date 2023/5/3 - 19:50
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_office")
public class Office {
    /**
    * 机构ID
    */
    @TableId(value = "office_id", type = IdType.ASSIGN_UUID)
    private String officeId;

    /**
    * 机构名称
    */
    private String officeName;

    /**
    * 机构编码
    */
    private String officeCode;

    /**
    * 机构登录账号
    */
    private String managerId;

    /**
    * 是否停用
    */
    private String isStop;

    /**
    * 创建者
    */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
    * 更新者
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String modifyBy;

    /**
    * 更新时间
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
    * 删除标记
    */
    @TableLogic(value = "0",delval = "1")
    private String delFlag;
}
