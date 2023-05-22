package com.tpms.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.base.feign.LogFeignService;
import com.tpms.base.service.MajorService;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.base.Major;
import com.tpms.common.web.bean.query.MajorQuery;
import com.tpms.common.web.bean.sys.OperateLog;
import com.tpms.common.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author wld
 * @date 2023/5/16 - 15:02
 */
@CrossOrigin
@RestController
@RequestMapping("/major")
public class MajorController extends BaseController {
    @Resource
    private MajorService majorService;

    @PostMapping("/")
    public Result add(@RequestBody Major major) {

        if (majorService.checkDuplicate(major)) {
            major.setDelFlag("0");
            major.setIsStop("0");
            if (majorService.save(major)) {
                String logMsg = "添加专业，专业ID：" + major.getMajorId();
                logOperate("专业管理", "ADD", logMsg);
                return ResultUtil.success("添加成功");
            } else {
                return ResultUtil.error("添加失败");
            }
        } else {
            return ResultUtil.error("专业名称或编码已存在");
        }

    }

    /**
     * 修改专业
     */
    @PutMapping(value = "/")
    public Result update(@RequestBody Major major) {

        if (majorService.checkDuplicate(major, major.getMajorId())) {
            if (majorService.updateById(major)) {
                String logMsg = "修改专业，专业ID：" + major.getMajorId();
                logOperate("专业管理", "UPDATE", logMsg);
                return ResultUtil.success("修改成功");
            } else {
                return ResultUtil.error("修改失败");
            }
        }
        return ResultUtil.success("专业名称或编码已存在");
    }

    /**
     * 启用停用
     */
    @PutMapping(value = "/stop")
    public Result updateStop(@RequestBody Map<String, Object> map) {
        String majorId = (String) map.get("majorId");
        String isStop = (String) map.get("isStop");

        if (StringUtils.isNotBlank(majorId) && StringUtils.isNotBlank(isStop)) {
            Major update = Major.builder()
                    .majorId(majorId).isStop(isStop).build();

            if (majorService.updateById(update)) {
                String action = "1".equals(isStop) ? "停用" : "启用";
                String logMsg = action + "专业，专业ID：" + majorId;
                logOperate("专业管理", "UPDATE", logMsg);
                return ResultUtil.success(action + "成功");
            }
        }
        return ResultUtil.error("操作失败");
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/{id}")
    public Result delById(@PathVariable("id") String majorId) {
        if (majorService.removeById(majorId)) {
            String logMsg = "删除专业，专业ID：" + majorId;
            logOperate("专业管理", "DELETE", logMsg);
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String majorId) {
        Major major = majorService.getById(majorId);
        return ResultUtil.success(major);
    }

    @PostMapping("/page")
    public Result page(@RequestBody MajorQuery majorQuery) {
        Page<Major> page = new Page<>(majorQuery.getPageNum(), majorQuery.getPageSize());

        QueryWrapper<Major> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(majorQuery.getMajorName())) {
            wrapper.like("bm.major_name", majorQuery.getMajorName());
        }
        if (StringUtils.isNotBlank(majorQuery.getMajorCode())) {
            wrapper.like("bm.major_code", majorQuery.getMajorCode());
        }
        if (StringUtils.isNotBlank(majorQuery.getIsStop())) {
            wrapper.eq("bm.is_stop", majorQuery.getIsStop());
        }
        if (StringUtils.isNotBlank(majorQuery.getCollegeId())) {
            wrapper.eq("bc.college_id", majorQuery.getCollegeId());
        }
        wrapper.eq("bm.del_flag", "0");
        wrapper.orderByAsc("bm.major_code");
        page = majorService.getPage(page, wrapper);
        PageResult<Major> pageBean = PageResult.init(page);

        return ResultUtil.success().buildData("page", pageBean);
    }

    @PostMapping("/list")
    public Result list(@RequestBody MajorQuery majorQuery) {
        QueryWrapper<Major> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(majorQuery.getMajorName())) {
            wrapper.like("bm.major_name", majorQuery.getMajorName());
        }
        if (StringUtils.isNotBlank(majorQuery.getMajorCode())) {
            wrapper.like("bm.major_code", majorQuery.getMajorCode());
        }
        wrapper.eq("bm.is_stop", "0");
        wrapper.eq("bm.del_flag", "0");
        wrapper.orderByAsc("bm.major_code");
        List<Major> list = majorService.getList(wrapper);
        return ResultUtil.success().buildData("rows", list);
    }

    @Resource
    private LogFeignService logFeignService;
    private void logOperate(String logModule,String logEvent,String logMsg){
        logFeignService.log(OperateLog.builder()
                .officeId(this.officeId)
                .officeName(this.officeName)
                .logUser(this.userName)
                .logModule(logModule)
                .logEvent(logEvent)
                .logMessage(this.userName+logMsg)
                .build());
    }

}
