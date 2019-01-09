package org.nuaa.b730401.plcompiler.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 10:24
 */
@Data
public class Response <T>{
    /**
     * 成功状态
     */
    public static final int NORMAL_SUCCESS_CODE = 0;
    /**
     * 编译成功
     */
    public static final int COMPILE_SUCCESS_CODE = 1;
    /**
     * 运行成功
     */
    public static final int RUN_SUCCESS_CODE = 2;
    /**
     * 编译出错
     */
    public static final int COMPILE_ERROR_CODE = 3;
    /**
     * 运行错误
     */
    public static final int RUN_ERROR_CODE = 4;
    /**
     * 等待输入
     */
    public static final int WAIT_INPUT_CODE = 5;
    /**
     * 服务器错误
     */
    public static final int SERVER_ERROR_CODE = 6;

    /**
     * 测评错误
     */
    public static final int JUDGE_ERROR_CODE = 7;

    /**
     * 超出时间限制
     */
    public static final int JUDGE_TIMEOUT_CODE = 8;

    /**
     * 堆栈溢出
     */
    public static final int STACK_OVERFLOW_ERROR = 9;

    private int code;
    private String msg;
    private String token;

    private T data;
    private List<T> array;
    private long count;

    public Response() {}

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = data != null ? 1 : 0;
    }

    public Response(int code, String msg, List<T> array) {
        this.code = code;
        this.msg = msg;
        this.array = array;
        this.count = array != null ? array.size() : 0;
    }
    public Response(int code, String msg, List<T> array, String token) {
        this.code = code;
        this.msg = msg;
        this.array = array;
        this.count = array != null ? array.size() : 0;
        this.token = token;
    }

    public Response(int code, String msg, List<T> array, long count) {
        this.code = code;
        this.msg = msg;
        this.array = array;
        this.count = count;
    }

}
