package com.tpms.tpms.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.query.ProgramQuery;
import com.tpms.common.web.bean.tp.Program;
import com.tpms.common.web.bean.tp.ProgramCourse;
import com.tpms.common.web.controller.BaseController;
import com.tpms.tpms.service.ProgramCourseService;
import com.tpms.tpms.service.ProgramService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wld
 * @date 2023/5/17 - 20:52
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/program")
public class ProgramController extends BaseController {
    @Resource
    private ProgramService programService;
    @Resource
    private ProgramCourseService programCourseService;

    /**
     * 添加培养方案
     */
    @PostMapping(value = "/")
    public Result addProgram(@RequestBody Program program) throws Exception {
        if (programService.checkDuplicate(program)) {
            program.setDelFlag("0");
            if (programService.save(program)) {
                String courseIds = program.getCourseIds();
                //课程不为空，培养方案添加课程
                if(StringUtils.isNotBlank(courseIds)){
                    String programId = program.getProgramId();

                    List<ProgramCourse> list = new ArrayList<>();
                    for (String courseId : courseIds.split(",")){
                        ProgramCourse tmp = ProgramCourse.builder()
                                .programId(programId).courseId(courseId).build();
                        list.add(tmp);
                    }
                    programCourseService.saveBatch(list);
                }
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
    public Result updateProgram(@RequestBody Program program) {
        if (programService.checkDuplicate(program,program.getProgramId())) {
            if (programService.updateById(program)) {
                String courseIds = program.getCourseIds();

                //courseIds如果为pass，跳过修改培养方案课程
                if("pass".equals(courseIds)){
                    return ResultUtil.success("修改成功");
                }

                String programId = program.getProgramId();
                LambdaQueryWrapper<ProgramCourse> wrapper = Wrappers.lambdaQuery();
                wrapper.eq(ProgramCourse::getProgramId, programId);

                //删除原有课程
                programCourseService.remove(wrapper);

                //courseIds不为空，进行添加课程
                if(StringUtils.isNotBlank(courseIds)){
                    List<ProgramCourse> list = new ArrayList<>();
                    for (String courseId : courseIds.split(",")){
                        ProgramCourse tmp = ProgramCourse.builder()
                                .programId(programId).courseId(courseId).build();
                        list.add(tmp);
                    }
                    programCourseService.saveBatch(list);
                }
                return ResultUtil.success("修改成功");
            } else {
                return ResultUtil.error("修改失败");
            }
        }
        return ResultUtil.success("培养方案名称已存在");
    }

    /**
     * 启用停用
     */
    @PutMapping(value = "/stop")
    public Result updateStop(@RequestBody Map<String, Object> map) {
        String programId = (String) map.get("programId");
        String isStop = (String) map.get("isStop");

        if (StringUtils.isNotBlank(programId) && StringUtils.isNotBlank(isStop)) {
            Program update = Program.builder()
                    .programId(programId).isStop(isStop).build();

            if (programService.updateById(update)) {
                return ResultUtil.success("1".equals(isStop)?"停用成功":"启用成功");
            }
        }
        return ResultUtil.error("操作失败");
    }

    @PostMapping("/page")
    public Result page(@RequestBody ProgramQuery programQuery) {
        Page<Program> page = new Page<>(programQuery.getPageNum(), programQuery.getPageSize());

        QueryWrapper<Program> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(programQuery.getProgramName())) {
            wrapper.like("tp.program_name", programQuery.getProgramName());
        }
        if (StringUtils.isNotBlank(programQuery.getMajorId())) {
            wrapper.eq("tp.major_id", programQuery.getMajorId());
        }
        if (StringUtils.isNotBlank(programQuery.getIsStop())) {
            wrapper.eq("tp.is_stop", programQuery.getIsStop());
        }
        wrapper.eq("tp.del_flag", "0");
        wrapper.orderByDesc("tp.modify_time");
        page = programService.getPage(page, wrapper);

        PageResult<Program> pageBean = PageResult.init(page);

        return ResultUtil.success().buildData("page", pageBean);
    }

    @PostMapping("/list")
    public Result list(@RequestBody Program program) {
        QueryWrapper<Program> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(program.getProgramName())) {
            wrapper.like("tp.program_name", program.getProgramName());
        }
        if (StringUtils.isNotBlank(program.getMajorId())) {
            wrapper.eq("tp.major_id", program.getMajorId());
        }
        wrapper.eq("tp.is_stop", "0");
        wrapper.eq("tp.del_flag", "0");
        wrapper.orderByDesc("tp.modify_time");
        List<Program> list = programService.getList(wrapper);
        return ResultUtil.success().buildData("rows", list);
    }

    /**
     * 根据id查询
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") String programId) {
        Program program = programService.getById(programId);
        List<Course> courses = programCourseService.getCourses(programId);
        program.setCourses(courses);
        return ResultUtil.success(program);
    }

    /**
     * 根据id删除
     */
    @DeleteMapping("/{id}")
    public Result delById(@PathVariable("id") String programId){
        if(programService.removeById(programId)){
            QueryWrapper<ProgramCourse> wrapper = new QueryWrapper<>();
            wrapper.eq("programId", programId);
            programCourseService.remove(wrapper);
            return ResultUtil.success("删除成功");
        }
        return ResultUtil.error("删除失败");
    }
}
