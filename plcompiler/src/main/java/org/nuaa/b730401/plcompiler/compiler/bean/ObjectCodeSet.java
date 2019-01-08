package org.nuaa.b730401.plcompiler.compiler.bean;

import lombok.Data;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: cyw35
 * @Date: 2019/1/7 20:17
 * @Description:
 */
@Data
public class ObjectCodeSet {
    private static final int ROWMAX = 10000;

    /**
     * 记录当前已填充位置的下一行
     * 其数值也表示当前已填充行数
     **/
    private int pos = 0;

    /*
     * 符号表
     * */
    private List<ObjectCode> objectCodeList;

    public ObjectCodeSet() {
        objectCodeList = new ArrayList<>();
        objectCodeList.forEach(objectCode -> {
            objectCode = new ObjectCode(-1, -1, -1);
        });
    }

    /*
     * 填充目标代码
     * */
    public void recordObjectCode(int opcode, int deep, int offset) {
        objectCodeList.add(new ObjectCode(opcode, deep, offset));
        pos++;
    }
}
