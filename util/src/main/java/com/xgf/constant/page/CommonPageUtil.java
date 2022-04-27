package com.xgf.constant.page;

import org.apache.ibatis.session.RowBounds;

/**
 * @author xgf
 * @create 2022-04-27 23:10
 * @description 通用分页工具类
 **/

public class CommonPageUtil {

    /**
     * 转换 RowBounds
     * @param offset 偏移量
     * @param pageSize 页面大小
     * @return RowBounds
     */
    public static RowBounds convertRowBoundsByOffset(int offset, int pageSize) {
        return new RowBounds(offset, pageSize);
    }

    /**
     * 通过 CommonPage 偏移量 和 页面大小 转换 RowBounds
     * @param page CommonPage
     * @return RowBounds
     */
    public static RowBounds convertRowBoundsByOffset(CommonPage page) {
        return convertRowBoundsByOffset(page.getOffset(), page.getPageSize());
    }

    /**
     * 转换 RowBounds
     * @param pageNumber 当前页数
     * @param pageSize 页面大小
     * @return RowBounds
     */
    public static RowBounds convertRowBoundsByPageNumber(int pageNumber, int pageSize) {
        return convertRowBoundsByOffset((pageNumber - 1) * pageSize, pageSize);
    }

    /**
     * 通过 CommonPage 当前页数 和 页面大小 转换 RowBounds
     * @param page CommonPage
     * @return RowBounds
     */
    public static RowBounds convertRowBoundsByPageNumber(CommonPage page) {
        return convertRowBoundsByPageNumber(page.getPageNum(), page.getPageSize());
    }

}
