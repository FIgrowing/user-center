package com.igrowing.exception;

import com.igrowing.common.BaseResponse;
import com.igrowing.common.ErrorCode;
import com.igrowing.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description:    // 类说明
 *
 * @ClassName: GlobalExceptionHandler    // 类名，会自动填充
 * @Author: 周天赐        // 创建者
 * @Email: m18879004515@163.com
 * @Date: 2023/10/8 20:06   // 时间
 * @Version: 1.0     // 版本
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseException.class)
    public BaseResponse responseExceptionHandler(ResponseException e)
    {
        log.error("ResponseException: "+e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e)
    {
        log.error("RuntimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"系统内部异常");
    }

}

    
    
