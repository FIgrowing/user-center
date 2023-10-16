package com.igrowing.common;

/**
 * Description:    // 类说明
 *
 * @ClassName: ResultUtils    // 类名，会自动填充
 * @Author: 周天赐        // 创建者
 * @Email: m18879004515@163.com
 * @Date: 2023/10/8 19:29   // 时间
 * @Version: 1.0     // 版本
 */
public class ResultUtils {

    public static <T> BaseResponse<T> success(T data)
    {
        return new BaseResponse<>(0,data,"ok");
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode)
    {
        return new BaseResponse<>(errorCode);
    }

    public static BaseResponse error(int code , String message , String description)
    {
        return new BaseResponse(code,null, message, description);
    }

    public static BaseResponse error(ErrorCode errorCode , String message , String description)
    {
        return new BaseResponse(errorCode.getCode(), null,message, description);
    }

}

    
    
