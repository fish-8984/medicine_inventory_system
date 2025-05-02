package com.luyu.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    /**
     * 创建一个错误结果的静态方法。
     * @param msg 错误信息。
     * @return 返回错误结果的Result对象。
     */
    public static <T> Result<T> error(String msg) {
        // 创建Result对象实例
        Result result = new Result();
        // 设置错误信息
        result.msg = msg;
        // 设置错误码为默认值（通常为0表示错误）
        result.code = 0;
        // 返回带有错误信息的Result对象实例
        return result;
    }

}

