package org.nuaa.b730401.plcompiler.sevice;

import org.nuaa.b730401.plcompiler.entity.Response;

import javax.servlet.http.HttpSession;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 10:36
 */
public interface CodeService {
    /**
     * 编译并返回目标码，若出错，返回报错信息
     * @param sourceCode 输入源码
     * @param session session
     * @return response
     */
    Response compile(String sourceCode, HttpSession session);

    /**
     * 运行编译缓存并返回运行结果，若出错，返回出错信息，若需要输入，返回等待状态
     * @param session session
     * @return response
     */
    Response run(HttpSession session);

    /**
     * 编译运行，若编译出错，返回错误，若运行出错，返回错误，否则返回编译运行结果
     * @param sourceCode 输入源码
     * @param session session
     * @return response
     */
    Response comrun(String sourceCode, HttpSession session);

    /**
     * 接受输入，并接续运行程序
     * @param input 输入
     * @param session session
     * @return response
     */
    Response input(int input, HttpSession session);
}
