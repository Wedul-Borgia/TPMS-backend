package com.tpms.common.web.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 封装服务器端返回的结果
 * @author wld
 * @date 2023/5/3 - 20:38
 */
@Getter
@Setter
@ToString
public class Result<T> {

    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 请求响应状态码
     */
    private int code;
    /**
     * 请求结果描述信息
     */
    private String msg;
    /**
     * 请求结果数据
     */
    private T data;

    /**
     * 将key-value形式的成对出现的参数转换为JSON
     *
     * @param objs
     * @return
     */
    public Result<T> buildData(Object... objs) {
        if (objs.length % 2 != 0) {
            throw new RuntimeException("参数个数不对");
        }
        for (int i = 0; i < objs.length; i += 2) {
            if (!(objs[i] instanceof String)) {
                throw new RuntimeException("奇数参数必须为字符串");
            }
        }

        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < objs.length; i += 2) {
            map.put((String) objs[i], objs[i + 1]);
        }

        this.data = (T) map;
        return this;
    }
}
