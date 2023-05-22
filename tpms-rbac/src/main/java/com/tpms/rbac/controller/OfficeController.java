package com.tpms.rbac.controller;

import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.sys.Office;
import com.tpms.common.web.bean.sys.OperateLog;
import com.tpms.common.web.controller.BaseController;
import com.tpms.rbac.service.OfficeService;
import com.tpms.rbac.service.OperateLogService;
import com.tpms.rbac.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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
        if(officeService.getById(office.getOfficeId())!=null){
            return ResultUtil.error("机构编码已存在");
        }
        if (officeService.save(office)) {
            userService.generateAdmin(office.getOfficeId());
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
    public Result list() {
        List<Office> list = officeService.list();
        return ResultUtil.success().buildData("rows",list);
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
