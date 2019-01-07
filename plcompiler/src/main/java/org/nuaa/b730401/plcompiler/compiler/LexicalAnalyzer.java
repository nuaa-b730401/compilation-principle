package org.nuaa.b730401.plcompiler.compiler;

import org.nuaa.b730401.plcompiler.compiler.bean.ErrorBean;
import org.nuaa.b730401.plcompiler.compiler.bean.LexBean;
import org.nuaa.b730401.plcompiler.compiler.bean.Table;
import org.nuaa.b730401.plcompiler.compiler.constant.ConstWords;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: ToMax
 * @Description: 词法分析器
 * @Date: Created in 2019/1/7 10:14
 */
public class LexicalAnalyzer {
    /**
     * 字符缓冲区
     */
    private CharSequenceBuffer buffer;

    private List<LexBean> lexTable;

    public LexicalAnalyzer(String sourceCode) {
        System.out.println(sourceCode);
        this.buffer = new CharSequenceBuffer(sourceCode);
        lexTable = new LinkedList<>();
    }

    public boolean analyze() {
        while (!buffer.error() && buffer.hasNext()) {
            LexBean lex = buffer.extractLex();
            if (lex != null) {
                lexTable.add(lex);
            } else {
                return false;
            }
        }

        lexTable.forEach(lex -> {
            if (lex.getId() == ConstWords.CONST) {
                lex.setValue(buffer.getCstTable().get(Integer.parseInt(lex.getValue())));
            }
            if (lex.getId() == ConstWords.SYM) {
                lex.setValue(buffer.getSymTable().get(Integer.parseInt(lex.getValue())));
            }
         });

        return !buffer.error();
    }

    public ErrorBean getError() {
        return buffer.getError();
    }

    public Table getSymTable() {
        return buffer.getSymTable();
    }

    public Table getCstTable() {
        return buffer.getCstTable();
    }

    public List<LexBean> getLexTable() {
        return lexTable;
    }
}
