/**
 * MIT License
 *
 * Copyright (c) 2021 Plain
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * 演示 Java 11 新特性
 * <p>
 * 参考 https://juejin.cn/post/6844903698481561614
 */
public class Feature {

    /**
     * 特性一：自动类型推导
     */
    private static void feature1() {
        var number = 1;
        var name = "Java11";
        System.out.println("number is " + number);
        System.out.println("name = " + name + ", class is " + name.getClass().getSimpleName());
    }

    /**
     * 特性二：{@link String} 新增方法
     */
    private static void feature2() {
        System.out.println("判断是否为空串 \" \".isBlank() result is " + " ".isBlank());
        System.out.println("去除首尾空格 \" [ Java ] \".strip() result is " + " [ Java ] ".strip());
        System.out.println("去除首部空格 \" Java ]\".stripLeading() result is " + " Java ]".stripLeading());
        System.out.println("去除尾部空格 \"[ Java ] \".stripTrailing() result is " + "[ Java ] ".stripTrailing());
        System.out.println("复制字符串 \"Hello\".repeat() result is " + "Hello".repeat(2));
        System.out.println("统计行数 \"Hello\\nJava\\n11\".lines().count() result is " + "Hello\nJava\n11".lines().count());
    }

    /**
     * 特性三：{@link java.util.Collection} 集合加强
     * <p>
     * 新增 {@link List#of} 和 {@link List#copyOf} 方法
     * 通过该方法创建的集合为不可变集合，不能进行添加、删除、替换、查询操作
     * <p>
     * 例子使用 {@link List} 进行演示，{@link java.util.Set} 和 {@link java.util.Map} 同样有对应方法
     */
    private static void feature3() {
        var listOf = List.of("a", "b", "c");
        var copyOf = List.copyOf(listOf);
        System.out.println("listOf == copyOf ? " + (listOf == copyOf));

        var newList = new ArrayList<String>();
        var copyOf2 = List.copyOf(newList);
        System.out.println("newList == copyOf2 ? " + (newList == copyOf2));
    }

    /**
     * 特性四：{@link Stream} 增强
     */
    private static void feature4() {
        // 增加单个参数构造方法 ofNullable
        var count = Stream.ofNullable(null).count();
        System.out.println("Stream.ofNullable(null).count() result is " + count);
        // 增加 takeWhileResult 和 dropWhileResult 方法
        // 当 it < 3 时截止
        var takeWhileResult = Stream.of(1, 2, 3, 2, 1)
                .takeWhile(it -> it < 3)
                .collect(Collectors.toList());
        // 当 it < 3 时开始
        var dropWhileResult = Stream.of(1, 2, 3, 2, 1)
                .dropWhile(it -> it < 3)
                .collect(Collectors.toList());
        System.out.println("takeWhileResult(it -> it < 3) result is " + takeWhileResult);
        System.out.println("dropWhileResult(it -> it < 3) result is " + dropWhileResult);
    }

    /**
     * 特性五：{@link Optional} 增强
     */
    private static void feature5() {
        var result1 = Optional.of("HelloWorld").orElseThrow();
        System.out.println("Optional.of(\"HelloWorld\").orElseThrow() result is " + result1);
        var result2 = Optional.of("HelloWorld").stream().count();
        System.out.println("Optional.of(\"HelloWorld\").stream().count() result is " + result2);
        var result3 = Optional.ofNullable(null)
                .or(() -> Optional.of("abc"))
                .get();
        System.out.println("or(() -> Optional.of(\"abc\")) result is " + result3);
    }

    /**
     * 特性六：{@link InputStream} 增强
     * <p>
     * 新增 {@link InputStream#transferTo} 方法
     */
    private static void feature6() {
        ByteArrayInputStream byteArrayInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            File tempFile = new File("./src/main/resources/assets/hello.md");
            var isDelete = false;
            var isCreate = false;
            if (tempFile.exists()) isDelete = tempFile.delete();
            if (isDelete) isCreate = tempFile.createNewFile();
            if (!isCreate) return;
            byteArrayInputStream = new ByteArrayInputStream("# Hello\n\n> Hello Java 11".getBytes(StandardCharsets.UTF_8));
            fileOutputStream = new FileOutputStream(tempFile);
            var transferToResult = byteArrayInputStream.transferTo(fileOutputStream);
            System.out.println("transferToResult " + transferToResult);
            System.out.println("Open file " + tempFile.getAbsolutePath() + " to view");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert byteArrayInputStream != null;
                byteArrayInputStream.close();
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 特性七：Http Client API
     */
    private static void feature7() {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://sspai.com/api/v1/article/index/page/get?limit=10&offset=0&created_at=0"))
                .GET()
                .build();
        var client = HttpClient.newBuilder()
                .connectTimeout(Duration.of(60, SECONDS))
                .build();
        try {
            // execute
            var responseByExecute = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            String bodyJsonStr = getResponseBodyJsonStr(responseByExecute);
            System.out.println("responseByExecute result is " + bodyJsonStr);

            AtomicBoolean lock = new AtomicBoolean(true);

            // enqueue
            client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                    .thenApply(Feature::getResponseBodyJsonStr)
                    .thenAccept(result -> {
                        lock.set(false);
                        System.out.println("responseByEnqueue result is " + result);
                    });

            while (lock.get()) {
                // empty
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getResponseBodyJsonStr(@NotNull HttpResponse<InputStream> responseByExecute) {
        var bodyJsonStr = "";
        try (var bodyOutPutStream = new ByteArrayOutputStream(); var bodyInputStream = responseByExecute.body()) {
            bodyInputStream.transferTo(bodyOutPutStream);
            bodyJsonStr = bodyOutPutStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bodyJsonStr;
    }

    public static void main(String[] args) {
        feature1();
        feature2();
        feature3();
        feature4();
        feature5();
        feature6();
        feature7();
    }

}
