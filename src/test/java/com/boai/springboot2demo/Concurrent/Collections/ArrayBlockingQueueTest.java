package com.boai.springboot2demo.Concurrent.Collections;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ArrayBlockingQueueTest {

    /**
     * 有界阻塞队列 基于数组
     * 先进先出
     * 容器大小确定后不可改变
     */

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
//        queue.put("1");
//        queue.put("2");
//        queue.put("3");
//        queue.put("4");
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");
        System.out.println(queue);

    }
}
