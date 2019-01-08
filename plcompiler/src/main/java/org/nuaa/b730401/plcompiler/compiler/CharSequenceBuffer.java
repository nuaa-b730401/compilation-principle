package org.nuaa.b730401.plcompiler.compiler;

import org.nuaa.b730401.plcompiler.compiler.bean.ErrorBean;
import org.nuaa.b730401.plcompiler.compiler.bean.LexBean;
import org.nuaa.b730401.plcompiler.compiler.bean.Table;
import org.nuaa.b730401.plcompiler.compiler.constant.ConstWords;
import org.nuaa.b730401.plcompiler.compiler.util.PreProcess;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/7 10:37
 */

public class CharSequenceBuffer {
    /**
     * 字符缓冲区
     */
    private char[] buffer;

    /**
     * 记录当前指向位置
     */
    private int pointer;

    private char currentChar;

    /**
     * 记录当前行号
     */
    private int line = 1;

    private ErrorBean error = null;

    /**
     * 标识符表
     */
    private Table symTable;
    /**
     * 常量表
     */
    private Table cstTable;

    /**
     * init char sequence and optimize
     * @param input input source code
     */
    public CharSequenceBuffer(String input) {
        symTable = new Table();
        cstTable = new Table();
        this.buffer = PreProcess.execute(input);
    }

    public LexBean extractLex() {
        StringBuilder token = new StringBuilder();
        int code;

        getChar();
        getBC();

        // end
        if (currentChar == '\n') {
            return new LexBean(-1, line, "-1");
        }

        // 标识符
        if (Character.isLetter(currentChar)) {
            while (Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
                token.append(currentChar);
                getChar();
            }
            retract();
            String word = token.toString();
            code = reserve(word);
            return code == -1 ?
                    new LexBean(ConstWords.SYM, line, String.valueOf(symTable.insert(word))) :
                    new LexBean(code, line, "-");
        }

        // 常量
        if (Character.isDigit(currentChar)) {
            while (Character.isDigit(currentChar)) {
                token.append(currentChar);
                getChar();
            }
            retract();
            String word = token.toString();
            return new LexBean(ConstWords.CONST, line, String.valueOf(cstTable.insert(word)));
        }

        // 可以直接确定含义的符号
        if (ConstWords.DEFINITE_LABEL_MAP.containsKey(currentChar)) {
            return new LexBean(ConstWords.DEFINITE_LABEL_MAP.get(currentChar), line, "-");
        }

        // < begin
        if (currentChar == '<') {
            getChar();
            if (currentChar == '=') {
                return new LexBean(ConstWords.LESSE, line, "-");
            }
            if (currentChar == '>') {
                return new LexBean(ConstWords.NEQE, line, "-");
            }
            retract();
            return new LexBean(ConstWords.LESS, line, "-");
        }

        // > begin
        if (currentChar == '>') {
            getChar();
            if (currentChar == '=') {
                return new LexBean(ConstWords.LARE, line, "-");
            }
            retract();
            return new LexBean(ConstWords.LAR, line, "-");
        }

        if (currentChar == ':') {
            getChar();
            if (currentChar == '=') {
                return new LexBean(ConstWords.CEQU, line, "-");
            }
            error = new ErrorBean(ErrorBean.INVALID_OPCODE, line, "invalid opcode ':'");
            return null;
        }
        error = new ErrorBean(ErrorBean.INVALID_OPCODE, line, "invalid opcode");
        return null;
    }

    public char getChar() {
        if (hasNext()) {
            currentChar = buffer[pointer++];
        }
        return currentChar;
    }

    public void getBC() {
        while (isEmpty(currentChar) && hasNext()) {
            if (currentChar == '\n') {
                line++;
            }
            getChar();
        }
    }

    public int reserve(String word) {
        if (ConstWords.KEY_WORD_MAP.containsKey(word)) {
            return ConstWords.KEY_WORD_MAP.get(word);
        }
        return -1;
    }

    public void retract() {
        pointer--;
        currentChar = ' ';
    }


    public boolean hasNext() {
        return pointer < buffer.length;
    }

    public boolean isEmpty(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n';
    }

    public boolean error() {
        return error != null;
    }

    public ErrorBean getError() {
        return error;
    }

    public Table getCstTable() {
        return cstTable;
    }

    public Table getSymTable() {
        return symTable;
    }
}
