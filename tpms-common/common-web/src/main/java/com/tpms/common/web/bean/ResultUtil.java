package com.tpms.common.web.bean;

import com.tpms.common.web.exception.CommonException;

/**
 * 结果工具类
 * @author wld
 * @date 2023/5/3 - 20:49
 */
public class ResultUtil {
    public static <T> Result<T> success() {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    public static <T> Result<T> success(String msg) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(Integer code, String msg) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> error() {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(500);
        result.setMsg("操作失败");
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> set(CommonException ce) {
        Result result = new Result();
        result.setSuccess(ce.isSuccess());
        result.setCode(ce.getCode());
        result.setMsg(ce.getMsg());
        return result;
    }
}
