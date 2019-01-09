package org.nuaa.b730401.plcompiler.sevice;

import org.nuaa.b730401.plcompiler.entity.Response;

import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 10:36
 */
public interface CodeService {
    /**
     * 编译并返回目标码，若出错，返回报错信息
     * @param sourceCode 输入源码
     * @return response
     */
    Response compile(String sourceCode);

    /**
     * 运行编译缓存并返回运行结果，若出错，返回出错信息，若需要输入，返回等待状态
     * @param token token
     * @return response
     */
    Response run(String token);

    /**
     * 编译运行，若编译出错，返回错误，若运行出错，返回错误，否则返回编译运行结果
     * @param sourceCode 输入源码
     * @return response
     */
    Response comrun(String sourceCode);

    /**
     * 接受输入，并接续运行程序
     * @param token token
     * @param input 输入
     * @return response
     */
    Response input(String token, int input);

    /**
     * 获取符号表
     * @param token 根据token获取
     * @return
     */
    Response getSymbolTable(String token);

    /**
     * 题目测评
     * @param id 题号
     * @param token token
     * @return 测评结果
     */
    Response judge(int id, String token) throws IOException;
}
