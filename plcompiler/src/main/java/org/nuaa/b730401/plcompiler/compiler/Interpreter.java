package org.nuaa.b730401.plcompiler.compiler;

import org.nuaa.b730401.plcompiler.compiler.bean.ErrorBean;
import org.nuaa.b730401.plcompiler.compiler.bean.ObjectCode;
import org.nuaa.b730401.plcompiler.compiler.constant.ConstInstruction;
import org.nuaa.b730401.plcompiler.compiler.exception.StackOverflowException;
import org.nuaa.b730401.plcompiler.compiler.util.Procedure;
import org.omg.CORBA.DATA_CONVERSION;

import java.util.*;

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
     * 默认栈最大空间
     */
    private static final int DEFAULT_MAX_DATA_STACK_SIZE = 100000;
    /**
     * 运行时数据栈
     */
    private List<Integer> dataStack;

    /**
     * 程序最大运行时间(单位秒)
     */
    private int maxExecuteTime;
    /**
     * 栈最大空间
     */
    private int maxStackSize;

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
    /**
     * P 指针寄存器，指向下一条指令地址
     */
    private int next;

    private List<ObjectCode> codeList;

    private List<ErrorBean> errorList;

    /**
     * 运算过程map
     */
    private Map<Integer, Procedure> CALCULATE_EXECUTE_MAP = new HashMap<Integer, Procedure>(){{
        put(ConstInstruction.OPR_RET, () -> {
            top = base;
            next = dataStack.get(base + 2);
            base = dataStack.get(base);
        });

        put(ConstInstruction.OPR_NEG, () -> {
            dataStack.set(top - 1, -dataStack.get(top - 1));
        });

        put(ConstInstruction.OPR_ADD, () -> {
            dataStack.set(top - 2, dataStack.get(top - 2) + dataStack.get(top - 1));
            top--;
        });

        put(ConstInstruction.OPR_SUB, ()-> {
            dataStack.set(top - 2, dataStack.get(top - 2) - dataStack.get(top - 1));
            top--;
        });

        put(ConstInstruction.OPR_MUL, () -> {
            dataStack.set(top - 2, dataStack.get(top - 2) * dataStack.get(top - 1));
            top--;
        });

        put(ConstInstruction.OPR_DIV, () -> {
            dataStack.set(top - 2, dataStack.get(top - 2) / dataStack.get(top - 1));
            top--;
        });

        put(ConstInstruction.OPR_ODD, () -> {
            dataStack.set(top - 1, dataStack.get(top - 1) % 2);
        });

        put(ConstInstruction.OPR_SEVEN, () -> {
            // to do nothing
        });

//        put(ConstInstruction.)
    }};

    /**
     * 指令执行过程map
     */
    private Map<Integer, Procedure> INSTRUCTION_EXECUTE_MAP = new HashMap<Integer, Procedure>(){{
        // LIT 指令
        put(ConstInstruction.LIT, () -> {
            dataStack.set(top, ip.getOffset());
            top++;
        });
        put(ConstInstruction.OPR, () -> {

        });
    }};

    public Interpreter(List<ObjectCode> codeList) {
        dataStack = new ArrayList<>(DEFAULT_MAX_DATA_STACK_SIZE);
        this.codeList = codeList;
        errorList = new LinkedList<>();
    }

    public void interpreter() {
        if (codeList == null || codeList.size() == 0) {
            return;
        }
        long beg = System.currentTimeMillis();
        while (next < codeList.size() && (System.currentTimeMillis() - beg) / 1000 <= maxExecuteTime) {
            ip = codeList.get(next++);
        }
    }
}
