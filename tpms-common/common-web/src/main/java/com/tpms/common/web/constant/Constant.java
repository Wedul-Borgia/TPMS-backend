package com.tpms.common.web.constant;

import avro.shaded.com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author wld
 * @date 2023/5/13 - 23:43
 */
public interface Constant {
    Integer PAGE_SIZE = 20;

    Map<String,String> COURSE_TYPE = ImmutableMap.<String,String>builder()
            .put("公共必修课","0")
            .put("专业必修课","1")
            .put("公共选修课","2")
            .put("专业选修课","3")
            .build();

    Map<String,String> COURSE_TYPE_CN = ImmutableMap.<String,String>builder()
            .put("0","公共必修课")
            .put("1","专业必修课")
            .put("2","公共选修课")
            .put("3","专业选修课")
            .build();

    /**
     * 用户等级
     */
    final class UserLevel{
        /**saas管理员*/
        public static final String SAASADMIN = "2";
        /**机构管理员*/
        public static final String ADMIN = "1";
        /**用户*/
        public static final String USER = "0";
    }

    /**
     * 权限等级
     */
    final class PowerLevel{
        /**saas管理员*/
        public static final String SAASADMIN = "1";
        /**机构管理员*/
        public static final String ADMIN = "0";
    }
}
