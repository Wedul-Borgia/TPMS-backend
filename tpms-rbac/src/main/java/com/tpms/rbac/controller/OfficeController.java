package com.tpms.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.query.OfficeQuery;
import com.tpms.common.web.bean.sys.Office;
import com.tpms.common.web.bean.sys.OperateLog;
import com.tpms.common.web.controller.BaseController;
import com.tpms.rbac.service.OfficeService;
import com.tpms.rbac.service.OperateLogService;
import com.tpms.rbac.service.UserService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/office")
public class OfficeController extends BaseController {
    @Resource
    private OfficeService officeService;

    @Resource
    private UserService userService;

    /**
     * 添加机构
     * @param office
     * @return
     */
    @PostMapping("/")
    public Result add(@RequestBody Office office) {
        office.setOfficeId(office.getOfficeCode());
        if(officeService.getById(office.getOfficeId())!=null){
            return ResultUtil.error("机构编码已存在");
        }
        if (officeService.save(office)) {
            userService.generateAdmin(office.getOfficeId(),office.getOfficeName());
            String logMsg = "添加机构，机构ID："+office.getOfficeId();
            logOperate("机构管理","ADD",logMsg);
            return ResultUtil.success();
        } else {
            return ResultUtil.error();
        }
    }

    /**
     * 修改机构
     * @param office
     * @return
     */
    @PutMapping("/")
    public Result update(@RequestBody Office office) {
        //调用Service更新
        if (officeService.updateById(office)) {
            String logMsg = "修改机构，机构ID："+office.getOfficeId();
            logOperate("机构管理","UPDATE",logMsg);
            return ResultUtil.success("修改成功");
        }
        return ResultUtil.error("修改失败");
    }

    @GetMapping("/list")
    public Result list(Office office) {
        LambdaQueryWrapper<Office> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(office.getOfficeName())) {
            wrapper.like(Office::getOfficeName,office.getOfficeName());
        }
        if (StringUtils.isNotBlank(office.getOfficeCode())) {
            wrapper.like(Office::getOfficeCode, office.getOfficeCode());
        }
        if (StringUtils.isNotBlank(office.getIsStop())) {
            wrapper.like(Office::getIsStop, office.getIsStop());
        }
        wrapper.orderByAsc(Office::getOfficeCode);
        List<Office> list = officeService.list(wrapper);
        return ResultUtil.success().buildData("rows",list);
    }

    /**
     * 获取机构分页列表
     * @param officeQuery
     * @return
     */
    @GetMapping("/page")
    public Result page(OfficeQuery officeQuery) {
        Page<Office> page = new Page<>(officeQuery.getPageNo(), officeQuery.getPageSize());
        LambdaQueryWrapper<Office> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(officeQuery.getOfficeName())) {
            wrapper.like(Office::getOfficeName, officeQuery.getOfficeName());
        }
        if (StringUtils.isNotBlank(officeQuery.getOfficeCode())) {
            wrapper.like(Office::getOfficeCode, officeQuery.getOfficeCode());
        }
        if (StringUtils.isNotBlank(officeQuery.getIsStop())) {
            wrapper.like(Office::getIsStop, officeQuery.getIsStop());
        }
        wrapper.orderByAsc(Office::getOfficeCode);
        page = officeService.page(page,wrapper);
        PageResult<Office> pageBean = PageResult.init(page);
        return ResultUtil.success(pageBean);
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/{id}")
    public Result delById(@PathVariable("id") String officeId){
        if(officeService.removeById(officeId)){
            String logMsg = "删除机构，机构ID："+officeId;
            logOperate("机构管理","DELETE",logMsg);
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String officeId) {
        Office office = officeService.getById(officeId);
        return ResultUtil.success(office);
    }

    /**
     * 启用停用
     */
    @PutMapping(value = "/stop")
    public Result updateStop(@RequestBody Map<String, Object> map) {
        String officeId = (String) map.get("officeId");
        String isStop = (String) map.get("isStop");

        if (StringUtils.isNotBlank(officeId) && StringUtils.isNotBlank(isStop)) {
            Office update = Office.builder()
                    .officeId(officeId).isStop(isStop).build();

            if (officeService.updateById(update)) {
                String action = "1".equals(isStop) ? "停用" : "启用";
                String logMsg = action + "机构，机构ID：" + officeId;
                logOperate("机构管理", "UPDATE", logMsg);
                return ResultUtil.success(action + "成功");
            }
        }
        return ResultUtil.error("操作失败");
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
