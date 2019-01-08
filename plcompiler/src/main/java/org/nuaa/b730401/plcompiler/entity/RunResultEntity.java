package org.nuaa.b730401.plcompiler.entity;

import lombok.Data;
import org.nuaa.b730401.plcompiler.compiler.bean.ErrorBean;

import java.util.List;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 10:40
 */
@Data
public class RunResultEntity {
    private long executeTime;
    private String output;

    public RunResultEntity(long executeTime, String output) {
        this.executeTime = executeTime;
        this.output = output;
    }
}
