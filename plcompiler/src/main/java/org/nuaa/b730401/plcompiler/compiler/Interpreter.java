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

    private long executeTime;

    private List<ObjectCode> codeList;

    private List<ErrorBean> errorList;

    /**
     * 输出流
     */
    private StringBuilder outputBuffer;



    public Interpreter(List<ObjectCode> codeList) {
        dataStack = new ArrayList<>(DEFAULT_MAX_DATA_STACK_SIZE);
        this.codeList = codeList;
        errorList = new LinkedList<>();
        outputBuffer = new StringBuilder();
    }

    public void interpreter() {
        if (codeList == null || codeList.size() == 0) {
            return;
        }
        long beg = System.currentTimeMillis();
        while (next < codeList.size() && (System.currentTimeMillis() - beg) / 1000 <= maxExecuteTime) {
            ip = codeList.get(next++);
            instructionExecuteMap.get(ip.getOpcode()).apply();
        }
        executeTime = (System.currentTimeMillis() - beg) / 1000;
    }

    /**
     * 运算过程map
     */
    private Map<Integer, Procedure> calculateExecuteMap = new HashMap<Integer, Procedure>(){{
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

        put(ConstInstruction.OPR_IS_EQUAL, () -> {
            dataStack.set(top - 2,
                    dataStack.get(top - 2).equals(dataStack.get(top - 1)) ? 1 : 0);
            top--;
        });

        put(ConstInstruction.OPR_IS_NOT_EQUAL, () -> {
            dataStack.set(top - 2,
                    dataStack.get(top - 2).equals(dataStack.get(top - 1)) ? 0 : 1);
            top--;
        });

        put(ConstInstruction.OPR_LESS, () -> {
            dataStack.set(top - 2,
                    dataStack.get(top - 2) < dataStack.get(top - 1) ? 1 : 0);
            top--;
        });

        put(ConstInstruction.OPR_LESS_EQUAL, () -> {
            dataStack.set(top - 2,
                    dataStack.get(top - 2) <= dataStack.get(top - 1) ? 1 : 0);
            top--;
        });

        put(ConstInstruction.OPR_LARGE, () -> {
            dataStack.set(top - 2,
                    dataStack.get(top - 2) > dataStack.get(top - 1) ? 1 : 0);
            top--;
        });

        put(ConstInstruction.OPR_LARGE_EQUAL, () -> {
            dataStack.set(top - 2,
                    dataStack.get(top - 2) >= dataStack.get(top - 1) ? 1 : 0);
            top--;
        });

        put(ConstInstruction.OPR_PRINT, () -> {
            outputBuffer.append(dataStack.get(top - 1));
            outputBuffer.append(" ");
        });

        put(ConstInstruction.OPR_PRINT_LINE, () -> {
            outputBuffer.append('\n');
        });

        put(ConstInstruction.OPR_READ, () -> {
            // TODO : read
        });
    }};

    /**
     * 指令执行过程map
     */
    private Map<Integer, Procedure> instructionExecuteMap = new HashMap<Integer, Procedure>(){{
        // LIT 指令
        put(ConstInstruction.LIT, () -> {
            dataStack.set(top, ip.getOffset());
            top++;
        });

        // OPR 指令
        put(ConstInstruction.OPR, () -> {
            calculateExecuteMap.get(ip.getOffset()).apply();
        });

        // LOD 指令
        put(ConstInstruction.LOD, () -> {
            dataStack.set(top++, dataStack.get(ip.getOffset() + queryBase(base, ip.getDeep())));
        });

        // STO 指令
        put(ConstInstruction.STO, () -> {
            dataStack.set(ip.getOffset() + queryBase(base, ip.getDeep()),
                    dataStack.get(top - 1));
            top--;
        });

        // CAL 指令
        put(ConstInstruction.CAL, () -> {
            dataStack.set(top, base);
            dataStack.set(top + 1, queryBase(base, ip.getOffset()));
            dataStack.set(top + 2, next);
            base = top;
            next = ip.getOffset();
        });

        // INT 指令
        put(ConstInstruction.INT, () -> {
            top += ip.getOffset();
        });

        // JMP 指令
        put(ConstInstruction.JMP, () -> {
            next = ip.getOffset();
        });

        // JPC 指令
        put(ConstInstruction.JPC, () -> {
            if (dataStack.get(top - 1) == 0) {
                next = ip.getOffset();
            }
        });

    }};

    private int queryBase(int currentBasePointer, int level) {
        int oldBasePointer = currentBasePointer;
        while (level-- > 0) {
            oldBasePointer = dataStack.get(oldBasePointer + 1);
        }
        return oldBasePointer;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public List<ErrorBean> getErrorList() {
        return errorList;
    }

    public StringBuilder getOutputBuffer() {
        return outputBuffer;
    }
}
