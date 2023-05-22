package com.tpms.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.ProfileResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.query.UserQuery;
import com.tpms.common.web.bean.sys.Office;
import com.tpms.common.web.bean.sys.User;
import com.tpms.common.web.controller.BaseController;
import com.tpms.rbac.service.OfficeService;
import com.tpms.rbac.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author wld
 * @date 2023/5/3 - 20:17
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private OfficeService officeService;

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @PostMapping("/")
    public Result add(@RequestBody User user) {

        if (userService.checkDuplicate(user)) {
            String password = user.getPassword();
            if (StringUtils.isBlank(password)) {
                password = "123456";
            }
            password = new Md5Hash(password, user.getUsername(), 3).toString();
            user.setPassword(password);
            user.setDelFlag("0");
            user.setUserStatus("1");
            user.setLevel("0");
            if (userService.save(user)) {
                return ResultUtil.success("添加成功");
            } else {
                return ResultUtil.error("添加失败");
            }
        } else {
            return ResultUtil.error("用户名已存在");
        }
    }

    /**
     * 修改User
     */
    @PutMapping(value = "/")
    public Result update(@RequestBody User user) {
        //调用Service更新
        if (userService.updateById(user)) {
            return ResultUtil.success("修改成功");
        }
        return ResultUtil.error("修改失败");
    }

    @PostMapping("/page")
    public Result page(@RequestBody UserQuery userQuery) {
        Page<User> page = new Page<>(userQuery.getPageNum(), userQuery.getPageSize());

        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(userQuery.getUsername())) {
            wrapper.like(User::getUsername, userQuery.getUsername());
        }
        if (StringUtils.isNotBlank(userQuery.getTrueName())) {
            wrapper.like(User::getTrueName, userQuery.getTrueName());
        }
        if (StringUtils.isNotBlank(userQuery.getUserSex())) {
            wrapper.eq(User::getUserSex, userQuery.getUserSex());
        }
        wrapper.orderByAsc(User::getUsername);
        userService.page(page, wrapper);

        PageResult<User> pageBean = PageResult.init(page);

        return ResultUtil.success().buildData("page", pageBean);
    }

    @PostMapping("/list")
    public Result list(@RequestBody User user) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(user.getUsername())) {
            wrapper.like(User::getUsername, user.getUsername());
        }
        if (StringUtils.isNotBlank(user.getTrueName())) {
            wrapper.like(User::getTrueName, user.getTrueName());
        }
        if (StringUtils.isNotBlank(user.getUserSex())) {
            wrapper.eq(User::getUserSex, user.getUserSex());
        }
        wrapper.orderByAsc(User::getUsername);
        List<User> list = userService.list(wrapper);
        return ResultUtil.success().buildData("rows", list);
    }

    /**
     * 用户登录
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody Map<String, Object> loginMap) {
        String username = (String) loginMap.get("username");
        String password = (String) loginMap.get("password");
        String officeId = (String) loginMap.get("officeId");
        try {
            Office office = officeService.getById(officeId);
            //单位已停用，直接返回
            if ("1".equals(office.getIsStop())) {
                return ResultUtil.error("您登录的单位目前已停用，请等待通知");
            }
            myContext.setOfficeId(officeId);
            //单位已停用，直接返回
            LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(User::getUsername, username);
            User user = userService.getOne(wrapper);
            if ("0".equals(user.getUserStatus())) {
                return ResultUtil.error("您的用户已被锁定，暂时无法登录");
            }
            //构造登录令牌
            password = new Md5Hash(password, username, 3).toString();
            UsernamePasswordToken upToken = new UsernamePasswordToken(username, password);
            //获取subject
            Subject subject = SecurityUtils.getSubject();
            //调用login方法,进入realm完成认证
            subject.login(upToken);
            //获取sessionId
            Object sessionId = subject.getSession().getId();
            //构造返回结果
            return ResultUtil.success(sessionId);
        } catch (Exception e) {
            return ResultUtil.error("用户名或密码错误");
        }
    }

    /**
     * 用户登录成功之后,获取用户信息
     */
    @PostMapping(value = "/profile")
    public Result profile() {
        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //subject获取所有的安全集合
        PrincipalCollection principals = subject.getPrincipals();
        //获取安全数据
        ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
        return ResultUtil.success(result);
    }

    /**
     * 用户登录
     */
    @RequiresAuthentication
    @GetMapping(value = "/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return ResultUtil.success("退出登录");
    }

    @PutMapping("/password")
    public Result updatePwd(@RequestBody Map<String, Object> map) {
        String username = (String) map.get("username");
        String oldPassword = (String) map.get("oldPassword");
        String newPassword = (String) map.get("newPassword");

        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getUsername, username);
        User user = userService.getOne(wrapper);
        if (user == null) {
            return ResultUtil.error("获取用户异常");
        }

        String password = user.getPassword();

        oldPassword = new Md5Hash(oldPassword, username, 3).toString();

        if (password.equals(oldPassword)) {
            newPassword = new Md5Hash(newPassword, username, 3).toString();
            User update = User.builder().password(newPassword)
                    .userId(user.getUserId()).build();
            if (userService.updateById(update)) {
                return ResultUtil.success("密码修改成功");
            } else {
                return ResultUtil.error("密码修改失败");
            }
        } else {
            return ResultUtil.error("原密码不正确");
        }

    }

    @PostMapping("/password/reset")
    public Result resetPwd(@RequestBody Map<String, Object> map) {
        String username = (String) map.get("username");
        String userId = (String) map.get("userId");
        String password = new Md5Hash("123456", username, 3).toString();
        User update = User.builder().password(password)
                .userId(userId).build();
        if (userService.updateById(update)) {
            return ResultUtil.success("密码重置成功");
        } else {
            return ResultUtil.error("密码重置失败");
        }
    }

}
