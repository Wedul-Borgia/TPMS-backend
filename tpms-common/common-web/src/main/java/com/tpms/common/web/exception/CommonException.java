package com.tpms.common.web.exception;

/**
 * @author wld
 * @date 2023/5/15 - 18:30
 */

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 自定义异常
 */
@Getter
@Builder
@ToString
public class CommonException extends Exception  {

    private int code;
    private String msg;
    private boolean success;

}
