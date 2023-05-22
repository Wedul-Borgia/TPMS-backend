package com.tpms.common.web.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author wld
 * @date 2023/5/3 - 22:04
 */
public class StringUtil {
    final static Pattern pattern = Pattern.compile("_(\\w)");
    /**
     * 将驼峰式命名转换成下划线命名
     *
     * @param str
     * @return
     */
    public static String hump2underscore(String str) {
        String lowerCase = str.replaceAll("[A-Z]", "_$0").toLowerCase();
        if (lowerCase.startsWith("_")) {
            lowerCase = lowerCase.substring(1);
        }
        return lowerCase;
    }

    /**
     * 下划线转驼峰
     *
     * @param str
     * @return
     */
    public static String underline2hump(String str) {
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 首字母转大写
     *
     * @param str
     * @return
     */
    public static String first2Upper(String str) {
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        }
    }
}
