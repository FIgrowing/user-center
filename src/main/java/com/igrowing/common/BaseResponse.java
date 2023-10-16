package com.igrowing.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:    // 类说明
 *
 * @ClassName: BaseResponse    // 类名，会自动填充
 * @Author: 周天赐        // 创建者
 * @Email: m18879004515@163.com
 * @Date: 2023/10/8 18:45   // 时间
 * @Version: 1.0     // 版本
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message,String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description=description;
    }

    public BaseResponse(int code,T data)
    {
        this.code = code;
        this.data = data;
    }

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }


    public BaseResponse(ErrorCode errorCode)
    {
        this.code=errorCode.getCode();
        this.message=errorCode.getMessage();
        this.description=errorCode.getDescription();
    }

}

    
    
