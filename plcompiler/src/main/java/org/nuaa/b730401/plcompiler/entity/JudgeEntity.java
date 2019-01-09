package org.nuaa.b730401.plcompiler.entity;

import lombok.Data;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/9 10:53
 */
@Data
public class JudgeEntity {
    private String input;
    private String output;
    private boolean result;
    private String realOutput;

    public JudgeEntity(String input, String output, boolean result, String realOutput) {
        this.input = input;
        this.output = output;
        this.result = result;
        this.realOutput = realOutput;
    }
}
