package org.nuaa.b730401.plcompiler.controller;

import org.nuaa.b730401.plcompiler.entity.Response;
import org.nuaa.b730401.plcompiler.sevice.CodeService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public Response compile(String sourceCode) {
        System.out.println("source code : ");
        System.out.println(sourceCode);
        return codeService.compile(sourceCode);
    }

    @PostMapping("/run")
    public Response run(String token) {
        return codeService.run(token);
    }

    @PostMapping("/comrun")
    public Response compileAndRun(String sourceCode) {
        return codeService.comrun(sourceCode);
    }

    @PostMapping("/input")
    public Response input(String token, int input) {
        System.out.println("input : " + input);
        return codeService.input(token, input);
    }

    @GetMapping("/symbol")
    public Response symbol(String token) {
        return codeService.getSymbolTable(token);
    }

    @GetMapping("/judge")
    public Response judge(int id, String token) throws IOException {
        return codeService.judge(id, token);
    }
}
