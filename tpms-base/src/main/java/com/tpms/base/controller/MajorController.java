package com.tpms.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.base.service.MajorService;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.base.Major;
import com.tpms.common.web.bean.query.MajorQuery;
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

        if(majorService.checkDuplicate(major)){
            major.setDelFlag("0");
            major.setIsStop("0");
            boolean res = majorService.save(major);
            if (res) {
                return ResultUtil.success("添加成功");
            } else {
                return ResultUtil.error("添加失败");
            }
        }else{
            return ResultUtil.error("专业名称或编码已存在");
        }

    }

    /**
     * 修改专业
     */
    @PutMapping(value = "/")
    public Result update(@RequestBody Major major) {

        if (majorService.checkDuplicate(major,major.getMajorId())) {
            if (majorService.updateById(major)) {
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
                return ResultUtil.success("1".equals(isStop)?"停用成功":"启用成功");
            }
        }
        return ResultUtil.error("操作失败");
    }

    @PostMapping("/page")
    public Result page(@RequestBody MajorQuery majorQuery) {
        Page<Major> page = new Page<>(majorQuery.getPageNum(), majorQuery.getPageSize());

        QueryWrapper<Major> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(majorQuery.getMajorName())){
            wrapper.like("bm.major_name",majorQuery.getMajorName());
        }
        if(StringUtils.isNotBlank(majorQuery.getMajorCode())){
            wrapper.like("bm.major_code",majorQuery.getMajorCode());
        }
        if(StringUtils.isNotBlank(majorQuery.getIsStop())){
            wrapper.eq("bm.is_stop", majorQuery.getIsStop());
        }
        if(StringUtils.isNotBlank(majorQuery.getCollegeId())){
            wrapper.eq("bc.college_id",majorQuery.getCollegeId());
        }
        wrapper.eq("bm.del_flag", "0");
        wrapper.orderByAsc("bm.major_code");
        page = majorService.getPage(page,wrapper);
        PageResult<Major> pageBean = PageResult.init(page);

        return ResultUtil.success().buildData("page", pageBean);
    }

    @PostMapping("/list")
    public Result list(@RequestBody MajorQuery majorQuery) {
        QueryWrapper<Major> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(majorQuery.getMajorName())) {
            wrapper.like("bm.major_name",majorQuery.getMajorName());
        }
        if (StringUtils.isNotBlank(majorQuery.getMajorCode())) {
            wrapper.like("bm.major_code",majorQuery.getMajorCode());
        }
        wrapper.eq("bm.is_stop","0");
        wrapper.eq("bm.del_flag", "0");
        wrapper.orderByAsc("bm.major_code");
        List<Major> list = majorService.getList(wrapper);
        return ResultUtil.success().buildData("rows",list);
    }

}
