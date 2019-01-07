package org.nuaa.b730401.plcompiler.compiler;

import org.nuaa.b730401.plcompiler.compiler.bean.ErrorBean;
import org.nuaa.b730401.plcompiler.compiler.bean.LexBean;
import org.nuaa.b730401.plcompiler.compiler.bean.ObjectCodeSet;
import org.nuaa.b730401.plcompiler.compiler.bean.SymbolTable;

import java.util.List;

import static org.nuaa.b730401.plcompiler.compiler.constant.ConstWords.PROG;
import static org.nuaa.b730401.plcompiler.compiler.constant.ConstWords.SEMIC;
import static org.nuaa.b730401.plcompiler.compiler.constant.ConstWords.SYM;

/**
 * @Auther: cyw35
 * @Date: 2019/1/7 19:05
 * @Description:
 */
public class SyntaxAnalyzer {

    /*
     * 错误标志
     * */
    private boolean errorFlag = false;

    /*
     * 主程序为第0层
     * */
    private int level = 0;

    /*
     * 主程序或变量的声明是为0
     * */
    private int address = 0;

    /*
     * 存储 token
     * */
    private List<LexBean> lexBeanList;

    /**
     * 记录当前已识别token的下一个
     **/
    private int pos = 0;

    /*
     * 符号表
     * */
    private SymbolTable symbolTable;

    /*
     * 目标代码集
     * */
    private ObjectCodeSet objectCodeSet;

    /*
    * 错误列表
    * */
    private List<ErrorBean> errorBeanList;

    public SyntaxAnalyzer() {
        lexBeanList = new LexicalAnalyzer("program p;\n" +
                "\tconst c1:=1,c2:=2;\n" +
                "\tvar v1,v2;\n" +
                "\tbegin\n" +
                "\t\tread(v1,v2);\n" +
                "\t\tv1:=v1+c1;\n" +
                "\t\tv2:=v2+c2;\n" +
                "\t\twrite(v1,v2)\n" +
                "\tend\n" +
                "end\n" +
                "\t").getLexTable();
        symbolTable = new SymbolTable();
        objectCodeSet = new ObjectCodeSet();
    }

    /*
    * 非终结符:     prog
    * 对应范式为：<prog> → program <id>；<block>
    * */
    public void prog(){
        if (lexBeanList.get(pos).getId() == PROG){
            pos ++;
            if(lexBeanList.get(pos).getId() == SYM){
                pos++;
                if(lexBeanList.get(pos).getId() == SEMIC){
                    pos++;
//                    block();
                }else{
                    errorFlag = true;

                }
            }
        }
    }
}
