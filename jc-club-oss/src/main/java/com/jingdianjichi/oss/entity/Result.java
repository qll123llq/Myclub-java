package com.jingdianjichi.oss.entity;

import lombok.Data;

@Data
public class Result<T> {

    private Boolean success;

    private Integer errno;

    private String message;

    private T data;

    public static Result ok(){
        Result result = new Result();
        result.setSuccess(true);
        result.setErrno(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getDesc());
        return result;
    }

    public static <T> Result ok(T data){
        Result result = new Result();
        result.setSuccess(true);
        result.setErrno(ResultCodeEnum.SUCCESS.getCode());
        result.setMessage(ResultCodeEnum.SUCCESS.getDesc());
        result.setData(data);
        return result;
    }
    public static <T> Result okByEditor(T data){
        Result result = new Result();
        result.setSuccess(true);
        result.setErrno(0);
        result.setMessage(ResultCodeEnum.SUCCESS.getDesc());
        result.setData(data);
        return result;
    }
    public static Result failByEditor(){
        Result result = new Result();
        result.setSuccess(false);
        result.setErrno(1);
        result.setMessage(ResultCodeEnum.FAIL.getDesc());
        return result;
    }
    public static Result fail(){
        Result result = new Result();
        result.setSuccess(false);
        result.setErrno(ResultCodeEnum.FAIL.getCode());
        result.setMessage(ResultCodeEnum.FAIL.getDesc());
        return result;
    }

    public static <T> Result fail(T data){
        Result result = new Result();
        result.setSuccess(false);
        result.setErrno(ResultCodeEnum.FAIL.getCode());
        result.setMessage(ResultCodeEnum.FAIL.getDesc());
        result.setData(data);
        return result;
    }

}
