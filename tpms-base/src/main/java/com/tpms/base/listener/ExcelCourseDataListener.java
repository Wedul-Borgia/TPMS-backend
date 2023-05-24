package com.tpms.base.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.tpms.base.entity.excel.ExcelCourseData;
import com.tpms.base.service.CourseService;
import com.tpms.common.web.bean.base.Course;
import com.tpms.common.web.constant.Constant;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wld
 * @date 2023/5/18 - 2:36
 */
@Slf4j
public class ExcelCourseDataListener extends AnalysisEventListener<ExcelCourseData> {

    private CourseService courseService;

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 50;

    private List<Course> courses = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private List<String> existName = new ArrayList<>();
    private List<String> existCode = new ArrayList<>();

    private Integer success = 0;
    private Integer error = 0;
    private String msg;

    /**
     * 因为不能交给spring管理，所以需要自己new，不能自动注入
     */
    public ExcelCourseDataListener(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public void invoke(ExcelCourseData excelCourseData, AnalysisContext analysisContext) {
        if (excelCourseData == null) {
            msg = "文件数据为空";
            try {
                throw new Exception(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String courseName = excelCourseData.getCourseName();
        String courseCode = excelCourseData.getCourseCode();

        if (existName.contains(courseName) || existCode.contains(courseCode)) {
            error++;
        }

        Course course = Course.builder()
                .courseName(courseName)
                .courseCode(courseCode).build();

        if (courseService.checkDuplicate(course)) {
            //转换
            String courseType = Constant.COURSE_TYPE.get(excelCourseData.getCourseType());
            course.setCourseType(courseType);
            course.setCredit(excelCourseData.getCredit());
            courses.add(course);
            existName.add(courseName);
            existCode.add(courseCode);

            if (courses.size() >= BATCH_COUNT) {
                saveData();
                // 存储完成清理 list
                courses = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                existName = new ArrayList<>();
                existCode = new ArrayList<>();
            }
        } else {
            error++;
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        if(courses.size()>0){
            saveData();
        }
        this.msg = "导入成功" + success + "条数据，导入失败" + error + "条数据";
        log.info("所有数据解析完成！" + msg);
    }

    public String getMsg(){
        return msg;
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", courses.size());
        success += courses.size();
        courseService.saveBatch(courses);
        log.info("存储数据库成功！");
    }
}
