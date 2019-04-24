package com.boai.springboot2demo.Util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamDebugTest {

    /**
     * IDEA 调试Stream
     * @param args
     */
    public static void main(String[] args) {
        List<Integer> integerList = Arrays.asList(1, 2, 3, 5, 6, 0, 21, 24, 13);
        List<Integer> collect = integerList
                .stream()
                .filter(v -> v % 2 == 1)
                .peek(System.out::println)
                .map(v -> v = v + 1)
                .sorted()
                .collect(Collectors.toList());
        System.out.println(collect);
    }
}
