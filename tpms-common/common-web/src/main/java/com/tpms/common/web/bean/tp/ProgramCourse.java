package com.tpms.common.web.bean.tp;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tp_program_course")
public class ProgramCourse implements Serializable {
    /**
     * 培养方案ID
     */
    private String programId;

    /**
     * 课程ID
     */
    private String courseId;

    /**
     * 机构id
     */
    private String officeId;
}

