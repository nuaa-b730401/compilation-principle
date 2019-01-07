package org.nuaa.b730401.plcompiler.compiler;

import org.nuaa.b730401.plcompiler.compiler.bean.ObjectCode;
import org.nuaa.b730401.plcompiler.compiler.bean.RuntimeStack;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/7 17:20
 */
public class Interpreter {
    /**
     * 默认的程序最大运行时间，单位s
     */
    private static final int DEFAULT_MAX_EXECUTE_TIME = 3600;
    /**
     * 运行时数据栈
     */
    private RuntimeStack<Integer> dataStack;

    /**
     * 程序最大运行时间
     */
    private int maxExecuteTime;
    /**
     * 开始执行时间
     */
    private long beginTime;
    /**
     * 当前执行时间
     */
    private long currentTime;

    /**
     * 指令执行指针I寄存器
     */
    private ObjectCode ip;

    /**
     * T 指针寄存器，指向栈顶
     */
    private int top;

    /**
     * B 指针寄存器，指向基址
     */
    private int base;
}
