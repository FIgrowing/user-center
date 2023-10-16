package com.igrowing.exception;

import com.igrowing.common.ErrorCode;

/**
 * Description:    // 类说明
 *
 * @ClassName: ResponseException    // 类名，会自动填充
 * @Author: 周天赐        // 创建者
 * @Email: m18879004515@163.com
 * @Date: 2023/10/8 19:55   // 时间
 * @Version: 1.0     // 版本
 */
public class ResponseException extends RuntimeException{

    private final int code;
    private final String description;

    public ResponseException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public ResponseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }


    public ResponseException(ErrorCode errorCode , String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description=description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

    
    
