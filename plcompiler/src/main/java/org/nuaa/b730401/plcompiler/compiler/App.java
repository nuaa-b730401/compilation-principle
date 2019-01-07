package org.nuaa.b730401.plcompiler.compiler;

import org.nuaa.b730401.plcompiler.compiler.bean.LexBean;
import org.nuaa.b730401.plcompiler.compiler.constant.ConstWords;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
//        FileInputStream fileInputStream = new FileInputStream(App.class.getResource("/").getPath()+"/example/testPL11.txt");
//        Scanner scanner = new Scanner(fileInputStream);
//        StringBuilder sourceCodeBuilder = new StringBuilder();
//        while (scanner.hasNext()) {
//            sourceCodeBuilder.append(scanner.nextLine());
//            sourceCodeBuilder.append("\n");
//        FileInputStream fileInputStream = new FileInputStream("E:\\tmp\\compiling\\PL-0-Compiler-master\\PL-0-Compiler-master\\testPL0\\testPL12.txt");
//        Scanner scanner = new Scanner(fileInputStream);
//        StringBuilder sourceCodeBuilder = new StringBuilder();
//        while (scanner.hasNext()) {
//            sourceCodeBuilder.append(scanner.nextLine());
//            sourceCodeBuilder.append("\n");
//        }
//        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(sourceCodeBuilder.toString());
//        if (lexicalAnalyzer.analyze()) {
//            System.out.println("lex table");
//            for (LexBean lex : lexicalAnalyzer.getLexTable()) {
//                System.out.println(lex);
//            }
//        } else {
//            System.out.println(lexicalAnalyzer.getError().getDesc());
//        }
//        fileInputStream.close();
//        scanner.close();
        long beg = System.currentTimeMillis();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis() - beg) / 1000);
    }
}

