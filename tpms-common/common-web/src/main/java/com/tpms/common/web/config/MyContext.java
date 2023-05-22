package com.tpms.common.web.config;

import org.springframework.stereotype.Component;

/**
 * @author wld
 * @date 2023/5/16 - 1:43
 */
@Component
public class MyContext {
    private static ThreadLocal<String> tenantContext = new ThreadLocal();
    private static ThreadLocal<String> usernameContext = new ThreadLocal();

    public void setOfficeId(String officeId){
        tenantContext.set(officeId);
    }

    public String getOfficeId(){
        return tenantContext.get();
    }

    public void setUsername(String username){
        usernameContext.set(username);
    }

    public String getUsername(){
        return usernameContext.get();
    }
}
