package com.boai.springboot2demo.Concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PhaserTest {

    public static void main(String[] args) {
        Phaser phaser = new Phaser();
        phaser.register();

        int parties = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(parties);
        for (int i = 0; i < parties; i++) {
            phaser.register();
            executorService.execute(() -> {
                int j = 0;
                while (j < 3 && !phaser.isTerminated()) {
                    System.out.println("generation:" + phaser.getPhase());
                    phaser.arriveAndAwaitAdvance();
                    j++;
                }

                phaser.arriveAndDeregister();
            });
        }

        phaser.arriveAndDeregister();
    }
}
