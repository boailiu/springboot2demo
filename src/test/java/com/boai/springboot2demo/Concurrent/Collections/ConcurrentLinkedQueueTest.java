package com.boai.springboot2demo.Concurrent.Collections;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentLinkedQueueTest {

    public static void main(String[] args) {
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        queue.add("1");
        queue.add("2");
        queue.add("3");
        System.out.println(queue);
        System.out.println(queue.peek());
        System.out.println(queue);
        queue.offer("4");
        System.out.println(queue);
        String poll = queue.poll();
        System.out.println(poll);
        System.out.println(queue);

    }
}
