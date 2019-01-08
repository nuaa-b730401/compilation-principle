package org.nuaa.b730401.plcompiler.compiler.constant;

import org.nuaa.b730401.plcompiler.compiler.bean.LexBean;

import java.util.*;

/**
 * @Author: ToMax
 * @Description: define labels of pl0
 * @Date: Created in 2019/1/7 9:52
 */
public class ConstWords {
    // key word
    /**
     * program
     */
    public static final int PROG = 1;
    /**
     * begin
     */
    public static final int BEG = 2;
    public static final int END = 3;
    public static final int IF = 4;
    public static final int THEN = 5;
    public static final int ELSE = 6;
    /**
     * const
     */
    public static final int CON = 7;
    /**
     * procedure
     */
    public static final int PROC = 8;
    public static final int VAR = 9;
    public static final int DO = 10;
    public static final int WHILE = 11;
    public static final int CALL = 12;
    public static final int READ = 13;
    public static final int WRITE = 14;
    /**
     * repeat
     */
    public static final int REP = 15;
    public static final int ODD = 16;


    // operator
    // compare operator
    /**
     * equal
     */
    public static final int EQU = 17;
    /**
     * less then
     */
    public static final int LESS = 18;
    /**
     * less or equal
     */
    public static final int LESSE = 19;
    /**
     * large then
     */
    public static final int LAR = 20;
    /**
     * large or equal
     */
    public static final int LARE = 21;
    /**
     * not equal
     */
    public static final int NEQE = 22;

    // calculate operator
    public static final int ADD = 23;
    public static final int SUB = 24;
    public static final int MUL = 25;
    public static final int DIV = 26;

    /**
     * a := b
     */
    public static final int CEQU = 27;
    /**
     *
     */
    public static final int COMMA = 28;
    /**
     * ;
     */
    public static final int SEMIC = 29;
    /**
     * .
     */
    public static final int POI = 30;
    /**
     * (
     */
    public static final int LBR = 31;
    /**
     * )
     */
    public static final int RBR = 32;

    // other
    /**
     * 标识符
     */
    public static final int SYM = 33;
    /**
     * 常量值
     */
    public static final int CONST = 34;

    /**
     * 关键字字典
     */
    public static final Map<String, Integer> KEY_WORD_MAP = new LinkedHashMap<String, Integer>(){{
        put("program", PROG);
        put("begin", BEG);
        put("end", END);
        put("if", IF);
        put("then", THEN);
        put("else", ELSE);
        put("const", CON);
        put("procedure", PROC);
        put("var", VAR);
        put("do", DO);
        put("while", WHILE);
        put("call", CALL);
        put("read", READ);
        put("write", WRITE);
        put("repeat", REP);
        put("odd", ODD);
    }};

    public static final List<String> WORDS_LIST = new LinkedList<String>(){{
        add("program");
        add("begin");
        add("end");
        add("if");
        add("then");
        add("else");
        add("const");
        add("procedure");
        add("var");
        add("do");
        add("while");
        add("call");
        add("read");
        add("write");
        add("repeat");
        add("odd");
    }};

    public static final Map<Character, Integer> DEFINITE_LABEL_MAP = new HashMap<Character, Integer>(){{
        put('=', EQU);
        put('+', ADD);
        put('-', SUB);
        put('*', MUL);
        put('/', DIV);
        put(',', COMMA);
        put(';', SEMIC);
        put('.', POI);
        put('(', LBR);
        put(')', RBR);
    }};

    public static final Map<Integer, String> LABEL_MAP = new HashMap<Integer, String>(){{
        put(PROG, "program");
        put(BEG, "begin");
        put(END, "end");
        put(IF , "if");
        put(THEN, "then");
        put(ELSE, "else");
        put(CON, "const");
        put(PROC, "procedure");
        put(VAR, "var");
        put(DO, "do");
        put(WHILE, "while");
        put(CALL, "call");
        put(READ, "read");
        put(WRITE, "write");
        put(REP, "repeat");
        put(ODD, "odd");
        put(EQU, "=");
        put(NEQE, "<>");
        put(ADD, "+");
        put(SUB, "-");
        put(MUL, "*");
        put(DIV, "/");
        put(CEQU, ":=");
        put(COMMA, ",");
        put(SEMIC, ";");
        put(POI, ".");
        put(LBR, "(");
        put(RBR, ")");
        put(SYM, "sym label");
        put(CONST, "const label");
    }};
}
