package com.boai.springboot2demo.Thread;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class MutilTest {

    private static final Logger logger = LoggerFactory.getLogger(MutilTest.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);
        Future<String> submit = completionService.submit(MutilTest::logTest);
        System.out.println(submit.get());
//        completionService.submit(MutilTest::logTest2);
//        completionService.submit(MutilTest::logTest3);
//        for (int i = 0; i < 1; i++) {
//            Future<String> poll = completionService.take();
//            System.out.println(poll.get());
//        }
//        Future<String> submit = executorService.submit(MutilTest::logTest);
//        Future<String> submit1 = executorService.submit(MutilTest::logTest2);
//        Future<String> submit2 = executorService.submit(MutilTest::logTest2);
//        System.out.println(submit.get());
//        System.out.println(submit1.get());
//        System.out.println(submit2.get());
        executorService.shutdown();
    }

    private static String logTest() {
        logger.info("print ...");
        ExecutorService executorService = Executors.newFixedThreadPool(2,
                new DefaultThreadFactory("log-test"));
        Future<String> submit = executorService.submit(MutilTest::logTest2);
        executorService.shutdown();
        return "print ...";
    }

    private static String logTest2() {
        logger.info("print2 ...");
        return "print2 ...";
    }

    private static String logTest3() {
        logger.info("print3 ...");
        return "print3 ...";
    }
}
