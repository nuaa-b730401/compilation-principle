package org.nuaa.b730401.plcompiler.controller;

import org.nuaa.b730401.plcompiler.entity.Response;
import org.nuaa.b730401.plcompiler.sevice.CodeService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 10:28
 */
@RestController
@CrossOrigin
@RequestMapping("/code")
public class CodeController {

    private final CodeService codeService;

    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @PostMapping("/compile")
    public Response compile(String sourceCode, HttpSession session) {
        return codeService.compile(sourceCode, session);
    }

    @PostMapping("/run")
    public Response run(HttpSession session) {
        return codeService.run(session);
    }

    @PostMapping("/comrun")
    public Response compileAndRun(String sourceCode, HttpSession session) {
        return codeService.comrun(sourceCode, session);
    }

    @PostMapping("/input")
    public Response input(int input, HttpSession session) {
        return codeService.input(input, session);
    }


}
