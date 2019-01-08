package org.nuaa.b730401.plcompiler.compiler.bean;

import lombok.Data;

/**
 * @Auther: cyw35
 * @Date: 2019/1/7 19:14
 * @Description:
 */
@Data
public class SymbolTableItem {
    /*
    * 常量、变量或过程类型
    * */
    private int type;
    /*
     * 变量、常量或过程名
     * */
    private String name;
    /*
    * 嵌套层次，最大应为3，不在这里检查其是否小于等于，在SymbolTable中检查
    * */
    private int level;
    /*
    * 表示常量或变量的值
    * */
    private int  value;
    /*
    * 若常量时，则表示数值
    * 否则表示相对于所在嵌套过程基地址的地址
    * */
    private int address;
    /*
    * 编译时所占空间大小
    * */
    private int size;

    public SymbolTableItem(int type, String name, int level, int value, int address, int size) {
        this.type = type;
        this.name = name;
        this.level = level;
        this.value = value;
        this.address = address;
        this.size = size;
    }


}
