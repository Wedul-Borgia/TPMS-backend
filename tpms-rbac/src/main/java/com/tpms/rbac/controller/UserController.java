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
import com.tpms.common.web.bean.sys.OperateLog;
import com.tpms.common.web.bean.sys.User;
import com.tpms.common.web.controller.BaseController;
import com.tpms.rbac.service.OfficeService;
import com.tpms.rbac.service.OperateLogService;
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
import java.time.LocalDateTime;
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
            user.setOfficeName(officeName);
            user.setPassword(password);
            user.setDelFlag("0");
            user.setUserStatus("1");
            user.setLevel("0");
            if (userService.save(user)) {
                String logMsg = "添加用户，用户ID：" + user.getUserId();
                logOperate("用户管理", "ADD", logMsg);
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
            String logMsg = "修改用户，用户ID：" + user.getUserId();
            logOperate("用户管理", "UPDATE", logMsg);
            return ResultUtil.success("修改成功");
        }
        return ResultUtil.error("修改失败");
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/{id}")
    public Result delById(@PathVariable("id") String userId) {
        if (userService.removeById(userId)) {
            String logMsg = "删除用户，用户ID：" + userId;
            logOperate("用户管理", "DELETE", logMsg);
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String userId) {
        User user = userService.getById(userId);
        return ResultUtil.success(user);
    }

    @GetMapping("/page")
    public Result page(UserQuery userQuery) {
        Page<User> page = new Page<>(userQuery.getPageNo(), userQuery.getPageSize());

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
        if (StringUtils.isNotBlank(userQuery.getUserStatus())) {
            wrapper.eq(User::getUserStatus, userQuery.getUserStatus());
        }
        wrapper.eq(User::getLevel, "0");
        wrapper.orderByAsc(User::getUsername);
        userService.page(page, wrapper);

        PageResult<User> pageBean = PageResult.init(page);

        return ResultUtil.success(pageBean);
    }

    @GetMapping("/list")
    public Result list(User user) {
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
        if (StringUtils.isNotBlank(user.getUserStatus())) {
            wrapper.eq(User::getUserStatus, user.getUserStatus());
        }
        wrapper.eq(User::getLevel, "0");
        wrapper.orderByAsc(User::getUsername);
        List<User> list = userService.list(wrapper);
        return ResultUtil.success(list);
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
     * 根据id查询
     */
    @GetMapping("/reflash")
    public Result reflash() {
        Subject subject = SecurityUtils.getSubject();
        User current = userService.getById(userId);
        String currentUserName = this.userName;
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        //构造登录令牌
        UsernamePasswordToken upToken = new UsernamePasswordToken(currentUserName, current.getPassword());
        //调用login方法,进入realm完成认证
        subject.login(upToken);
        //获取sessionId
        Object sessionId = subject.getSession().getId();
        //构造返回结果
        return ResultUtil.success(sessionId);
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
        String userId = (String) map.get("userId");
        String oldPassword = (String) map.get("oldPassword");
        String newPassword = (String) map.get("newPassword");

        User user = userService.getById(userId);
        if (user == null) {
            return ResultUtil.error("获取用户异常");
        }

        String password = user.getPassword();

        oldPassword = new Md5Hash(oldPassword, user.getUsername(), 3).toString();

        if (password.equals(oldPassword)) {
            newPassword = new Md5Hash(newPassword, user.getUsername(), 3).toString();
            User update = User.builder().password(newPassword)
                    .userId(user.getUserId()).build();
            if (userService.updateById(update)) {
                String logMsg = "修改用户密码，用户ID：" + user.getUserId();
                logOperate("用户管理", "UPDATE", logMsg);
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
        String userId = (String) map.get("userId");
        User user = userService.getById(userId);
        String password = new Md5Hash("123456", user.getUsername(), 3).toString();
        User update = User.builder().password(password)
                .userId(userId).build();
        if (userService.updateById(update)) {
            String logMsg = "重置用户密码，用户ID：" + userId;
            logOperate("用户管理", "UPDATE", logMsg);
            return ResultUtil.success("密码重置成功");
        } else {
            return ResultUtil.error("密码重置失败");
        }
    }

    @Resource
    private OperateLogService operateLogService;

    private void logOperate(String logModule, String logEvent, String logMsg) {
        operateLogService.save(OperateLog.builder()
                .officeId(this.officeId)
                .officeName(this.officeName)
                .logUser(this.userName)
                .logModule(logModule)
                .logEvent(logEvent)
                .logMessage(this.userName + logMsg)
                .logTime(String.valueOf(LocalDateTime.now()))
                .build());
    }

}
