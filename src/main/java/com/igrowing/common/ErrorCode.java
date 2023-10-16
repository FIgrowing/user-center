package com.igrowing.common;

public enum ErrorCode {

    SUCCESS(0,"ok",""),
    SYSTEM_ERROR(5000,"系统内部异常",""),
    PARAMS_ERROR(4000,"请求参数错误",""),
    NULL_ERROR(4001,"请求数据为空",""),
    NOT_LOGIN(40100,"未登录",""),
    NO_AUTH(40100,"无权限","");




    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code , String message , String description)
    {
        this.code=code;
        this.message=message;
        this.description=description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
