package org.nuaa.b730401.plcompiler.compiler.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/7 15:38
 */
public class ConstInstruction {
    public static final int LIT = 0;
    public static final int OPR = 1;
    public static final int LOD = 2;
    public static final int STO = 3;
    public static final int CAL = 4;
    public static final int INT = 5;
    public static final int JMP = 6;
    public static final int JPC = 7;
    public static final int RED = 8;
    public static final int WRT = 9;

    public static final List<String> INSTRUCTION_SET = new ArrayList<String>(){{
        add("LIT");
        add("OPR");
        add("LOD");
        add("STO");
        add("CAL");
        add("INT");
        add("JMP");
        add("JPC");
        add("RED");
        add("WRT");
    }};

    public static final Map<String, Integer> INSTRUCTION_MAP = new HashMap<String, Integer>(){{
        put("LIT", LIT);
        put("OPR", OPR);
        put("LOD", LOD);
        put("STO", STO);
        put("CAL", CAL);
        put("INT", INT);
        put("JMP", JMP);
        put("JPC", JPC);
        put("RED", RED);
        put("WRT", WRT);
    }};

    /**
     * 过程调用结束后,返回调用点并退栈
     */
    public static final int OPR_RET = 0;
    /**
     * 栈顶元素取反
     */
    public static final int OPR_NEG = 1;
    /**
     * 次栈顶与栈顶相加，退两个栈元素，结果值进栈
     */
    public static final int OPR_ADD = 2;
    /**
     * 次栈顶减去栈顶，退两个栈元素，结果值进栈
     */
    public static final int OPR_SUB = 3;
    /**
     * 次栈顶乘以栈顶，退两个栈元素，结果值进栈
     */
    public static final int OPR_MUL = 4;
    /**
     * 次栈顶除以栈顶，退两个栈元素，结果值进栈
     */
    public static final int OPR_DIV = 5;
    /**
     * 栈顶元素的奇偶判断，结果值在栈顶
     */
    public static final int OPR_ODD = 6;
    /**
     *
     */
    public static final int OPR_SEVEN = 7;
    /**
     * 次栈顶与栈顶是否相等，退两个栈元素，结果值进栈
     */
    public static final int OPR_IS_EQUAL = 8;
    /**
     * 次栈顶与栈顶是否相等，退两个栈元素，结果值进栈
     */
    public static final int OPR_IS_NOT_EQUAL = 9;
    /**
     * 次栈顶是否小于栈顶，退两个栈元素，结果值进栈
     */
    public static final int OPR_LESS = 10;
    /**
     * 次栈顶是否小于等于栈顶，退两个栈元素，结果值进栈
     */
    public static final int OPR_LESS_EQUAL = 11;
    /**
     * 次栈顶是否大于栈顶，退两个栈元素，结果值进栈
     */
    public static final int OPR_LARGE = 12;
    /**
     * 次栈顶是否大于等于栈顶，退两个栈元素，结果值进栈
     */
    public static final int OPR_LARGE_EQUAL = 13;
    /**
     * 栈顶值输出至屏幕,并且输出一个空格
     */
    public static final int OPR_PRINT = 14;
    /**
     * 屏幕输出换行
     */
    public static final int OPR_PRINT_LINE = 15;
    /**
     * 从命令行读入一个输入置于栈顶
     */
    public static final int OPR_READ = 16;

}
