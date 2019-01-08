package org.nuaa.b730401.plcompiler.entity;

import lombok.Data;
import org.nuaa.b730401.plcompiler.compiler.bean.ObjectCode;
import org.nuaa.b730401.plcompiler.compiler.constant.ConstInstruction;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 19:40
 */
@Data
public class ObjectCodeView {
    private String instruction;
    private int offset;
    private int deep;

    public ObjectCodeView(ObjectCode code) {
        this.instruction = ConstInstruction.INSTRUCTION_SET.get(code.getOpcode());
        this.offset = code.getOffset();
        this.deep = code.getDeep();
    }
}
