package com.boai.springboot2demo.Concurrent.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LinkedBlockingQueueTest {

    /**
     * 有界阻塞队列 基于链表
     * 先进先出
     */
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(1);
        queue.offer("1");
        queue.offer("2");
        System.out.println(queue.offer("3"));
/*        Collection<String> objects = new ArrayList<>();
        int i = queue.drainTo(objects);
        System.out.println(i);
        System.out.println(objects);*/
        String take = queue.take();
        System.out.println(take);
        String poll = queue.poll(1, TimeUnit.SECONDS);
        System.out.println(poll);
    }
}
