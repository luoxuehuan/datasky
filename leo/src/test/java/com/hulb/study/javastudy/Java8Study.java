package com.hulb.study.javastudy;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by hulb on 17/3/30.
 */
public class Java8Study {

    //private static Logger logger = LoggerFactory.getLogger(Java8Study.class);


    @Test
    public  void forEachMap() {
       System.out.println("==========================forEachMap=================================");
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

    @Test
    public  void forEachList() {
        System.out.println("==========================forEachList=================================");
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

    @Test
    public  void groupingBy( ){
        System.out.println("==========================groupingBy=================================");

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
    @Test
    public  void sortAndGet(){


//        List<Integer> transactionsIds = transactions.parallelStream().
//                filter(t -> t.getType() == Transaction.GROCERY).
//                sorted(comparing(Transaction::getValue).reversed()).
//                map(Transaction::getId).
//                collect(toList());
//
//
    }

    @Test
    public  void map(){
        System.out.println("==========================map=================================");
        List<Integer> nums = Arrays.asList(1, 2, 3, 4);
        List<Integer> squareNums = nums.stream().
                map(n -> n * n).
                collect(Collectors.toList());

        squareNums.stream().forEach(System.out::println);
    }



}
