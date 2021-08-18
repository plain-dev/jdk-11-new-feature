# ***Java 11*** 新特性

> ***Java 11*** 包含 ***9 - 11*** 的全部更新

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