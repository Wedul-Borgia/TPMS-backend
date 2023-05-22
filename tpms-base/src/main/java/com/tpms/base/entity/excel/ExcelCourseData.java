package com.tpms.base.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import lombok.Data;

/**
 *
 * @ExcelProperty(index = 2)
 * @author wld
 * @date 2023/5/18 - 2:33
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(25)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER)
public class ExcelCourseData {

    @ColumnWidth(50)
    @ExcelProperty(value = "课程名称",index = 0)
    private String courseName;


    @ColumnWidth(25)
    @ExcelProperty(value = "课程编号",index = 1)
    private String courseCode;

    @ColumnWidth(50)
    @ExcelProperty(value = "课程类型",index = 2)
    private String courseType;

    @ColumnWidth(20)
    @ExcelProperty(value = "学分",index = 3)
    private Float credit;
}
