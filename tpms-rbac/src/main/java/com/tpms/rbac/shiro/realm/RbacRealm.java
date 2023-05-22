package com.tpms.rbac.shiro.realm;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tpms.common.web.bean.ProfileResult;
import com.tpms.common.web.bean.sys.Power;
import com.tpms.common.web.bean.sys.User;
import com.tpms.common.web.constant.Constant;
import com.tpms.common.web.shiro.realm.TpRealm;
import com.tpms.rbac.service.PowerService;
import com.tpms.rbac.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wld
 * @date 2023/5/13 - 20:43
 */
public class RbacRealm extends TpRealm {
    @Resource
    private UserService userService;

    @Resource
    private PowerService powerService;

    /**
     * 认证方法
     *
     * @param authenticationToken 权限token
     * @return 权限token对应的权限Info
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        //获取用户名和密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username = upToken.getUsername();
        String password = new String(upToken.getPassword());
        //根据用户名查询用户
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, username);
        User user = userService.getOne(wrapper);
        //根据用户是否存在,用户密码是否和输入密码一致
        if (user != null && user.getPassword().equals(password)) {
            //构造安全数据并返回(安全数据：用户基本信息,权限信息,ProfileResult)
            userService.updateLogintime(user.getUserId(),LocalDateTime.now(),user.getOfficeId());
            ProfileResult result;
            //如果是员工,就把员工的信息保存
            if (Constant.UserLevel.USER.equals(user.getLevel())) {
                List<Power> list = powerService.findUserPower(user.getUserId());
                result = new ProfileResult(user,list);
            } else {
                Map<String, Object> map = new HashMap<>(1);
                //如果是机构管理员,就查询机构管理员可见的
                if (Constant.UserLevel.ADMIN.equals(user.getLevel())) {
                    map.put("level", Constant.PowerLevel.ADMIN);
                } else if (Constant.UserLevel.SAASADMIN.equals(user.getLevel())) {
                    //如果是SaaS管理员，只显示SaaS管理员可见的
                    map.put("level", Constant.PowerLevel.SAASADMIN);
                }
                map.put("officeId", user.getOfficeId());
                List<Power> list = powerService.findAll(map);
                result = new ProfileResult(user, list);
            }


            //构造方法：安全数据,密码,realm域名
            return new SimpleAuthenticationInfo(result, user.getPassword(), this.getName());
        }
        //返回null,会抛出异常,表示用户名和密码不匹配
        return null;
    }
}
