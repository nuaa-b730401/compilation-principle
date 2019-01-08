package org.nuaa.b730401.plcompiler.sevice.impl;

import org.nuaa.b730401.plcompiler.compiler.Interpreter;
import org.nuaa.b730401.plcompiler.compiler.LexicalAnalyzer;
import org.nuaa.b730401.plcompiler.compiler.SyntaxAnalyzer;
import org.nuaa.b730401.plcompiler.compiler.bean.ErrorBean;
import org.nuaa.b730401.plcompiler.compiler.bean.ObjectCode;
import org.nuaa.b730401.plcompiler.entity.Response;
import org.nuaa.b730401.plcompiler.sevice.CodeService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 10:36
 */
@Service
public class CodeServiceImpl implements CodeService{
    @Override
    public Response compile(String sourceCode, HttpSession session) {
        // 补换行
        sourceCode += "\n";
        // 词法分析
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        if (!lexicalAnalyzer.analyze()) {
            return new Response<ErrorBean>(
                Response.COMPILE_ERROR_CODE,
                "词法分析error",
                lexicalAnalyzer.getError()
            );
        }

        //  语法、语义分析、翻译
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
        if (syntaxAnalyzer.analysis()) {
            return new Response<ErrorBean>(
                Response.COMPILE_ERROR_CODE,
                "语法分析error",
                syntaxAnalyzer.getErrorList()
            );
        }

        // 编译成功，将编译结果写进缓存
        setLexToSession(lexicalAnalyzer, session);
        setSyntaxToSession(syntaxAnalyzer, session);
        // 返回目标码
        return new Response<ObjectCode>(
                Response.COMPILE_SUCCESS_CODE,
                "编译成功",
                syntaxAnalyzer.getObjectCodeSet().getObjectCodeList()
        );
    }

    @Override
    public Response run(HttpSession session) {
        return null;
    }

    @Override
    public Response comrun(String sourceCode, HttpSession session) {
        return null;
    }

    @Override
    public Response input(int input, HttpSession session) {
        return null;
    }


    private void clearSession(HttpSession session) {
        if (session.getAttribute(session.getId() + "-lex") != null) {
            session.removeAttribute(session.getId() + "-lex");
        }

        if (session.getAttribute(session.getId() + "-syntax") != null) {
            session.removeAttribute(session.getId() + "-syntax");
        }

        if (session.getAttribute(session.getId() + "-interpreter") != null) {
            session.removeAttribute(session.getId() + "-interpreter");
        }
    }

    private LexicalAnalyzer getLexFromSession(HttpSession session) {
        return (LexicalAnalyzer) session.getAttribute(session.getId() + "-lex");
    }

    private SyntaxAnalyzer getSyntaxAnalyzerSession(HttpSession session) {
        return (SyntaxAnalyzer) session.getAttribute(session.getId() + "-syntax");
    }

    private Interpreter getInterpreterFromSession(HttpSession session) {
        return (Interpreter) session.getAttribute(session.getId() + "-interpreter");
    }

    private void setLexToSession(LexicalAnalyzer lex, HttpSession session) {
        session.setAttribute(session.getId() + "-lex", lex);
    }

    private void setSyntaxToSession(SyntaxAnalyzer syntax, HttpSession session) {
        session.setAttribute(session.getId() + "-syntax", syntax);
    }

    private void setInterpreterToSession(Interpreter interpreter, HttpSession session) {
        session.setAttribute(session.getId() + "-interpreter", interpreter);
    }
}
