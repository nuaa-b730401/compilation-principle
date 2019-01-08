package org.nuaa.b730401.plcompiler.compiler;

import org.nuaa.b730401.plcompiler.compiler.bean.*;

import java.util.ArrayList;
import java.util.List;

import static org.nuaa.b730401.plcompiler.compiler.constant.ConstInstruction.*;
import static org.nuaa.b730401.plcompiler.compiler.constant.ConstWords.*;

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
    private List<LexBean> lexList;

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
    private List<ErrorBean> errorList;

    public SyntaxAnalyzer(LexicalAnalyzer lexicalAnalyzer) {
        lexList = lexicalAnalyzer.getLexTable();
        symbolTable = new SymbolTable();
        objectCodeSet = new ObjectCodeSet();
        errorList = new ArrayList<>();
    }

    /*
     * 非终结符:     prog
     * 对应范式为：<prog> → program <id>；<block>
     * */
    public void prog() {
        if (lexList.get(pos).getId() == PROG) {
            pos++;
            if (lexList.get(pos).getId() == SYM) {
                pos++;
                if (lexList.get(pos).getId() == SEMIC) {
                    pos++;
                    block();
                } else {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有分号"));
                    return;
                }
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有合法的标识符"));
                return;
            }
        } else {
            errorFlag = true;
            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有关键字'program'"));
            return;
        }
    }

    /*
     * 非终结符:     block
     * 对应范式为：<block> → [<condecl>][<vardecl>][<proc>]<body>
     * */
    public void block() {
        //记录本层之前的数据量，以便恢复时返回
        int curAddr = address;
        //记录本层名字的初始位置
        int curTablePos = symbolTable.getPos();
        //记录本层变量的开始位置
        int curLevelProc = 0;
        if (curTablePos > 0) {
            curLevelProc = symbolTable.getLevelProc(level);
            curTablePos -= symbolTable.getRow(curLevelProc).getSize();
        }
        //每一层最开始位置的三个空间用来存放静态链SL、动态链DL、和返回地址RA
        address = curTablePos == 0 ? 3 : 3 + symbolTable.getSymbolTable().get(curLevelProc).getSize();
        int tempPos = objectCodeSet.getPos();
//        暂时填写一条，等待回填
        objectCodeSet.recordObjectCode(JMP, 0, 0);
        if (lexList.get(pos).getId() == CON) {
            condecl();
        }
        if (lexList.get(pos).getId() == VAR) {
            vardecl();
        }
        if (lexList.get(pos).getId() == PROC) {
            proc();
            level--;    //还不清楚意思
        }
//        这个size具体意义是啥
        int size = symbolTable.getSymbolTable().get(curLevelProc).getSize();
        if (curTablePos > 0) {
            for (int i = 0; i < size; i++) {
                objectCodeSet.recordObjectCode(STO, 0, size + 3 - 1 - i);
            }
        }
        objectCodeSet.getObjectCodeList().get(tempPos).setOffset(objectCodeSet.getPos());
        objectCodeSet.recordObjectCode(INT, 0, address);
        if (curTablePos < 0) {
            //将本过程在符号表中的值设为本过程执行语句开始的位置
            symbolTable.getRow(curLevelProc).setValue(objectCodeSet.getPos() - 1 - size);
        }
        body();
        objectCodeSet.recordObjectCode(OPR, 0, 0);
        //分程序结束，恢复相关值
        address = curAddr;
        symbolTable.setPos(curTablePos);
    }

    /*
     * 非终结符:     condecl
     * 对应范式为：<condecl> → const <const>{,<const>}
     * */
    public void condecl() {
        if (lexList.get(pos).getId() == CON) {
            pos++;
            myconst();
            while (lexList.get(pos).getId() == COMMA) {
                pos++;
                myconst();
            }
            if (lexList.get(pos).getId() == SEMIC) {
                pos++;
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有分号"));
                return;
            }
        } else {
            errorFlag = true;
            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有关键字'const'"));
            return;
        }
    }

    /*
     * 非终结符:     const
     * 对应范式为：<const> → <id>:=<integer>
     * */
    public void myconst() {
        String varName = "";
        int value;
        if (lexList.get(pos).getId() == SYM) {
            varName = lexList.get(pos).getValue();
            pos++;
            if (lexList.get(pos).getId() == CEQU) {
                pos++;
                if (lexList.get(pos).getId() == CONST) {
                    value = Integer.parseInt(lexList.get(pos).getValue());
                    if (symbolTable.isDefineNow(varName, level)) {
                        errorFlag = true;
                        errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有分号"));
                        return;
                    }
                    symbolTable.recordConst(varName, level, value, address);
                    pos++;
                }
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有赋值号"));
                return;
            }
        } else {
            errorFlag = true;
            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有合法的标识符"));
            return;
        }
    }

    /*
     * 非终结符:     vardecl
     * 对应范式为：<vardecl> → var <id>{,<id>}
     * */
    public void vardecl() {
        String varName = "";
        int value;
        if (lexList.get(pos).getId() == VAR) {
            pos++;
            if (lexList.get(pos).getId() == SYM) {
                varName = lexList.get(pos).getValue();
                if (symbolTable.isDefineNow(varName, level)) {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "变量重复定义"));
                    return;
                }
                symbolTable.recordVar(varName, level, address);
                //约定一个变量占一个运行空间单元
                address++;
                pos++;
                while (lexList.get(pos).getId() == COMMA) {
                    pos++;
                    if (lexList.get(pos).getId() == SYM) {
                        varName = lexList.get(pos).getValue();
                        if (symbolTable.isDefineNow(varName, level)) {
                            errorFlag = true;
                            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "变量重复定义"));
                            return;
                        }
                        symbolTable.recordVar(varName, level, address);
                        //约定一个变量占一个运行空间单元
                        address++;
                        pos++;
                    } else {
                        errorFlag = true;
                        errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有合法的标识符"));
                        return;
                    }
                }
                if (lexList.get(pos).getId() == SEMIC) {
                    pos++;
                } else {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有分号"));
                    return;
                }
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "没有合法的标识符"));
                return;
            }
        } else {
            errorFlag = true;
            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "变量定义缺少关键字'var'"));
            return;
        }
    }

    /*
     * 非终结符:     proc
     * 对应范式为：<proc> → procedure <id>（[<id>{,<id>}]）;<block>{;<proc>}
     * */
    public void proc() {
        if (lexList.get(pos).getId() == PROC) {
            pos++;
            //参数个数
            int count = 0;
            //记录本层变量的开始位置
            int curLevelProc = 0;
            if (lexList.get(pos).getId() == SYM) {
                String name = lexList.get(pos).getValue();
                if (symbolTable.isDefineNow(name, level)) {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "变量重复定义"));
                    return;
                }
                curLevelProc = symbolTable.getPos();
                symbolTable.recordProc(name, level, address);
                level++;
                pos++;
                if (lexList.get(pos).getId() == LBR) {
                    pos++;
                    if (lexList.get(pos).getId() == SYM) {
                        //3+count+1为形参在存储空间中的位置
                        symbolTable.recordVar(lexList.get(pos).getValue(), level, 3 + count);
                        count++;
                        //用本过程在符号表中的size域记录形参的个数
                        symbolTable.getSymbolTable().get(curLevelProc).setSize(count);
                        pos++;
                        while (lexList.get(pos).getId() == COMMA) {
                            pos++;
                            if (lexList.get(pos).getId() == SYM) {
                                //3+count+1为形参在存储空间中的位置
                                symbolTable.recordVar(lexList.get(pos).getValue(), level, 3 + count);
                                count++;
                                //用本过程在符号表中的size域记录形参的个数
                                symbolTable.getSymbolTable().get(curLevelProc).setSize(count);
                                pos++;
                            } else {
                                errorFlag = true;
                                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "形参传入逗号后需跟随变量"));
                                return;
                            }
                        }
                    }
                    if (lexList.get(pos).getId() == RBR) {
                        pos++;
                        if (lexList.get(pos).getId() == SEMIC) {
                            pos++;
                            block();
                            while (lexList.get(pos).getId() == SEMIC) {
                                pos++;
                                proc();
                            }
                        } else {
                            errorFlag = true;
                            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "过程定义有括号后缺少分号"));
                            return;
                        }
                    } else {
                        errorFlag = true;
                        errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "过程定义缺少右括号"));
                        return;
                    }
                } else {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "过程定义缺少左括号"));
                    return;
                }
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "过程定义缺少标识符"));
                return;
            }
        } else {
            errorFlag = true;
            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "过程定义缺少关键字'procedure'"));
            return;
        }
    }

    /*
     * 非终结符:     body
     * 对应范式为：<body> → begin <statement>{;<statement>}end
     * */
    public void body() {
        if (lexList.get(pos).getId() == BEG) {
            pos++;
            statement();
            while (lexList.get(pos).getId() == SEMIC) {
                pos++;
                statement();
            }
            if (lexList.get(pos).getId() == END) {
                pos++;
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "主体定义缺少关键字'end'"));
                return;
            }
        } else {
            errorFlag = true;
            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "主体定义缺少关键字'begin'"));
            return;
        }
    }

    /*
     * 非终结符:     statement
     * 对应范式为：<statement> → <id> := <exp>
                                    |if <lexp> then <statement>[else <statement>]
                                    |while <lexp> do <statement>
                                    |call <id>[（<exp>{,<exp>}）]
                                    |<body>
                                    |read (<id>{，<id>})
                                    |write (<exp>{,<exp>})
     * */
    public void statement() {
        if (lexList.get(pos).getId() == SYM) {
            String name = lexList.get(pos).getValue();
            pos++;
            if (lexList.get(pos).getId() == CEQU) {
                pos++;
                exp();
                if (!symbolTable.isDefineBefore(name, level)) {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "标识符未定义"));
                    return;
                } else {
                    SymbolTableItem temp = symbolTable.getRow(symbolTable.getNameRow(name));
                    if (temp.getType() == VAR) {
                        //STO L ，a 将数据栈栈顶的内容存入变量
                        objectCodeSet.recordObjectCode(STO, level - temp.getLevel(), temp.getAddress());
                    } else {
                        errorFlag = true;
                        errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "类型不一致"));
                        return;
                    }
                }
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "用':='表示赋值"));
                return;
            }
        } else if (lexList.get(pos).getId() == IF) {
            pos++;
            int curCodePos;
            lexp();
            if (lexList.get(pos).getId() == THEN) {
                //记录当前位置,等待回填
                curCodePos = objectCodeSet.getPos();
                //产生条件转移指令，条件的bool值为0时跳转，跳转的目的地址暂时填为0
                objectCodeSet.recordObjectCode(JPC, 0, 0);
                pos++;
                statement();
                int curCodePos2 = objectCodeSet.getPos();
                objectCodeSet.recordObjectCode(JMP, 0, 0);
                objectCodeSet.getObjectCodeList().get(curCodePos).setOffset(objectCodeSet.getPos());
                objectCodeSet.getObjectCodeList().get(curCodePos2).setOffset(objectCodeSet.getPos());
                //如果 then 之后的语句会跟上;
                if(lexList.get(pos).getId() == SEMIC){
                    pos++;
                }
                if (lexList.get(pos).getId() == ELSE) {
                    pos++;
                    statement();
                    objectCodeSet.getObjectCodeList().get(curCodePos2).setOffset(objectCodeSet.getPos());
                }
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少关键字'then'"));
                return;
            }
        } else if (lexList.get(pos).getId() == WHILE) {
            int curCodePos = objectCodeSet.getPos();
            pos++;
            lexp();
            if (lexList.get(pos).getId() == DO) {
                int curCodePos2 = objectCodeSet.getPos();
                objectCodeSet.recordObjectCode(JPC, 0, 0);
                pos++;
                statement();
                objectCodeSet.recordObjectCode(JMP, 0, curCodePos);
                objectCodeSet.getObjectCodeList().get(curCodePos2).setOffset(objectCodeSet.getPos());
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少关键字'do'"));
                return;
            }
        } else if (lexList.get(pos).getId() == CALL) {
            pos++;
            //用来检验传入的参数和设定的参数是否相等
            int count = 0;
            SymbolTableItem temp;
            if (lexList.get(pos).getId() == SYM) {
                if (symbolTable.isDefineBefore(lexList.get(pos).getValue(), level)) {
                    temp = symbolTable.getRow(symbolTable.getNameRow(lexList.get(pos).getValue()));
                    //call 调用的是一个过程名
                    if (temp.getType() != PROC) {
                        errorFlag = true;
                        errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "类型不一致"));
                        return;
                    }
                } else {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "该变量未定义"));
                    return;
                }
                pos++;
                // TODO: 2019/1/8 这个左括号好像不是必须的,后期调正
                if (lexList.get(pos).getId() == LBR) {
                    pos++;
                    if (lexList.get(pos).getId() == RBR) {
                        pos++;
                        //调用过程中的保存现场由解释程序完成，这里只产生目标代码,+3需详细说明
                        // TODO: 2019/1/8 这里还不理解
                        objectCodeSet.recordObjectCode(CAL, level - temp.getLevel(), temp.getValue());
                    } else {
                        exp();
                        count++;
                        while (lexList.get(pos).getId() == COMMA) {
                            pos++;
                            exp();
                            count++;
                        }
                        if (count != temp.getSize()) {
                            errorFlag = true;
                            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "调用过程传参个数不对"));
                            return;
                        }
                        objectCodeSet.recordObjectCode(CAL, level - temp.getLevel(), temp.getValue());
                        if (lexList.get(pos).getId() == RBR) {
                            pos++;
                        } else {
                            errorFlag = true;
                            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少右括号"));
                            return;
                        }
                    }
                } else {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少左括号"));
                    return;
                }
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少调用过程名"));
                return;
            }
        } else if (lexList.get(pos).getId() == BEG) {
            //body不生成目标代码
            body();
        } else if (lexList.get(pos).getId() == READ) {
            pos++;
            if (lexList.get(pos).getId() == LBR) {
                pos++;
                if (lexList.get(pos).getId() == SYM) {
                    if (!symbolTable.isDefineBefore(lexList.get(pos).getValue(), level)) {
                        errorFlag = true;
                        errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "变量未定义"));
                        return;
                    } else {
                        SymbolTableItem temp = symbolTable.getRow(symbolTable.getNameRow(lexList.get(pos).getValue()));
                        if (temp.getType() == VAR) {
                            //OPR 0 16	从命令行读入一个输入置于栈顶
                            objectCodeSet.recordObjectCode(OPR, 0, 16);
                            //STO L ，a 将数据栈栈顶的内容存入变量（相对地址为a，层次差为L）
                            objectCodeSet.recordObjectCode(STO, level - temp.getLevel(), temp.getAddress());
                        } else {
                            errorFlag = true;
                            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "类型不一致"));
                            return;
                        }
                    }
                    pos++;
                    while (lexList.get(pos).getId() == COMMA) {
                        pos++;
                        if (lexList.get(pos).getId() == SYM) {
                            if (!symbolTable.isDefineBefore(lexList.get(pos).getValue(), level)) {
                                errorFlag = true;
                                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "变量未定义"));
                                return;
                            } else {
                                SymbolTableItem temp = symbolTable.getRow(symbolTable.getNameRow(lexList.get(pos).getValue()));
                                if (temp.getType() == VAR) {
                                    //OPR 0 16	从命令行读入一个输入置于栈顶
                                    objectCodeSet.recordObjectCode(OPR, 0, 16);
                                    //STO L ，a 将数据栈栈顶的内容存入变量（相对地址为a，层次差为L）
                                    objectCodeSet.recordObjectCode(OPR, level - temp.getLevel(), temp.getAddress());
                                } else {
                                    errorFlag = true;
                                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "类型不一致"));
                                    return;
                                }
                            }
                            pos++;
                        } else {
                            errorFlag = true;
                            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "类型不一致"));
                            return;
                        }
                    }
                    if (lexList.get(pos).getId() == RBR) {
                        pos++;
                    } else {
                        errorFlag = true;
                        errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少右括号"));
                        return;
                    }
                } else {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少变量"));
                    return;
                }
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少左括号"));
                return;
            }
        } else if (lexList.get(pos).getId() == WRITE) {
            pos++;
            if (lexList.get(pos).getId() == LBR) {
                pos++;
                exp();
                //输出栈顶的值到屏幕
                objectCodeSet.recordObjectCode(OPR, 0, 14);
                while (lexList.get(pos).getId() == COMMA) {
                    pos++;
                    exp();
                    objectCodeSet.recordObjectCode(OPR, 0, 14);
                }
                //输出换行
                objectCodeSet.recordObjectCode(OPR, 0, 15);
                if (lexList.get(pos).getId() == RBR) {
                    pos++;
                } else {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少右括号"));
                    return;
                }
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少左括号"));
                return;
            }
        } else {
            errorFlag = true;
            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "statement处标识符不合法"));
            return;
        }
    }

    /*
     * 非终结符:     lexp
     * 对应范式为：<lexp> → <exp> <lop> <exp>|odd <exp>
     * */
    public void lexp() {
        if (lexList.get(pos).getId() == ODD) {
            pos++;
            exp();
            objectCodeSet.recordObjectCode(OPR, 0, 6);
        } else {
            exp();
            int lopOper = lop();
            exp();
            if (lopOper == EQU) {
                //OPR 0 8	次栈顶与栈顶是否相等，退两个栈元素，结果值进栈
                objectCodeSet.recordObjectCode(OPR, 0, 8);
            } else if (lopOper == NEQE) {
                //OPR 0 9	次栈顶与栈顶是否不等，退两个栈元素，结果值进栈
                objectCodeSet.recordObjectCode(OPR, 0, 9);
            } else if (lopOper == LESS) {
                //OPR 0 10	次栈顶是否小于栈顶，退两个栈元素，结果值进栈
                objectCodeSet.recordObjectCode(OPR, 0, 10);
            } else if (lopOper == LESSE) {
                // OPR 0 13	次栈顶是否小于等于栈顶，退两个栈元素，结果值进栈
                objectCodeSet.recordObjectCode(OPR, 0, 13);
            } else if (lopOper == LAR) {
                //OPR 0 12	次栈顶是否大于栈顶，退两个栈元素，结果值进栈
                objectCodeSet.recordObjectCode(OPR, 0, 12);
            } else if (lopOper == LARE) {
                //OPR 0 11	次栈顶是否大于等于栈顶，退两个栈元素，结果值进栈
                objectCodeSet.recordObjectCode(OPR, 0, 11);
            }
        }
    }

    /*
     * 非终结符:     exp
     * 对应范式为：<exp> → [+|-]<term>{<aop><term>}
     * */
    public void exp() {
        int tempId = lexList.get(pos).getId();
        if (lexList.get(pos).getId() == ADD || lexList.get(pos).getId() == SUB) {
            pos++;
        }
        term();
        if (tempId == SUB) {
            //  OPR 0 1	栈顶元素取反
            objectCodeSet.recordObjectCode(OPR, 0, 1);
        }
        while (lexList.get(pos).getId() == SUB || lexList.get(pos).getId() == ADD) {
            tempId = lexList.get(pos).getId();
            pos++;
            term();
            if (tempId == ADD) {
                //次栈顶与栈顶相加，退两个栈元素，结果值进栈
                objectCodeSet.recordObjectCode(OPR, 0, 2);
            } else if (tempId == SUB) {
                //次栈顶减去栈顶，退两个栈元素，结果值进栈
                objectCodeSet.recordObjectCode(OPR, 0, 3);
            }
        }
    }

    /*
     * 非终结符:     term
     * 对应范式为：<term> → <factor>{<mop><factor>}
     * */
    public void term() {
        factor();
        while (lexList.get(pos).getId() == MUL || lexList.get(pos).getId() == DIV) {
            int tempId = lexList.get(pos).getId();
            pos++;
            factor();
            if (tempId == MUL) {
                objectCodeSet.recordObjectCode(OPR, 0, 4);
            } else if (tempId == DIV) {
                objectCodeSet.recordObjectCode(OPR, 0, 5);
            }
        }
    }

    /*
     * 非终结符:     factor
     * 对应范式为：<factor>→<id>|<integer>|(<exp>)
     * */
    public void factor() {
        if (lexList.get(pos).getId() == CONST) {
            //数字的话,取常量放入数据栈栈顶
            objectCodeSet.recordObjectCode(LIT, 0, Integer.parseInt(lexList.get(pos).getValue()));
            pos++;
        } else if (lexList.get(pos).getId() == LBR) {
            pos++;
            exp();
            if (lexList.get(pos).getId() == RBR) {
                pos++;
            } else {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "缺少右括号"));
                return;
            }
        } else if (lexList.get(pos).getId() == SYM) {
            String name = lexList.get(pos).getValue();
            //该变量之前未被定义过
            if (!symbolTable.isDefineBefore(name, level)) {
                errorFlag = true;
                errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "该变量未定义"));
                return;
            } else {
                SymbolTableItem temp = symbolTable.getRow(symbolTable.getNameRow(name));
                if (temp.getType() == VAR) {
                    //取变量（相对地址为a，层差为L）放到数据栈的栈顶
                    objectCodeSet.recordObjectCode(LOD, level - temp.getLevel(), temp.getAddress());
                } else if (temp.getType() == CON) {
                    //取常量a放入数据栈栈顶
                    objectCodeSet.recordObjectCode(LIT, 0, temp.getValue());
                } else {
                    errorFlag = true;
                    errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "类型不一致"));
                    return;
                }
            }
            pos++;
        } else {
            errorFlag = true;
            errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "factor处标识符不合法"));
            return;
        }
    }

    /*
     * 非终结符:     lop
     * 对应范式为：<lop> → =|<>|<|<=|>|>=
     * */
    public int lop() {
        //本语法下定义的关系运算符是升序排列
        for (int i = EQU; i <= NEQE; i++) {
            if (lexList.get(pos).getId() == i) {
                pos++;
                return i;
            }
        }
        errorFlag = true;
        errorList.add(new ErrorBean(0, lexList.get(pos).getLine(), "该关系运算符不合法"));
        return -1;
    }

    public boolean analysis() {
        prog();
        //返回错误标志
        return errorFlag;
    }

    public void showObjectCode() {
        objectCodeSet.getObjectCodeList().forEach(objectCode -> {
            System.out.println(INSTRUCTION_SET.get(objectCode.getOpcode())
                    + " " + objectCode.getDeep() + " " + objectCode.getOffset());
        });
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public ObjectCodeSet getObjectCodeSet() {
        return objectCodeSet;
    }

    public List<ErrorBean> getErrorList() {
        return errorList;
    }
}
