package com.tpms.tpms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.query.PlanQuery;
import com.tpms.common.web.bean.tp.Plan;
import com.tpms.common.web.bean.tp.PlanCourse;
import com.tpms.common.web.controller.BaseController;
import com.tpms.tpms.service.PlanCourseService;
import com.tpms.tpms.service.PlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wld
 * @date 2023/5/17 - 20:52
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/plan")
public class PlanController extends BaseController {
    @Resource
    private PlanService planService;
    @Resource
    private PlanCourseService planCourseService;

    /**
     * 添加培养计划
     */
    @PostMapping(value = "/")
    public Result addProgram(@RequestBody Plan plan) throws Exception {
        //检查是否已有培养计划
        if (planService.checkYearDuplicate(plan)) {
            return ResultUtil.error("当前专业在当前年份已有培养计划");
        }

        if (planService.checkDuplicate(plan)) {
            plan.setDelFlag("0");
            if (planService.save(plan)) {
                String courseIds = plan.getCourseIds();
                //课程不为空，培养方案添加课程
                String logMsg = "添加培养计划，培养计划ID：" + plan.getPlanId();
                if (StringUtils.isNotBlank(courseIds)) {
                    logMsg += "，课程ID：" + courseIds;
                    String planId = plan.getPlanId();

                    List<PlanCourse> list = new ArrayList<>();
                    for (String courseId : courseIds.split(",")) {
                        PlanCourse tmp = PlanCourse.builder()
                                .planId(planId).courseId(courseId).build();
                        list.add(tmp);
                    }
                    planCourseService.saveBatch(list);
                }
                logOperate("培养计划管理", "ADD", logMsg);
                return ResultUtil.success("添加成功");
            } else {
                return ResultUtil.error("添加失败");
            }
        }
        return ResultUtil.success("培养方案名称已存在");
    }

    /**
     * 修改培养方案
     */
    @PutMapping(value = "/")
    public Result updatePlan(@RequestBody Plan plan) {
        if (planService.checkDuplicate(plan, plan.getPlanId())) {
            if (planService.updateById(plan)) {
                String courseIds = plan.getCourseIds();

                //courseIds如果为pass，跳过修改培养方案课程
                if ("pass".equals(courseIds)) {
                    return ResultUtil.success("修改成功");
                }

                String planId = plan.getPlanId();
                LambdaQueryWrapper<PlanCourse> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(PlanCourse::getPlanId, planId);

                //删除原有课程
                planCourseService.remove(wrapper);
                String logMsg = "修改培养计划，培养计划ID：" +planId;

                //courseIds不为空，进行添加课程
                if (StringUtils.isNotBlank(courseIds)) {
                    logMsg += "，课程ID：" + courseIds;
                    List<PlanCourse> list = new ArrayList<>();
                    for (String courseId : courseIds.split(",")) {
                        PlanCourse tmp = PlanCourse.builder()
                                .planId(planId).courseId(courseId).build();
                        list.add(tmp);
                    }
                    planCourseService.saveBatch(list);
                }
                logOperate("培养计划管理", "UPDATE", logMsg);
                return ResultUtil.success("修改成功");
            } else {
                return ResultUtil.error("修改失败");
            }
        }
        return ResultUtil.success("培养方案名称已存在");
    }


    @PostMapping("/page")
    public Result page(@RequestBody PlanQuery planQuery) {
        Page<Plan> page = new Page<>(planQuery.getPageNum(), planQuery.getPageSize());

        QueryWrapper<Plan> wrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(planQuery.getTheYear())) {
            wrapper.eq("tp.the_year", planQuery.getTheYear());
        }
        if (StringUtils.isNotBlank(planQuery.getMajorId())) {
            wrapper.eq("tp.major_id", planQuery.getMajorId());
        }
        if (StringUtils.isNotBlank(planQuery.getPlanName())) {
            wrapper.like("tp.plan_name", planQuery.getPlanName());
        }
        if (StringUtils.isNotBlank(planQuery.getProgramId())) {
            wrapper.eq("tp.program_id", planQuery.getProgramId());
        }
        wrapper.eq("tp.del_flag", "0");
        wrapper.orderByDesc("tp.modify_time");
        page = planService.getPage(page, wrapper);

        PageResult<Plan> pageBean = PageResult.init(page);

        return ResultUtil.success().buildData("page", pageBean);
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String planId) {
        Plan plan = planService.getById(planId);
        List<Course> courses = planCourseService.getCourses(planId);
        plan.setCourses(courses);
        return ResultUtil.success(planId);
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/{id}")
    public Result delById(@PathVariable("id") String planId) {
        if (planService.removeById(planId)) {
            QueryWrapper<PlanCourse> wrapper = new QueryWrapper<>();
            wrapper.eq("plan_id", planId);
            planCourseService.remove(wrapper);
            String logMsg = "删除培养计划，培养计划ID：" + planId;
            logOperate("培养计划管理","DELETE",logMsg);
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }
}
