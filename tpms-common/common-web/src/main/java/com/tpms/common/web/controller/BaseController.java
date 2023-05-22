package com.tpms.common.web.controller;

import com.tpms.common.web.bean.ProfileResult;
import com.tpms.common.web.config.MyContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wld
 * @date 2023/5/13 - 22:14
 */
public class BaseController {
    public HttpServletRequest request;
    public HttpServletResponse response;
    protected String officeId;
    protected String officeName;
    protected String userId;

    @Resource
    public
    MyContext myContext;

    /**
     * 进入控制器之前执行的方法,使用shiro获取
     *
     * @param request  请求
     * @param response 响应
     */
    @ModelAttribute
    public void serResAndReq(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        try {
            //获取session中的安全数据
            Subject subject = SecurityUtils.getSubject();
            //subject获取所有的安全集合
            PrincipalCollection principals = subject.getPrincipals();
            if (principals != null && !principals.isEmpty()) {
                //获取安全数据
                ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
                this.officeId = result.getOfficeId();
                this.officeName = result.getOfficeName();
                this.userId = result.getUserId();
                myContext.setOfficeId(officeId);
                myContext.setUsername(result.getUsername());
            }
        }catch (Exception e){

        }

    }


}
