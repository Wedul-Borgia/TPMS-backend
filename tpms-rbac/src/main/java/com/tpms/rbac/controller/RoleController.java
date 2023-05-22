package com.tpms.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.query.RoleQuery;
import com.tpms.common.web.bean.sys.Power;
import com.tpms.common.web.bean.sys.Role;
import com.tpms.common.web.controller.BaseController;
import com.tpms.rbac.entity.RolePower;
import com.tpms.rbac.entity.UserRole;
import com.tpms.rbac.service.PowerService;
import com.tpms.rbac.service.RolePowerService;
import com.tpms.rbac.service.RoleService;
import com.tpms.rbac.service.UserRoleService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wld
 * @date 2023/5/13 - 21:24
 */
//解决跨域
@CrossOrigin
@RestController
@RequestMapping(value = "/role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;
    @Resource
    private PowerService powerService;
    @Resource
    private RolePowerService rolePowerService;

    /**
     * 分配权限
     */
    @PutMapping(value = "/assignPrem")
    public Result save(@RequestBody Map<String, Object> map) {

        //获取被分配的角色id
        String roleId = (String) map.get("roleId");
        //获取到权限的id列表
        List<String> powerIds = (List<String>) map.get("powerIds");
        //调用service完成权限分配
        roleService.assignPowers(roleId, powerIds);
        String logMsg = "修改角色的权限，角色ID："+roleId;
        logOperate("角色管理","UPDATE",logMsg);
        return ResultUtil.success();
    }

    /**
     * 添加角色
     */
    @PostMapping(value = "/")
    public Result addRole(@RequestBody Role role) throws Exception {
        if (roleService.checkDuplicate(role)) {
            role.setDelFlag("0");
            if (roleService.save(role)) {
                String powerIds = role.getPowerIds();
                String roleId = role.getRoleId();
                if (StringUtils.isNotBlank(powerIds)) {
                    List<RolePower> list = new ArrayList<>();
                    for (String powerId : powerIds.split(",")) {
                        RolePower tmp = RolePower.builder()
                                .roleId(roleId).powerId(powerId).build();
                        list.add(tmp);
                    }
                    rolePowerService.saveBatch(list);
                }
                String logMsg = "添加角色，角色ID："+roleId;
                logOperate("角色管理","ADD",logMsg);
                return ResultUtil.success("添加成功");
            } else {
                return ResultUtil.error("添加失败");
            }
        }
        return ResultUtil.success("角色名称已存在");
    }

    /**
     * 添加权限
     */
    @PostMapping(value = "/power")
    public Result addPower(@RequestBody Power power) throws Exception {
        if (powerService.checkDuplicate(power)) {
            power.setDelFlag("0");
            if (powerService.save(power)) {
                String logMsg = "添加权限，权限ID："+power.getPowerId();
                logOperate("权限管理","ADD",logMsg);
                return ResultUtil.success("添加成功");
            } else {
                return ResultUtil.error("添加失败");
            }
        }
        return ResultUtil.success("权限名称或编码已存在");
    }

    /**
     * 根据ID获取角色信息
     */
    @GetMapping(value = "/{id}")
    public Result findRoleById(@PathVariable(name = "id") String roleId) throws Exception {
        Role role = roleService.getById(roleId);
        return ResultUtil.success(role);
    }

    /**
     * 根据ID获取权限信息
     */
    @GetMapping(value = "/power/{id}")
    public Result findPowerById(@PathVariable(name = "id") String powerId) throws Exception {
        Power power = powerService.getById(powerId);
        return ResultUtil.success(power);
    }

    /**
     * 根据用户ID获取全部权限
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/userId/{id}")
    public Result findRolesByUserId(@PathVariable(name = "id") String userId) {
        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRole::getUserId, userId);
        List<UserRole> roleIdList = userRoleService.list();
        if (!ObjectUtils.isEmpty(roleIdList)) {
            List<Role> roles = roleService.getByRoleIds(roleIdList);
            if (!ObjectUtils.isEmpty(roles)) {
                return ResultUtil.success(roles);
            }
        }
        return ResultUtil.success();
    }

    /**
     * 修改角色
     */
    @PutMapping(value = "/")
    public Result updateRole(@RequestBody Role role) {
        if (roleService.checkDuplicate(role, role.getRoleId())) {
            if (roleService.updateById(role)) {
                String logMsg = "修改角色，角色ID："+role.getRoleId();
                logOperate("角色管理","UPDATE",logMsg);
                return ResultUtil.success("修改成功");
            } else {
                return ResultUtil.error("修改失败");
            }
        }
        return ResultUtil.success("角色名称已存在");
    }

    /**
     * 修改权限
     */
    @PutMapping(value = "/power")
    public Result updatePower(@RequestBody Power power) {
        if (powerService.checkDuplicate(power, power.getPowerId())) {
            if (powerService.updateById(power)) {
                String logMsg = "修改权限，权限ID："+power.getPowerId();
                logOperate("权限管理","UPDATE",logMsg);
                return ResultUtil.success("修改成功");
            } else {
                return ResultUtil.error("修改失败");
            }
        }
        return ResultUtil.success("权限名称或编码已存在");
    }

    /**
     * 修改用户角色
     */
    @PutMapping(value = "/putRoles")
    public Result updateRoles(@RequestBody Map<String, Object> map) {

        String userId = (String) map.get("userId");
        String roleIds = (String) map.get("roleIds");

        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRole::getUserId, userId);

        //删除原有角色
        userRoleService.remove(wrapper);

        String logMsg = "修改用户角色，用户ID："+userId;
        if (StringUtils.isNotBlank(roleIds)) {
            logMsg += "，角色ID："+roleIds;
            List<UserRole> list = new ArrayList<>();
            for (String roleId : roleIds.split(",")) {
                UserRole userRole = UserRole.builder()
                        .userId(userId).roleId(roleId).build();
                list.add(userRole);
            }
            //重新插入角色
            userRoleService.saveBatch(list);
        }
        logOperate("角色管理","UPDATE",logMsg);
        return ResultUtil.success("操作成功");
    }

    /**
     * 修改角色权限
     */
    @PutMapping(value = "/putPowers")
    public Result updatePowers(@RequestBody Map<String, Object> map) {

        String roleId = (String) map.get("roleId");
        String powerIds = (String) map.get("powerIds");

        LambdaQueryWrapper<RolePower> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RolePower::getRoleId, roleId);

        //删除原有权限
        rolePowerService.remove(wrapper);
        String logMsg = "修改角色权限，角色ID："+roleId;

        if (StringUtils.isNotBlank(powerIds)) {
            logMsg += "，权限ID："+powerIds;
            List<RolePower> list = new ArrayList<>();
            for (String powerId : powerIds.split(",")) {
                RolePower rolePower = RolePower.builder()
                        .roleId(roleId).powerId(powerId).build();
                list.add(rolePower);
            }
            //重新插入权限
            rolePowerService.saveBatch(list);
        }
        logOperate("角色管理","UPDATE",logMsg);
        return ResultUtil.success("操作成功");
    }

    /**
     * 根据id删除角色
     */
    @DeleteMapping("/{id}")
    public Result delRoleById(@PathVariable("id") String roleId){
        if(roleService.removeById(roleId)){
            String logMsg = "删除角色，角色ID："+roleId;
            logOperate("角色管理","DELETE",logMsg);
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }

    /**
     * 根据id删除权限
     */
    @DeleteMapping("/power/{id}")
    public Result delPowerById(@PathVariable("id") String powerId){
        if(powerService.removeById(powerId)){
            String logMsg = "删除权限，权限ID："+powerId;
            logOperate("权限管理","DELETE",logMsg);
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }


    /**
     * 获取角色列表
     * @param role
     * @return
     */
    @PostMapping("/list")
    public Result list(@RequestBody Role role) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(role.getRoleName())) {
            wrapper.like(Role::getRoleName, role.getRoleName());
        }
        wrapper.orderByAsc(Role::getModifyTime);
        List<Role> list = roleService.list(wrapper);
        return ResultUtil.success().buildData("rows", list);
    }

    /**
     * 获取角色分页列表
     * @param roleQuery
     * @return
     */
    @PostMapping("/page")
    public Result page(@RequestBody RoleQuery roleQuery) {
        Page<Role> page = new Page<>(roleQuery.getPageNum(), roleQuery.getPageSize());
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(roleQuery.getRoleName())) {
            wrapper.like(Role::getRoleName, roleQuery.getRoleName());
        }
        if (StringUtils.isNotBlank(roleQuery.getRemark())) {
            wrapper.like(Role::getRemark, roleQuery.getRemark());
        }
        wrapper.orderByAsc(Role::getModifyTime);
        page = roleService.page(page,wrapper);
        PageResult<Role> pageBean = PageResult.init(page);
        return ResultUtil.success().buildData("page", pageBean);
    }

    @PostMapping("/power/list")
    public Result powerList(@RequestBody Power power) {
        LambdaQueryWrapper<Power> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(power.getPowerName())) {
            wrapper.like(Power::getPowerName, power.getPowerName());
        }
        wrapper.orderByAsc(Power::getModifyTime);
        List<Power> list = powerService.list(wrapper);
        return ResultUtil.success().buildData("rows", list);
    }
}
