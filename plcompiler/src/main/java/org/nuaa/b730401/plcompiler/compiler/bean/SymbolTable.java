package org.nuaa.b730401.plcompiler.compiler.bean;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static org.nuaa.b730401.plcompiler.compiler.constant.ConstSymbol.MYCONST;
import static org.nuaa.b730401.plcompiler.compiler.constant.ConstSymbol.PROC;
import static org.nuaa.b730401.plcompiler.compiler.constant.ConstSymbol.VAR;

/**
 * @Auther: cyw35
 * @Date: 2019/1/7 19:14
 * @Description:
 */
@Data
public class SymbolTable {

    private static final int ROWMAX = 10000;

    /**
     * 记录当前已填充位置的下一行
     * 其数值也表示当前已填充行数
     **/
    private int pos = 0;

    /*
     * 符号表
     * */
    private List<SymbolTableItem> symbolTable;

    /*
     * 初始化符号表
     * */
    public SymbolTable() {
        symbolTable = new ArrayList<>();
        symbolTable.forEach(item -> {
            item.setType(0);
            item.setName(null);
            item.setAddress(0);
            item.setLevel(0);
//            为了方便，将常量、变量、过程所占大小统一设置为4，后期可以根据硬件情况具体修改
            // TODO: 2019/1/7
            item.setSize(4);
        });
    }

    /*
     * 记录常量
     * address: 变量的值
     * */
    public void recordConst(String name, int level, int value) {
        symbolTable.get(pos).setType(MYCONST);
        symbolTable.get(pos).setName(name);
        symbolTable.get(pos).setLevel(level);
        symbolTable.get(pos).setAddress(value);
        pos++;
    }

    /*
     * 记录标识符（变量）
     * address:相对于该层次基地址的偏移量（对于基地址将在后面活动记录中详细说明）
     * */
    public void recordVar(String name, int level, int address) {
        symbolTable.get(pos).setType(VAR);
        symbolTable.get(pos).setName(name);
        symbolTable.get(pos).setLevel(level);
        symbolTable.get(pos).setAddress(address);
        pos++;
    }

    /*
     * 记录过程
     * address: 过程处理语句(产生目标代码的操作)的开始地址
     * */
    public void recordProc(String name, int level, int address) {
        symbolTable.get(pos).setType(PROC);
        symbolTable.get(pos).setName(name);
        symbolTable.get(pos).setLevel(level);
        symbolTable.get(pos).setAddress(address);
        pos++;
    }

    /*
     * 是否在lev层之前，包括lev层已被定义
     * */
    public boolean isDefineBefore(String name, int level) {
        for (int i = 0; i < pos; i++) {
            if (symbolTable.get(i).equals(name) && symbolTable.get(i).getLevel() <= level) {
                return true;
            }
        }
        return false;
    }

    /*
     * 是否在lev层已被定义
     * */
    public boolean isDefineNow(String name, int level) {
        for (int i = 0; i < pos; i++) {
            if (symbolTable.get(i).equals(name) && symbolTable.get(i).getLevel() == level) {
                return true;
            }
        }
        return false;
    }

    /*
     * 返回符号表中名字为name所在行的行号
     * */
    public int getNameRow(String name) {
        for (int i = pos - 1; i >= 0; i--) {
            if (symbolTable.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;          //返回-1表示不存在该名字
    }

    /*
    * 返回本层的过程在符号表中的位置
    * */
    public int getLevelProc(int level) {
        for (int i = pos - 1; i >= 0; i--) {
            if (symbolTable.get(i).getType() == PROC) {
                return i;
            }
        }
        return -1;
    }
}
