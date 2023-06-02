package com.tpms.common.web.bean.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wld
 * @date 2023/6/1 - 13:45
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Option {
    /**
     * 显示值
     */
    private String label;

    /**
     * 实际值
     */
    private String value;
}
