package org.nuaa.b730401.plcompiler.compiler;

import org.nuaa.b730401.plcompiler.compiler.bean.ErrorBean;
import org.nuaa.b730401.plcompiler.compiler.bean.LexBean;
import org.nuaa.b730401.plcompiler.compiler.bean.ObjectCode;
import org.nuaa.b730401.plcompiler.compiler.constant.ConstInstruction;
import org.nuaa.b730401.plcompiler.compiler.constant.ConstWords;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/7 9:51
 */
public class App {

    // first step : get source code
    // second step : pre-process , remove empty world , comment and get char sequence to char buffer
    //

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\cyw35\\Desktop\\sample2.pl0");
        Scanner scanner = new Scanner(fileInputStream);
        StringBuilder sourceCodeBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            sourceCodeBuilder.append(scanner.nextLine());
            sourceCodeBuilder.append("\n");
        }
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCodeBuilder.toString());
        if (lexicalAnalyzer.analyze()) {
            System.out.println("lex table");
            for (LexBean lex : lexicalAnalyzer.getLexTable()) {
                System.out.println(lex);
            }
        } else {
            System.out.println(lexicalAnalyzer.getError().getDesc());
        }

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
        if (syntaxAnalyzer.analysis()) {
            System.out.println("compile error");
            for (ErrorBean error : syntaxAnalyzer.getErrorList()) {
                System.out.println(error);
            }
        }

        Interpreter interpreter = new Interpreter(syntaxAnalyzer.getObjectCodeSet().getObjectCodeList());
        int input = 0;
        boolean inputStatus = false;
        Scanner reader = new Scanner(System.in);
        while (!interpreter.isEnd()) {
            interpreter.interpreter(input, inputStatus);
            inputStatus = false;
            if (interpreter.getErrorList().size() > 0) {
                System.out.println("run error");
                for (ErrorBean error : interpreter.getErrorList()) {
                    System.out.println(error);
                }
                break;
            }

            System.out.println(interpreter.getOutputBuffer().toString());

            if (!interpreter.isEnd()) {
                input = reader.nextInt();
                inputStatus = true;
            }
        }

        fileInputStream.close();
        scanner.close();
//        Interpreter interpreter = new Interpreter(new ArrayList<ObjectCode>(){{
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("JMP"), 0, 1));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("INT"), 0, 5));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("LIT"), 0, 1));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("LIT"), 0, 1));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("OPR"), 0, 2));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("STO"), 0, 3));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("LIT"), 0, 2));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("LIT"), 0, 2));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("OPR"), 0, 2));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("STO"), 0, 4));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("LOD"), 0, 3));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("OPR"), 0, 14));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("LOD"), 0, 4));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("OPR"), 0, 14));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("OPR"), 0, 15));
//            add(new ObjectCode(ConstInstruction.INSTRUCTION_MAP.get("OPR"), 0, 0));
//        }});
//        interpreter.interpreter(0, false);
//        System.out.println(interpreter.getOutputBuffer().toString());
    }
}

