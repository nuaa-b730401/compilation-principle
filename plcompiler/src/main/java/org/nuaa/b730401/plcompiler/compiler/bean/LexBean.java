package org.nuaa.b730401.plcompiler.compiler.bean;

import lombok.Data;
import org.nuaa.b730401.plcompiler.compiler.constant.ConstWords;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/7 11:09
 */
@Data
public class LexBean {
    private int id;
    private int line;
    private String value;

    public LexBean(int id, int line, String value) {
        this.id = id;
        this.line = line;
        this.value = value;
    }

    @Override
    public String toString() {
        return ConstWords.LABEL_MAP.get(id) + "(line : " + line + " ,value = " + value + ")";
    }
}
