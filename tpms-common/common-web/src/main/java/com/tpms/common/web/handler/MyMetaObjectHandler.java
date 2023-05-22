package com.tpms.common.web.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tpms.common.web.config.MyContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author wld
 * @date 2023/5/3 - 21:22
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Resource
    MyContext myContext;

    @Override
    public void insertFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject, "createBy", String.class, myContext.getUsername());
        this.strictInsertFill(metaObject, "modifyBy", String.class, myContext.getUsername());
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "modifyBy", String.class, myContext.getUsername());
        this.strictUpdateFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
    }
}
