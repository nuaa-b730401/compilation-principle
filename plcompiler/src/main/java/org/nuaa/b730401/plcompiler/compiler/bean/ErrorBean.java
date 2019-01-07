package org.nuaa.b730401.plcompiler.compiler.bean;

import lombok.Data;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/7 11:01
 */
@Data
public class ErrorBean {
    public static final int INVALID_OPCODE = 1;
    public static final int RUNTIME_ERROR = 2;

    private int type;
    private int line;
    private int offset;
    private String desc;

    public ErrorBean(int type, int line, String desc) {
        this.type = type;
        this.line = line;
        this.desc = desc;
    }
}
