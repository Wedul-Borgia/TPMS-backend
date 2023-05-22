package com.tpms.rbac.controller;

import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.sys.Office;
import com.tpms.common.web.controller.BaseController;
import com.tpms.rbac.service.OfficeService;
import com.tpms.rbac.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @PostMapping("/add")
    public Result add(@RequestBody Office office) {
        if(officeService.getById(office.getOfficeId())!=null){
            return ResultUtil.error("机构编码已存在");
        }
        if (officeService.save(office)) {
            userService.generateAdmin(office.getOfficeId());
            return ResultUtil.success();
        } else {
            return ResultUtil.error();
        }
    }

    @GetMapping("/list")
    public Result list() {
        List<Office> list = officeService.list();
        return ResultUtil.success().buildData("rows",list);
    }


}
