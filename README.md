# pl0编译器实现
NUAA软件工程课程设计，需要完成一个PASCAL语言子集（PL/0）编译器的设计与实现。在这次课程设计中基本完成了相关内容，包括词法分析、语法分析、语义分析、翻译、解释器与应用界面

### pl0简介（BNF描述）

``` 
<prog> → program <id>；<block>

<block> → <condecl>[<proc>]<body>

<condecl> → const <const>{,<const>}

<const> → <id>:=<integer>

<vardecl> → var <id>{,<id>}

<proc> → procedure <id>（[<id>{,<id>}]）;<block>{;<proc>}

<body> → begin <statement>{;<statement>}end

<statement> → <id> := <exp>               

|if <lexp> then <statement>[else <statement>]

               |while <lexp> do <statement>

               |call <id>（[<exp>{,<exp>}]）

               |<body>

               |read (<id>{，<id>})

               |write (<exp>{,<exp>})

<lexp> → <exp> <lop> <exp>|odd <exp>

<exp> → [+|-]<term>{<aop><term>}

<term> → <factor>{<mop><factor>}

<factor>→<id>|<integer>|(<exp>)

<lop> → =|<>|<|<=|>|>=

<aop> → +|-

<mop> → *|/

<id> → l{l|d}   （注：l表示字母）

<integer> → d{d}

注释：
<prog>：程序 ；<block>：块、程序体 ；<condecl>：常量说明 ；<const>：常量；<vardecl>：变量说明 ；<proc>：分程序 ； <body>：复合语句 ；<statement>：语句；<exp>：表达式 ；<lexp>：条件 ；<term>：项 ； <factor>：因子 ；<aop>：加法运算符；<mop>：乘法运算符； <lop>：关系运算符

```

### 项目实现

项目主要可以分为三个部分：编译器实现、后端、前端。








