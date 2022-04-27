package com.xgf.constant.page;

import com.xgf.constant.reqrep.CommonResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xgf
 * @create 2022-04-26 01:37
 * @description 通用分页响应
 **/

@Data
@NoArgsConstructor
@ApiModel("通用分页结果")
public class CommonPageDataResponse<T> extends CommonResponse {

    private static final long serialVersionUID = 696260179185712508L;

    @ApiModelProperty("分页查询结果List")
    private List<T> dataList;

    @ApiModelProperty("分页查询数量")
    private Long count;

    public static <T> CommonPageDataResponse<T> fail() {
        return fail(CommonResponse.CommonResponseCodeEnum.SERVICE_EXCEPTION);
    }

    public static <T> CommonPageDataResponse<T> fail(CommonResponseCodeEnum respEnum) {
        return custom(respEnum, new ArrayList<>());
    }

    public static <T> CommonPageDataResponse<T> fail(String responseMessage) {
        return custom(CommonResponse.CommonResponseCodeEnum.CUSTOM_MESSAGE_EXCEPTION.getCode(), responseMessage, CommonResponse.CommonResponseCodeEnum.SERVICE_EXCEPTION.getResponseTypeEnum(), new ArrayList<>());
    }

    public static <T> CommonPageDataResponse<T> ok() {
        return ok(new ArrayList<>());
    }

    public static <T> CommonPageDataResponse<T> ok(List<T> dataList) {
        return custom(CommonResponse.CommonResponseCodeEnum.SUCCESS, dataList);
    }

    public static <T> CommonPageDataResponse<T> ok(String responseMessage, List<T> dataList) {
        return custom(CommonResponse.CommonResponseCodeEnum.CUSTOM_MESSAGE_EXCEPTION.getCode(), responseMessage, CommonResponse.CommonResponseCodeEnum.SUCCESS.getResponseTypeEnum(), dataList);
    }

    /**
     * 自定义
     */
    public static <T> CommonPageDataResponse<T> custom(CommonResponseCodeEnum respEnum, List<T> dataList) {
        return custom(respEnum.getCode(), respEnum.getValue(), respEnum.getResponseTypeEnum(), dataList);
    }
    
    public static <T> CommonPageDataResponse<T> custom(String responseCode, String responseMessage, ResponseTypeEnum responseType, List<T> dataList) {
        CommonPageDataResponse<T> result = new CommonPageDataResponse<T>();
        result.setResponseCode(responseCode);
        result.setResponseMessage(responseMessage);
        result.setResponseType(responseType);
        result.setDataList(dataList);
        // 实时计算数据量
        result.setCount(computeCount(dataList));
        return result;
    }

    /**
     * 计算分页查询数量
     * @param dataList 分页数据结构
     * @param <T> 泛型类型
     * @return 查询结果数量
     */
    public static <T> Long computeCount(List<T> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return 0L;
        }

        return (long) dataList.size();
    }

    
    
}
