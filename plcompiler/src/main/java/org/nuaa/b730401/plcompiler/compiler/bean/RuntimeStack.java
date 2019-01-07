package org.nuaa.b730401.plcompiler.compiler.bean;

import org.nuaa.b730401.plcompiler.compiler.exception.StackOverflowException;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @Author: ToMax
 * @Description: 运行时栈
 * @Date: Created in 2019/1/7 16:37
 */
public class RuntimeStack<E> {
    private final Object[] data;
    public static final int DEFAULT_MAX_STACK_SIZE = 10000;
    public int top = 0;
    public int base = 0;
    public RuntimeStack() {
        this(DEFAULT_MAX_STACK_SIZE);
    }

    public RuntimeStack(int size) {
        data = new Object[size];
    }

    public void push(E e) throws StackOverflowException {
        if (size() < data.length) {
            data[top++] = e;
        } else {
            throw new StackOverflowException("data stack overflow error");
        }
    }

    public E pop() {
        if (!empty()) {
            return (E) data[top--];
        }
        return null;
    }

    public int size() {
        return top - base;
    }

    public boolean empty() {
        return size() == 0;
    }
}
