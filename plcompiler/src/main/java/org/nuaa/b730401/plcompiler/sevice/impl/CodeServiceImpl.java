package org.nuaa.b730401.plcompiler.sevice.impl;

import org.nuaa.b730401.plcompiler.compiler.Interpreter;
import org.nuaa.b730401.plcompiler.compiler.LexicalAnalyzer;
import org.nuaa.b730401.plcompiler.compiler.SyntaxAnalyzer;
import org.nuaa.b730401.plcompiler.compiler.bean.ErrorBean;
import org.nuaa.b730401.plcompiler.compiler.bean.ObjectCode;
import org.nuaa.b730401.plcompiler.compiler.bean.SymbolTableItem;
import org.nuaa.b730401.plcompiler.entity.JudgeEntity;
import org.nuaa.b730401.plcompiler.entity.ObjectCodeView;
import org.nuaa.b730401.plcompiler.entity.Response;
import org.nuaa.b730401.plcompiler.entity.RunResultEntity;
import org.nuaa.b730401.plcompiler.sevice.CodeService;
import org.nuaa.b730401.plcompiler.util.Cache;
import org.nuaa.b730401.plcompiler.util.JudgeUtil;
import org.nuaa.b730401.plcompiler.util.Token;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 10:36
 */
@Service
public class CodeServiceImpl implements CodeService{
    @Override
    public Response compile(String sourceCode) {
        String token = Token.generateToken();
        System.out.println("current user : " + token + ", compile");
        // 新的编译清除之前缓存
        clearCache(token);
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
        setLexToCache(token, lexicalAnalyzer);
        setSyntaxToCache(token, syntaxAnalyzer);
        // 返回目标码
        return new Response<ObjectCodeView>(
                Response.COMPILE_SUCCESS_CODE,
                "编译成功",
                syntaxAnalyzer.getObjectCodeSet().getObjectCodeList().stream()
                        .map(ObjectCodeView::new).collect(Collectors.toList()),
                token
        );
    }

    @Override
    public Response run(String token) {
        System.out.println("current user : " + token + ", run");
        SyntaxAnalyzer syntaxAnalyzer = getSyntaxAnalyzerCache(token);
        if (syntaxAnalyzer == null) {
            return new Response(Response.SERVER_ERROR_CODE, "缓存失效");
        }

        // 开始执行
        Interpreter interpreter = new Interpreter(syntaxAnalyzer.getObjectCodeSet().getObjectCodeList());
        interpreter.interpreter(0, false);

        // 运行时错误
        if (interpreter.getErrorList().size() > 0) {
            return new Response<ErrorBean>(Response.RUN_ERROR_CODE, "运行时错误", interpreter.getErrorList());
        }
        // 将interpreter 写入缓存
        setInterpreterToCache(token, interpreter);
        // 等待输入
        if (!interpreter.isEnd()) {
            return new Response<RunResultEntity>(Response.WAIT_INPUT_CODE, "等待输入",
                    new RunResultEntity(interpreter.getExecuteTime(), interpreter.getOutputBuffer().toString()));
        }

        // 运行成功，清空输出缓冲区
        return new Response<RunResultEntity>(Response.RUN_SUCCESS_CODE, "运行成功",
                new RunResultEntity(interpreter.getExecuteTime(), interpreter.getOutputBuffer().toString()));
    }

    @Override
    public Response comrun(String sourceCode) {
        return null;
    }

    @Override
    public Response input(String token, int input) {
        System.out.println("current user : " + token + ", input");
        // 从缓存中获取interpreter，继续执行
        Interpreter interpreter = getInterpreterFromCache(token);

        if (interpreter == null) {
            return new Response(Response.SERVER_ERROR_CODE, "缓存失效");
        }

        // 非法调用直接结束
        if (interpreter.isEnd()) {
            return new Response(Response.RUN_SUCCESS_CODE, "运行已结束");
        }

        // 输入执行
        interpreter.interpreter(input, true);
        // 运行时错误
        if (interpreter.getErrorList().size() != 0) {
            return new Response<ErrorBean>(Response.RUN_ERROR_CODE, "运行时错误", interpreter.getErrorList());
        }
        // 将interpreter 更新缓存
        setInterpreterToCache(token, interpreter);
        // 等待输入
        if (!interpreter.isEnd()) {
            return new Response<RunResultEntity>(Response.WAIT_INPUT_CODE, "等待输入",
                    new RunResultEntity(interpreter.getExecuteTime(), interpreter.getOutputBuffer().toString()));
        }
        // 运行成功，清空输出缓冲区
        return new Response<RunResultEntity>(Response.RUN_SUCCESS_CODE, "运行成功",
                new RunResultEntity(interpreter.getExecuteTime(), interpreter.getOutputBuffer().toString()));
    }

    @Override
    public Response judge(int id, String token) throws IOException {
        System.out.println("current user : " + token + ", judge");
        SyntaxAnalyzer syntaxAnalyzer = getSyntaxAnalyzerCache(token);
        if (syntaxAnalyzer == null) {
            return new Response(Response.SERVER_ERROR_CODE, "缓存失效");
        }
        List<JudgeEntity> judgeResult = new ArrayList<>();

        List<String> inputData = JudgeUtil.getInput(id, "in");
        List<String> outputData = JudgeUtil.getInput(id, "out");
        // 开始执行
        long beg = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            List<Integer> lineInput = Arrays.stream(inputData.get(i)
                    .split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> lineOutput = Arrays.stream(outputData.get(i)
                    .split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
            int inputPointer = 0;

            Interpreter interpreter = new Interpreter(syntaxAnalyzer.getObjectCodeSet().getObjectCodeList());
            int input = 0;
            boolean inputStatus = false;
            StringBuilder output = new StringBuilder();
            while (!interpreter.isEnd()) {
                interpreter.interpreter(input, inputStatus);
                inputStatus = false;
                if (interpreter.getErrorList().size() > 0) {
                    return new Response<ErrorBean>(
                            Response.RUN_ERROR_CODE,
                            "run error",
                            interpreter.getErrorList()
                    );
                }

                output.append(interpreter.getOutputBuffer().toString());

                if (!interpreter.isEnd()) {
                    if (inputPointer > lineInput.size()) {
                        return new Response<JudgeEntity>(
                                Response.JUDGE_ERROR_CODE,
                                "测评错误",
                                judgeResult
                        );
                    }
                    input = lineInput.get(inputPointer++);
                    inputStatus = true;
                }
            }
            if (inputPointer < lineInput.size()) {
                return new Response<JudgeEntity>(
                        Response.JUDGE_ERROR_CODE,
                        "测评错误",
                        judgeResult
                );
            }

            List<Integer> realOutput = Arrays.stream(output.toString()
                    .split("\\s+")).map(Integer::parseInt).collect(Collectors.toList());
            if (realOutput.size() != lineOutput.size()) {
                judgeResult.add(new JudgeEntity(
                        inputData.get(i), outputData.get(i), false, output.toString()
                ));
                return new Response<JudgeEntity>(
                        Response.JUDGE_ERROR_CODE,
                        "测评错误",
                        judgeResult
                );
            }

            for (int j = 0; j < realOutput.size(); j++) {
                if (!realOutput.get(j).equals(lineOutput.get(j))) {
                    judgeResult.add(new JudgeEntity(
                            inputData.get(i), outputData.get(i), false, output.toString()
                    ));
                    return new Response<JudgeEntity>(
                            Response.JUDGE_ERROR_CODE,
                            "测评错误",
                            judgeResult
                    );
                }
            }

            judgeResult.add(new JudgeEntity(
                    inputData.get(i), outputData.get(i), true, output.toString()
            ));
        }
        long executeTime = (System.currentTimeMillis() - beg) / 1000;
        // 10时限
        if (executeTime > 5) {
            return new Response<JudgeEntity>(
                    Response.JUDGE_TIMEOUT_CODE,
                    "时间超限"
            );
        }
        return new Response<JudgeEntity>(
                Response.NORMAL_SUCCESS_CODE,
                "测评正确",
                judgeResult
        );
    }

    @Override
    public Response getSymbolTable(String token) {
        System.out.println("current user : " + token + ", view symbol table");
        SyntaxAnalyzer syntaxAnalyzer = getSyntaxAnalyzerCache(token);
        return syntaxAnalyzer != null ?
                new Response<SymbolTableItem>(
                        Response.NORMAL_SUCCESS_CODE, "获取符号表数据成功",
                        syntaxAnalyzer.getSymbolTable().getSymbolTable()
                ) :
                new Response(Response.SERVER_ERROR_CODE, "缓存失效");
    }

    private void clearCache(String token) {
        if (Cache.getAttribute(token + "-lex") != null) {
            Cache.removeAttribute(token + "-lex");
        }

        if (Cache.getAttribute(token + "-syntax") != null) {
            Cache.removeAttribute(token + "-syntax");
        }

        if (Cache.getAttribute(token + "-interpreter") != null) {
            Cache.removeAttribute(token + "-interpreter");
        }
    }

    private LexicalAnalyzer getLexFromCache(String token) {
        return (LexicalAnalyzer) Cache.getAttribute(token + "-lex");
    }

    private SyntaxAnalyzer getSyntaxAnalyzerCache(String token) {
        return (SyntaxAnalyzer) Cache.getAttribute(token + "-syntax");
    }

    private Interpreter getInterpreterFromCache(String token) {
        return (Interpreter) Cache.getAttribute(token + "-interpreter");
    }

    private void setLexToCache(String token, LexicalAnalyzer lex) {
        Cache.setAttribute(token + "-lex", lex);
    }

    private void setSyntaxToCache(String token, SyntaxAnalyzer syntax) {
        Cache.setAttribute(token + "-syntax", syntax);
    }

    private void setInterpreterToCache(String token, Interpreter interpreter) {
        Cache.setAttribute(token + "-interpreter", interpreter);
    }
}
