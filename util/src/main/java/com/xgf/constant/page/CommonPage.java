package com.xgf.constant.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xgf
 * @create 2022-04-25 12:22
 * @description 分页通用
 **/

@Data
@Slf4j
@NoArgsConstructor
@ApiModel(value = "分页通用对象", description = "分页请求使用")
public class CommonPage {

    /**
     * 默认页数 : 第一页
     */
    public static final Integer DEFAULT_PAGE_NUM = 1;

    /**
     * 默认每页数据量 : 20
     */
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    /**
     * 默认每页最大数据量 : 500
     */
    public static final Integer MAX_PAGE_SIZE = 5000;

    /**
     * 默认偏移量
     */
    public static final Integer DEFAULT_OFFSET = 0;


    /**
     * 降序排序
     */
    public static final String DESC = "desc";

    /**
     * 升序排序
     */
    public static final String ASC = "asc";

    @NotNull(message = "CommonPage，当前页不能为空")
    @Min(value = 0, message = "CommonPage，当前页不能小于0")
    @ApiModelProperty(value = "当前页数，默认第一页", example = "1")
    private Integer pageNum = DEFAULT_PAGE_NUM;

    @NotNull(message = "CommonPage，每页数据量不能为空")
    @Min(value = 0, message = "CommonPage，每页数据量不能小于0")
    @ApiModelProperty(value = "每页数据量，默认20", example = "20")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    @ApiModelProperty(value = "排序字段")
    private String sortField;

    @ApiModelProperty(value = "排序方式，升序asc，降序desc，默认升序asc", example = "asc")
    private String sortWay = ASC;

    /**
     * 偏移量，跳过 offset 条数据，然后直接取 pageSize 条数据
     */
    @ApiModelProperty(value = "偏移量, 默认0", example = "0")
    private Integer offset = DEFAULT_OFFSET;

    /**
     * 分页偏移量，跳过 pageSkip 条数据然后通过页数来取值
     */
    @ApiModelProperty(value = "分页时跳过多少数据, 默认0", example = "0")
    private Integer pageSkip = DEFAULT_OFFSET;

    public CommonPage(Integer pageNum, Integer pageSize) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public CommonPage(Integer pageNum, Integer pageSize, String sortField, String sortWay) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortWay = sortWay;
    }

    public static CommonPage valueOf(Integer pageNum, Integer pageSize) {
        return new CommonPage(pageNum, pageSize);
    }

    public static CommonPage valueOf(Integer pageNum, Integer pageSize, String sortField, String sortWay) {
        return new CommonPage(pageNum, pageSize, sortField, sortWay);
    }

    /**
     * 通过 pageNum、pageSize、pageSkip 来返回数据 List 中应该返回的数据量
     * @param dataList 数据
     * @param <T> 类型
     * @return
     */
    public <T> List<T> subDataListByPage(List<T> dataList) {
        // 当前查询数量 不满足分页 页数 + 分页量 + 分页偏移量 的时候，返回空数组
        if (dataList.size() <= (pageNum - 1) * pageSize + pageSkip) {
            return new ArrayList<>();
        }

        // 开始下标（+偏移量）
        int start = (pageNum - 1) * pageSize + pageSkip;
        // 结束下标 (当前数据大小 - 开始坐标 和 分页数据量 去较小值)
        int end = start + Math.min(dataList.size() - start , pageSize);

        log.info("====== CommonPage page subDataListByPage start = 【{}】, end = 【{}】, pageSkip = 【{}】", start, end, pageSkip);
        return dataList.subList(start, end);
    }


    /**
     * 通过 偏移量 来返回分页数据
     * @param dataList 数据
     * @param <T> 类型
     * @return
     */
    public <T> List<T> subDataListByOffset(List<T> dataList) {
        // 当前查询数量 不满足分页 页数 + 分页量 + 分页偏移量 的时候，返回空数组
        if (dataList.size() <= offset) {
            return new ArrayList<>();
        }

        // 开始下标（+偏移量）
        int start = offset;
        // 结束下标 (当前数据大小 - 开始坐标 和 分页数据量 去较小值)
        int end = start + Math.min(dataList.size() - start , pageSize);

        log.info("====== CommonPage page subDataListByOffset start = 【{}】, end = 【{}】", start, end);
        return dataList.subList(start, end);
    }




}
