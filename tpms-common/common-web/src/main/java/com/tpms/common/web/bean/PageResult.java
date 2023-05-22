package com.tpms.common.web.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tpms.common.web.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页数据的Bean
 *
 * @author wld
 * @date 2022/9/29 - 16:23
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    /**
     * 每页显示的条数
     */
    private Long pageSize;

    /**
     * 当前的页码
     */
    private Long current;

    /**
     * 一共有多少条记录
     */
    private Long total;

    /**
     * 一共有多少页
     */
    private Long pages;

    /**
     * 每一页所显示的数据
     */
    private List<T> result;

    /**
     * 分页请求路径
     */
    private String url;

    /**
     * 将MyBatisPlus返回的IPage数据封装为自定义的PageBean
     *
     * @param page
     * @param <T>
     * @return
     */
    public static <T> PageResult<T> init(IPage<T> page) {
        PageResult<T> pageBean = new PageResult<>();
        pageBean.setCurrent(page.getCurrent());
        pageBean.setPageSize(page.getSize());
        pageBean.setPages(page.getPages());
        pageBean.setTotal(page.getTotal());
        pageBean.setResult(page.getRecords());
        return pageBean;
    }

    @Override
    public String toString() {
        return JsonUtil.obj2String(this);
    }
}
