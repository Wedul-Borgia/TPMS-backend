package com.tpms.tpms.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.*;
import com.deepoove.poi.util.PoitlIOUtils;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.query.PlanQuery;
import com.tpms.common.web.bean.sys.OperateLog;
import com.tpms.common.web.bean.tp.Plan;
import com.tpms.common.web.bean.tp.PlanCourse;
import com.tpms.common.web.controller.BaseController;
import com.tpms.tpms.feign.LogFeignService;
import com.tpms.tpms.service.PlanCourseService;
import com.tpms.tpms.service.PlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (!planService.checkYearDuplicate(plan)) {
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
                                .planId(planId).courseId(courseId)
                                .theYear(plan.getTheYear())
                                .majorId(plan.getMajorId()).build();
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
                String logMsg = "修改培养计划，培养计划ID：" + planId;

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
        Page<Plan> page = new Page<>(planQuery.getPageNo(), planQuery.getPageSize());

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
        if (StringUtils.isNotBlank(planQuery.getStatus())) {
            wrapper.eq("tp.status", planQuery.getStatus());
        }
        wrapper.eq("tp.del_flag", "0");
        wrapper.orderByDesc("tp.modify_time");
        page = planService.getPage(page, wrapper);

        PageResult<Plan> pageBean = PageResult.init(page);

        return ResultUtil.success(pageBean);
    }

    @PostMapping("/approve/page")
    public Result approvePage(@RequestBody PlanQuery planQuery) {
        Page<Plan> page = new Page<>(planQuery.getPageNo(), planQuery.getPageSize());

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
        if (StringUtils.isNotBlank(planQuery.getStatus())) {
            wrapper.eq("tp.status", planQuery.getStatus());
        }else {
            wrapper.ge("tp.status", "1");
        }
        wrapper.eq("tp.del_flag", "0");
        wrapper.orderByDesc("tp.modify_time");
        page = planService.getPage(page, wrapper);

        PageResult<Plan> pageBean = PageResult.init(page);

        return ResultUtil.success(pageBean);
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String planId) {
        Plan plan = planService.getByPlanId(planId);
        List<Course> courses = planCourseService.getCourses(planId);
        plan.setCourses(courses);
        return ResultUtil.success(plan);
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
            logOperate("培养计划管理", "DELETE", logMsg);
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }

    @PutMapping("/approve/")
    public Result updateStatus(@RequestBody Plan plan) {
        if(planService.updateById(plan)){
            return ResultUtil.success("操作成功");
        }
        return ResultUtil.error("操作失败");
    }

    /**
     * 导出文件
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable("id") String planId, HttpServletResponse response) throws IOException {

        Plan plan = planService.getByPlanId(planId);
        if (plan == null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "培养计划不存在");
            response.getWriter().println(JSONUtils.toJSONString(map));
            return;
        }

        try {
            response.setContentType("application/octet-stream");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode(plan.getPlanName(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".docx");
            OutputStream out = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);

            //填入数据
            Map<String, Object> data = new HashMap<>();

            //表格宽度
            int[] width = {20, 40, 10, 10, 20};
            //表头和空行
            RowRenderData header = Rows.create("课程编号", "课程名称", "学分", "学时", "执行学期");
            RowRenderData none = Rows.create("无", null, null, null, null);
            //合并单元格
            MergeCellRule rule = MergeCellRule.builder()
                    .map(MergeCellRule.Grid.of(1, 0), MergeCellRule.Grid.of(1, 3))
                    .build();

            //表格
            TableRenderData course0 = Tables.of(header).percentWidth("100%", width)
                    .center().create();
            TableRenderData course1 = Tables.of(header).percentWidth("100%", width)
                    .center().create();
            TableRenderData course2 = Tables.of(header).percentWidth("100%", width)
                    .center().create();
            TableRenderData course3 = Tables.of(header).percentWidth("100%", width)
                    .center().create();

            List<Course> courses = planCourseService.getCourses(planId);
            if (courses != null && courses.size() > 0) {
                for (Course course : courses) {
                    String courseType = course.getCourseType();
                    RowRenderData row = Rows.create(course.getCourseCode(), course.getCourseName(),
                            String.valueOf(course.getCredit()),String.valueOf(course.getXueshi()), null);
                    if ("0".equals(courseType)) {
                        course0.addRow(row);
                    } else if ("1".equals(courseType)) {
                        course1.addRow(row);
                    } else if ("2".equals(courseType)) {
                        course2.addRow(row);
                    } else if ("3".equals(courseType)) {
                        course3.addRow(row);
                    }
                }
            }

            if (course0.getRows().size() < 1) {
                course0.addRow(none);
                course0.setMergeRule(rule);
            }
            if (course1.getRows().size() < 1) {
                course1.addRow(none);
                course1.setMergeRule(rule);
            }
            if (course2.getRows().size() < 1) {
                course2.addRow(none);
                course2.setMergeRule(rule);
            }
            if (course3.getRows().size() < 1) {
                course3.addRow(none);
                course3.setMergeRule(rule);
            }

            data.put("course0", course0);
            data.put("course1", course1);
            data.put("course2", course2);
            data.put("course3", course3);
            data.put("plan", plan);
            data.put("total", plan.getBxCredit() + plan.getXxCredit());

            XWPFTemplate template = XWPFTemplate.compile("C:\\TPMS\\tpms\\plan.docx").render(data);
            template.write(bos);
            bos.flush();
            out.flush();
            PoitlIOUtils.closeQuietlyMulti(template, bos, out);
        } catch (Exception e) {
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSONUtils.toJSONString(map));
        }
    }

    @Resource
    public LogFeignService logFeignService;

    private void logOperate(String logModule, String logEvent, String logMsg) {
        logFeignService.log(OperateLog.builder()
                .officeId(this.officeId)
                .officeName(this.officeName)
                .logUser(this.userName)
                .logModule(logModule)
                .logEvent(logEvent)
                .logMessage(this.userName + logMsg)
                .build());
    }

}
