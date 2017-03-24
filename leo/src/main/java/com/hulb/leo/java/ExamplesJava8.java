package com.hulb.leo.java;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by hulb on 17/3/23.
 */

/**
 流的操作
 接下来，当把一个数据结构包装成 Stream 后，就要开始对里面的元素进行各类操作了。常见的操作可以归类如下。
 Intermediate：(中间的 transform)
 map (mapToInt, flatMap 等)、 filter、 distinct、 sorted、 peek、 limit、 skip、 parallel、 sequential、 unordered
 Terminal：（终端的 action）
 forEach、 forEachOrdered、 toArray、 reduce、 collect、 min、 max、 count、 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 iterator
 Short-circuiting：（短-环形 回路）
 anyMatch、 allMatch、 noneMatch、 findFirst、 findAny、 limit
 我们下面看一下 Stream 的比较典型用法。
 */
public class ExamplesJava8 {
    public static void main(String[] args) {
        groupingBy();
    }

    public static void forEachMap() {
        Map<String, Integer> items = new HashMap<>();
        items.put("A", 10);
        items.put("B", 20);
        items.put("C", 30);
        items.put("D", 40);
        items.put("E", 50);
        items.put("F", 60);

        items.forEach((k, v) -> System.out.println("Item : " + k + " Count : " + v));

        items.forEach((k, v) -> {
            System.out.println("Item : " + k + " Count : " + v);
            if ("E".equals(k)) {
                System.out.println("Hello E");
            }
        });
    }

    public static void forEachList() {
        List<String> items = new ArrayList<>();
        items.add("A");
        items.add("B");
        items.add("C");
        items.add("D");
        items.add("E");

        //lambda
        //Output : A,B,C,D,E
        items.forEach(item -> System.out.println(item));

        //Output : C
        items.forEach(item -> {
            if ("C".equals(item)) {
                System.out.println(item);
            }
        });

        //method reference
        //Output : A,B,C,D,E
        items.forEach(System.out::println);

        //Stream and filter
        //Output : B
        items.stream().filter(s -> s.contains("B")).findAny().orElse("null");
        items.stream().forEach(System.out::println);
    }

    public static void groupingBy( ){

        List<String> items = Arrays.asList("apple","banala","apple");
        Map<String,Long> result = items.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));

        Map<String,Long> flatMap = new LinkedHashMap<>();
        result.entrySet().stream().sorted(Map.Entry.<String,Long>comparingByValue().reversed()).forEachOrdered(e -> flatMap.put(e.getKey(),e.getValue()));
        System.out.println(flatMap);
    }

    /**
     * Java 8 的排序、取值实现
     *
     * Stream 的并行操作依赖于 Java7 中引入的 Fork/Join 框架（JSR166y）来拆分任务和加速处理过程
     * 1.0-1.4 中的 java.lang.Thread
     * 5.0 中的 java.util.concurrent
     * 6.0 中的 Phasers 等
     * 7.0 中的 Fork/Join 框架
     * 8.0 中的 Lambda
     *
     * 数据源本身可以是无限的。
     */
    public static void sortAndGet(){

        /*
        List<Integer> transactionsIds = transactions.parallelStream().
                filter(t -> t.getType() == Transaction.GROCERY).
                sorted(comparing(Transaction::getValue).reversed()).
                map(Transaction::getId).
                collect(toList());

        */
    }

    public static void map(){
        List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        List<Integer> squareNums = nums.stream().
                map(n -> n * n).
                collect(Collectors.toList());
    }
}
