package com.boai.springboot2demo.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CompletableFuturesUtils {

    private static final Logger logger = LoggerFactory.getLogger(CompletableFuturesUtils.class);

    private static <V> List<CompletableFuture<V>> executeTasks(List<Callable<V>> callables, Executor executor) {
        return executeTasks(callables, null, executor);
    }

    public static <V> List<CompletableFuture<V>> executeTasks(List<Callable<V>> callables, Integer timeout,
                                                              Executor executor) {
        final List<CompletableFuture<V>> futureList = new ArrayList<>();
        for (Callable<V> run : callables) {
            final CompletableFuture<V> future =
                    CompletableFuture.supplyAsync(() -> {
                        try {
                            return run.call();
                        } catch (Exception e) {
                            logger.error("subTask run failed", e);
                            return null;
                        }
                    }, executor);
            futureList.add(future);
        }

        final CompletableFuture<Void> resultFuture =
                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        try {
            if (timeout != null) {
                resultFuture.get(timeout, TimeUnit.SECONDS);
            } else {
                resultFuture.get();
            }
        } catch (InterruptedException | TimeoutException e) {
            logger.error("CompletableFuture 分任务处理超时", e);
        } catch (ExecutionException e) {
            logger.error("CompletableFuture ExecutionException ", e);
        }
        return futureList;
    }

    public static <V> List<V> getTasksResult(List<Callable<V>> callables, Integer timeout,
                                             Executor executor) {
        final List<CompletableFuture<V>> futureList = executeTasks(callables, timeout, executor);
        return getResults(futureList);
    }

    public static <V> List<V> getTasksResult(List<Callable<V>> callables, Executor executor) {
        final List<CompletableFuture<V>> futureList = executeTasks(callables, executor);
        return getResults(futureList);
    }

    private static <V> List<V> getResults(List<CompletableFuture<V>> futureList) {
        final List<V> results = new ArrayList<>();
        for (CompletableFuture<V> f : futureList) {
            if (f.isDone()) {
                try {
                    final V v = f.get();
                    if (v != null) {
                        results.add(v);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("get subTask result failed", e);
                }
            } else {
                f.cancel(true);
            }
        }
        return results;
    }
}
