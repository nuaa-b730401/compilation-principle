package org.nuaa.b730401.plcompiler;

import org.nuaa.b730401.plcompiler.compiler.LexicalAnalyzer;
import org.nuaa.b730401.plcompiler.compiler.SyntaxAnalyzer;

/**
 * @Auther: cyw35
 * @Date: 2019/1/7 19:02
 * @Description:
 */
public class SyntaxAnalyzerTest {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("");
        lexicalAnalyzer.analyze();
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(lexicalAnalyzer);
        syntaxAnalyzer.analysis();
        syntaxAnalyzer.showObjectCode();
    }
}
