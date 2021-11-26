# ***Java 11*** 新特性

> JDK 11 于2018年9月25日发布。Java 11 包含如下更新：
> 
> - JEP 181：针对嵌套成员的访问控制
> - JEP 309：动态类文件常量
> - JEP 315：利用 Aarch64 的特有架构改进其上的性能
> - JEP 318：Epsilon：无操作垃圾收集器
> - JEP 320：移除 Java EE 和 CORBA 模块
> - JEP 321：HTTP Client
> - JEP 323：lambda参数的局部变量语法
> - JEP 324：支持 Curve25519 和 Curve 448 密钥
> - JEP 327：Unicode 10
> - JEP 328：添加Java飞行记录器（JFR），其用于创建性能分析记录
> - JEP 329：ChaCha20 和 Poly1305 加密算法
> - JEP 330：运行单文件源码程序
> - JEP 331：低开销堆分析
> - JEP 332：支持 TLS 1.3
> - JEP 333：添加ZGC（一个可扩展的低延迟垃圾收集器）
> - JEP 335：弃用 Nashorn JavaScript 引擎
> - JEP 336：弃用 Pack200 相关的工具及 API
>
> 摘录自[维基百科](https://zh.wikipedia.org/wiki/Java%E7%89%88%E6%9C%AC%E6%AD%B7%E5%8F%B2#Java_SE_11)

## 自动类型推导

```java
var number = 1;
var name = "Java11";
```

## 字符串处理方法

```java
// 是否为空串（包括空格）
" ".isBlank() // true
// 去除首尾空格
" [ Java ] ".strip() // [Java]
// 去除首部空格
" Java ]".stripLeading() // Java ]
//去除尾部空格
"[ Java ] ".stripTrailing() // [ Java ]
// 复制字符串
"Hello".repeat(2) // HelloHello
// 统计行数
"Hello\nJava\n11".lines().count() // 3
```

## 集合加强

新增 `of` 和 `copyOf` 方法

通过该方法创建的集合为不可变集合，不能进行`添加`、`删除`、`替换`、`查询`操作

例子使用 `List` 进行演示，`Set` 和 `Map` 同样有对应方法

```java
var listOf = List.of("a", "b", "c");
var copyOf = List.copyOf(listOf);
listOf == copyOf // true

var newList = new ArrayList<String>();
var copyOf2 = List.copyOf(newList);
newList == copyOf2 // false
```

## ***Stream*** 增强

- 增加单个参数构造方法 `ofNullable`
- 增加 `takeWhileResult` 和 `dropWhileResult` 方法

```java
var count = Stream.ofNullable(null).count(); // 0

var takeWhileResult = Stream.of(1, 2, 3, 2, 1)
        .takeWhile(it -> it < 3)
        .collect(Collectors.toList());
// 当 it < 3 时截止，[1, 2]

var dropWhileResult = Stream.of(1, 2, 3, 2, 1)
        .dropWhile(it -> it < 3)
        .collect(Collectors.toList());
// 当 it < 3 时开始，[3, 2, 1]
```

## ***Optional*** 增强

```java
var result1 = Optional.of("HelloWorld").orElseThrow(); // HelloWorld

// 转为 Stream
var result2 = Optional.of("HelloWorld").stream().count(); // 1

// 为空时替换为其它 Optional
var result3 = Optional.ofNullable(null)
        .or(() -> Optional.of("abc"))
        .get(); // abc
```

## ***InputStream*** 增强

新增 `transferTo` 方法，将 `InputStream` 转为 `OutputStream`

在基础场景下很有用

```java
byteArrayInputStream = new ByteArrayInputStream("# Hello\n\n> Hello Java 11".getBytes(StandardCharsets.UTF_8));
fileOutputStream = new FileOutputStream(tempFile);
var transferToResult = byteArrayInputStream.transferTo(fileOutputStream);
```

## Http Client API

> 代码中演示接口来自 [sspai](https://sspai.com/) 侵删

```java
var request = HttpRequest.newBuilder()
        .uri(URI.create(""))
        .GET()
        .build();
var client = HttpClient.newBuilder()
        .connectTimeout(Duration.of(60, SECONDS))
        .build();

// 同步请求
client.send(request, HttpResponse.BodyHandlers.ofInputStream())

// 异步请求
client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
            .thenAppl(Feature::getResponseBodyJsonStr)
            .thenAccept(result -> );
```