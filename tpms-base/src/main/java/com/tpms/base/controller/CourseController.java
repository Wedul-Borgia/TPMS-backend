package com.tpms.base.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tpms.base.entity.excel.ExcelCourseData;
import com.tpms.base.listener.ExcelCourseDataListener;
import com.tpms.base.service.CourseService;
import com.tpms.common.web.bean.PageResult;
import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.bean.query.CourseQuery;
import com.tpms.common.web.constant.Constant;
import com.tpms.common.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wld
 * @date 2023/5/16 - 15:02
 */
@CrossOrigin
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController {
    @Resource
    private CourseService courseService;

    @PostMapping("/")
    public Result add(@RequestBody Course course) {

        if(courseService.checkDuplicate(course)){
            course.setDelFlag("0");
            course.setIsStop("0");
            boolean res = courseService.save(course);
            if (res) {
                return ResultUtil.success("添加成功");
            } else {
                return ResultUtil.error("添加失败");
            }
        }else{
            return ResultUtil.error("课程名称或编码已存在");
        }

    }

    /**
     * 修改课程
     */
    @PutMapping(value = "/")
    public Result update(@RequestBody Course course) {

        if (courseService.checkDuplicate(course,course.getCourseId())) {
            if (courseService.updateById(course)) {
                return ResultUtil.success("修改成功");
            } else {
                return ResultUtil.error("修改失败");
            }
        }
        return ResultUtil.success("课程名称或编码已存在");
    }

    /**
     * 启用停用
     */
    @PutMapping(value = "/stop")
    public Result updateStop(@RequestBody Map<String, Object> map) {
        String courseId = (String) map.get("courseId");
        String isStop = (String) map.get("isStop");

        if (StringUtils.isNotBlank(courseId) && StringUtils.isNotBlank(isStop)) {
            Course update = Course.builder()
                    .courseId(courseId).isStop(isStop).build();

            if (courseService.updateById(update)) {
                return ResultUtil.success("1".equals(isStop)?"停用成功":"启用成功");
            }
        }
        return ResultUtil.error("操作失败");
    }

    @PostMapping("/page")
    public Result page(@RequestBody CourseQuery courseQuery) {
        Page<Course> page = new Page<>(courseQuery.getPageNum(), courseQuery.getPageSize());

        LambdaQueryWrapper<Course> wrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(courseQuery.getCourseName())){
            wrapper.like(Course::getCourseName,courseQuery.getCourseName());
        }
        if(StringUtils.isNotBlank(courseQuery.getCourseCode())){
            wrapper.like(Course::getCourseCode,courseQuery.getCourseCode());
        }
        if(StringUtils.isNotBlank(courseQuery.getIsStop())){
            wrapper.eq(Course::getIsStop,courseQuery.getIsStop());
        }
        if(StringUtils.isNotBlank(courseQuery.getCourseType())){
            wrapper.eq(Course::getCourseType,courseQuery.getCourseType());
        }
        wrapper.orderByAsc(Course::getCourseCode);
        courseService.page(page, wrapper);

        PageResult<Course> pageBean = PageResult.init(page);

        return ResultUtil.success().buildData("page", pageBean);
    }

    /**
     * 列表查询（下拉之类的）
     * @param courseQuery
     * @return
     */
    @PostMapping("/list")
    public Result list(@RequestBody CourseQuery courseQuery) {
        LambdaQueryWrapper<Course> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(courseQuery.getCourseName())) {
            wrapper.like(Course::getCourseName, courseQuery.getCourseName());
        }
        if (StringUtils.isNotBlank(courseQuery.getCourseCode())) {
            wrapper.like(Course::getCourseCode, courseQuery.getCourseCode());
        }
        wrapper.eq(Course::getIsStop, "0")
                .orderByAsc(Course::getCourseCode);
        List<Course> list = courseService.list(wrapper);
        return ResultUtil.success().buildData("rows",list);
    }

    /**
     * 导出文件
     *
     */
    @PostMapping("/download")
    public void download(@RequestBody CourseQuery courseQuery, HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<Course> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(courseQuery.getCourseName())) {
            wrapper.like(Course::getCourseName, courseQuery.getCourseName());
        }
        if (StringUtils.isNotBlank(courseQuery.getCourseCode())) {
            wrapper.like(Course::getCourseCode, courseQuery.getCourseCode());
        }
        wrapper.eq(Course::getIsStop, "0")
                .orderByAsc(Course::getCourseCode);
        List<Course> list = courseService.list(wrapper);

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            String fileName = URLEncoder.encode("课程导出数据", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), ExcelCourseData.class).autoCloseStream(Boolean.FALSE).sheet("课程数据")
                    .doWrite(toExcelData(list));
        } catch (Exception e) {
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

    /**
     * 导入文件
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        try {
            EasyExcel.read(file.getInputStream(), ExcelCourseData.class, new ExcelCourseDataListener(courseService)).sheet().doRead();
            return ResultUtil.success("导入成功");
        }catch (Exception e){
            return ResultUtil.error("导入失败");
        }
    }

    List<ExcelCourseData> toExcelData(List<Course> list){
        List<ExcelCourseData> excel = new ArrayList<>();
        for(Course course : list){
            ExcelCourseData excelCourseData = new ExcelCourseData();
            excelCourseData.setCourseName(course.getCourseName());
            excelCourseData.setCourseCode(course.getCourseCode());
            excelCourseData.setCourseType(Constant.COURSE_TYPE_CN.get(course.getCourseType()));
            excelCourseData.setCredit(course.getCredit());
            excel.add(excelCourseData);
        }
        return excel;
    }
}
