# 编译原理课设后端说明

## 部署说明

直接在Intellij IDEA中导入即可，运行PlcompilerApplication可以启动程序

## API说明

### 状态码定义

0 : 响应成功

1 : 编译成功

2 : 运行成功

3 : 编译出错

4 : 运行时错误

5 : 等待输入

6 : 系统错误

7 : 测评错误

8 : 时间超限

9 : 堆栈溢出

### 编译

url : /code/compile

method : post

param : {

```
sourceCode : String(源码)
```

}

response : 

- 编译成功

```json
{
  "code": 1,
  "msg": "编译成功",
  "data": null,
  "array": [
    { // 输出形式为 instruction offset deep
      "instruction": "JMP",
      "offset": 1,
      "deep": 0
    },
    {
      "instruction": "INT",
      "offset": 5,
      "deep": 0
    },
    {
      "instruction": "OPR",
      "offset": 16,
      "deep": 0
    },
    {
      "instruction": "STO",
      "offset": 3,
      "deep": 0
    },
    {
      "instruction": "OPR",
      "offset": 16,
      "deep": 0
    },
    {
      "instruction": "STO",
      "offset": 4,
      "deep": 0
    },
    {
      "instruction": "LOD",
      "offset": 3,
      "deep": 0
    },
    {
      "instruction": "LIT",
      "offset": 1,
      "deep": 0
    },
    {
      "instruction": "OPR",
      "offset": 2,
      "deep": 0
    },
    {
      "instruction": "STO",
      "offset": 3,
      "deep": 0
    },
    {
      "instruction": "LOD",
      "offset": 4,
      "deep": 0
    },
    {
      "instruction": "LIT",
      "offset": 2,
      "deep": 0
    },
    {
      "instruction": "OPR",
      "offset": 2,
      "deep": 0
    },
    {
      "instruction": "STO",
      "offset": 4,
      "deep": 0
    },
    {
      "instruction": "LOD",
      "offset": 3,
      "deep": 0
    },
    {
      "instruction": "OPR",
      "offset": 14,
      "deep": 0
    },
    {
      "instruction": "LOD",
      "offset": 4,
      "deep": 0
    },
    {
      "instruction": "OPR",
      "offset": 14,
      "deep": 0
    },
    {
      "instruction": "OPR",
      "offset": 15,
      "deep": 0
    },
    {
      "instruction": "OPR",
      "offset": 0,
      "deep": 0
    }
  ],
  "count": 20,
  "token": "1546953671724"
}
```

- 编译失败

```json
{
  "code": 3,
  "msg": "语法分析error",
  "data": null,
  "array": [
    {
      "type": 0, (错误类型，暂不考虑)
      "line": 1, (错误发生行号)
      "offset": 0, （不考虑）
      "desc": "statement处标识符不合法" (错误描述)
    }
  ],
  "count": 1
}
```

### 运行

url : /code/run

method : post

param :  {

```
token : String(compile 的token)
```

}

response :

- 运行成功

运行结束直接输出output

```json
{
  "code": 2,
  "msg": "运行成功",
  "data": {
    "executeTime": 0,
    "output": "2 3 \n"
  },
  "array": null,
  "count": 1
}
```

- 等待输入

简单说明：每次等待输入都需要先输出data中的output，然后等待用户输入，用户输入完后，执行后续操作

```json
{
  "code": 5,
  "msg": ”等待输入“,
  "data": {
    "executeTime": 0,
    "output": ""
  },
  "array": null,
  "count": 1
} 
```

- 运行时错误

```json
{
  "code": 4,
  "msg": "运行时错误",
  "data": null,
  "array": [
      {
          "type" : 0,
          "line" : 0,
          "desc" : "死循环"
      }  
  ],
  "count": 1
} 
```

- 系统错误

```json
{
  "code": 6,
  "msg": ”缓存失效“,
  "data": null,
  "array": null,
  "count": 1
} 
```



### 输入

url : /code/input

method : post

param : {

```
input : int(输入的整型值),
token : String(compile 的token)
```

}

response : {

```
同运行的所有情况
```

}

### 编译运行

首先发送编译请求，成功后发送运行请求

### 获取符号表

需完成编译

url : /code/symbol

method : get

param : {

```
token : String
```

}

response :

- 获取成功

```json
{"code":0,"msg":"获取符号表数据成功","token":null,"data":null,"array":[{"type":7,"name":"c1","level":0,"value":1,"address":3,"size":4},{"type":9,"name":"b","level":0,"value":0,"address":3,"size":0},{"type":8,"name":"p1","level":0,"value":11,"address":4,"size":0},{"type":7,"name":"c2","level":1,"value":2,"address":3,"size":4},{"type":9,"name":"c","level":1,"value":0,"address":3,"size":0},{"type":8,"name":"p2","level":1,"value":3,"address":4,"size":0},{"type":7,"name":"c3","level":2,"value":3,"address":3,"size":4},{"type":9,"name":"d","level":2,"value":0,"address":3,"size":0}],"count":8}
```

- 缓存失效

```json
{
  "code": 6,
  "msg": ”缓存失效“,
  "data": null,
  "array": null,
  "count": 1
} 
```

### 测评

关于测评系统的demo，只是简易的测评程序，不支持管理添加程序，但是可以通过增加测评数据路径增加题目

测评路径的设置在util/JudgeUtil的JUDGE_FILE_PATH，对应题目的测评数据的输入输出文件为题号id（数字id）.in和.out文件构成（如1.in,1.out）通过增添测评数据即可添加题目

url : /code/judge

method : get

param : {

	id : int(题号)

	token : token(编译后获取的token)

}

response : 

+ 编译、运行错误同上
+ 测评错误包括答案错误、时间超限、输入异常等
+ 测评正确