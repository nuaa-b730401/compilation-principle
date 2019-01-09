package org.nuaa.b730401.plcompiler.compiler.exception;

import org.nuaa.b730401.plcompiler.entity.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/9 11:52
 */
@RestControllerAdvice
public class SystemExceptionHandler {

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Response handle(IndexOutOfBoundsException e) {
        return new Response(
                Response.STACK_OVERFLOW_ERROR,
                e.getMessage()
        );
    }
}
