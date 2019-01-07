package org.nuaa.b730401.plcompiler.compiler.bean;

import lombok.Data;
import org.nuaa.b730401.plcompiler.compiler.constant.ConstInstruction;

/**
 * @Author: ToMax
 * @Description: 目标码
 * @Date: Created in 2019/1/7 15:47
 */
@Data
public class ObjectCode {
    /**
     * F段，操作码
     */
    private int opcode;
    /**
     * L段，调用层与说明层的层差
     */
    private int deep;
    /**
     * A段，位移量
     */
    private int offset;

    public ObjectCode(int opcode, int deep, int offset) {
        this.opcode = opcode;
        this.deep = deep;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return ConstInstruction.INSTRUCTION_SET.get(opcode) + " " + deep + " " + offset + "\n";
    }
}
