package org.nuaa.b730401.plcompiler.compiler.constant;

import java.util.ArrayList;
import java.util.List;

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
}
