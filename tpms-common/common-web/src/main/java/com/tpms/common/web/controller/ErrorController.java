package com.tpms.common.web.controller;

import com.tpms.common.web.bean.Result;
import com.tpms.common.web.bean.ResultUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wld
 * @date 2023/5/15 - 21:29
 */
@RestController
@CrossOrigin
public class ErrorController {

    //公共错误跳转
    @RequestMapping(value = "autherror")
    public Result autherror(int code){
        if(code == 1){
            return ResultUtil.error(999,"您还未登录");
        }else{
            return ResultUtil.error(1000,"权限不足");
        }
    }
}
