package org.nuaa.b730401.plcompiler.compiler.exception;

/**
 * @Author: ToMax
 * @Description: 数据栈溢出
 * @Date: Created in 2019/1/7 16:44
 */
public class StackOverflowException extends Exception{
    public StackOverflowException() {
        super();
    }

    public StackOverflowException(String message) {
        super(message);
    }
}
