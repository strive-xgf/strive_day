package com.xgf.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author xgf
 * @create 2021-11-21 17:16
 * @description 自定义异常
 **/

@NoArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = -3211985892989639258L;

    /**
     * 异常code
     */
    protected String errorCode;

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

}