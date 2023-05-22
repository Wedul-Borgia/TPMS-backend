package com.tpms.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.base.service.CollegeService;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.base.College;
import com.tpms.common.web.bean.query.CollegeQuery;
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
@RequestMapping("/college")
public class CollegeController extends BaseController {
    @Resource
    private CollegeService collegeService;

    /**
     * 添加学院
     */
    @PostMapping("/")
    public Result add(@RequestBody College college) {

        if (collegeService.checkDuplicate(college)) {
            college.setDelFlag("0");
            college.setIsStop("0");
            boolean res = collegeService.save(college);
            if (res) {
                return ResultUtil.success("添加成功");
            } else {
                return ResultUtil.error("添加失败");
            }
        } else {
            return ResultUtil.error("院校名称或编码已存在");
        }

    }

    /**
     * 修改学院
     */
    @PutMapping(value = "/")
    public Result update(@RequestBody College college) {

        if (collegeService.checkDuplicate(college, college.getCollegeId())) {
            if (collegeService.updateById(college)) {
                return ResultUtil.success("修改成功");
            } else {
                return ResultUtil.error("修改失败");
            }
        }
        return ResultUtil.success("学院名称或编码已存在");
    }

    /**
     * 启用停用
     */
    @PutMapping(value = "/stop")
    public Result updateStop(@RequestBody Map<String, Object> map) {
        String collegeId = (String) map.get("collegeId");
        String isStop = (String) map.get("isStop");

        if (StringUtils.isNotBlank(collegeId) && StringUtils.isNotBlank(isStop)) {
            College update = College.builder()
                    .collegeId(collegeId).isStop(isStop).build();

            if (collegeService.updateById(update)) {
                return ResultUtil.success("1".equals(isStop)?"停用成功":"启用成功");
            }
        }
        return ResultUtil.error("操作失败");
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/{id}")
    public Result delById(@PathVariable("id") String collegeId){
        if(collegeService.removeById(collegeId)){
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String collegeId) {
        College college = collegeService.getById(collegeId);
        return ResultUtil.success(college);
    }

    /**
     * 分页查询
     * @param collegeQuery
     * @return
     */
    @PostMapping("/page")
    public Result page(@RequestBody CollegeQuery collegeQuery) {
        Page<College> page = new Page<>(collegeQuery.getPageNum(), collegeQuery.getPageSize());

        LambdaQueryWrapper<College> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(collegeQuery.getCollegeName())) {
            wrapper.like(College::getCollegeName, collegeQuery.getCollegeName());
        }
        if (StringUtils.isNotBlank(collegeQuery.getCollegeCode())) {
            wrapper.like(College::getCollegeCode, collegeQuery.getCollegeCode());
        }
        if (StringUtils.isNotBlank(collegeQuery.getIsStop())) {
            wrapper.eq(College::getIsStop, collegeQuery.getIsStop());
        }
        wrapper.orderByAsc(College::getCollegeCode);
        collegeService.page(page, wrapper);

        PageResult<College> pageBean = PageResult.init(page);

        return ResultUtil.success().buildData("page", pageBean);
    }

    /**
     * 条件查询
     * @param collegeQuery
     * @return
     */
    @PostMapping("/list")
    public Result list(@RequestBody CollegeQuery collegeQuery) {
        LambdaQueryWrapper<College> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(collegeQuery.getCollegeName())) {
            wrapper.like(College::getCollegeName, collegeQuery.getCollegeName());
        }
        if (StringUtils.isNotBlank(collegeQuery.getCollegeCode())) {
            wrapper.like(College::getCollegeCode, collegeQuery.getCollegeCode());
        }
        wrapper.eq(College::getIsStop, "0")
                .orderByAsc(College::getCollegeCode);
        List<College> list = collegeService.list(wrapper);
        return ResultUtil.success().buildData("rows",list);
    }
}
