package org.nuaa.b730401.plcompiler.util;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 21:14
 */
public class Token {
    public static String generateToken() {
        return String.valueOf(System.currentTimeMillis());
    }
}
