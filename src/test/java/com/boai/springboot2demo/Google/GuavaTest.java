package com.boai.springboot2demo.Google;

import com.google.common.cache.*;
import com.google.common.primitives.Doubles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GuavaTest {

    public static void main(String[] args) {
        //创建移除监听器 同步执行
        final RemovalListener<String, Object> removalListener = notification -> {
            final Object value = notification.getValue();
            System.out.println("移除的值" + value);
            System.out.println(notification.getCause());
        };

        //创建异步监听器
        final ExecutorService executorService = Executors.newFixedThreadPool(1);
        final RemovalListener<String, Object> asynchronous =
                RemovalListeners.asynchronous(removalListener, executorService);

        final Cache<String, Object> cache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .removalListener(asynchronous)
                .recordStats() //开启统计功能
                .build();
        cache.put("key1", 1);

/*        System.out.println(cache.stats());
        final Object key1 = cache.getIfPresent("key1");
        System.out.println(key1);

        cache.put("key1", 2);
        System.out.println(cache.stats());

        Thread.sleep(1000 * 8);
        System.out.println(cache.stats());
        final Object key2 = cache.getIfPresent("key1");
        System.out.println(key2);*/

        //get-if-absent-compute
/*        try {
            final Object key1 = cache.get("key2", () -> {
                System.out.println("执行...");
                return 3;
            });
            System.out.println(key1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        //清除
        cache.put("key2", 2);
        cache.put("key3", 3);
        System.out.println(cache.asMap());
        //清除单个键
        cache.invalidate("key2");
        System.out.println(cache.asMap());
        //清除一组
        cache.invalidateAll(Arrays.asList("key2", "key3"));
        System.out.println(cache.asMap());


    }

    @Test
    public void LoadingCacheTest() throws Exception {
        final CacheLoader<String, Map<String, Object>> cacheLoader = new CacheLoader<String, Map<String, Object>>() {
            @Override
            public Map<String, Object> load(String key) {
                return new HashMap<String, Object>() {{
                    put("test", 1);
                    put("test2", 2);
                    put("test3", 3);
                }};
            }
        };
        final LoadingCache<String, Map<String, Object>> loadingCache = CacheBuilder
                .newBuilder()
                .maximumSize(10)
                .recordStats() //开启统计功能
                .build(cacheLoader);
        final Map<String, Object> map = loadingCache.get("test1");
        System.out.println(map);
        System.out.println(map.get("test"));
        System.out.println(map.get("test1"));
        System.out.println();
        System.out.println(loadingCache.asMap());
        final Map<String, Object> map2 = loadingCache.get("test2");
        System.out.println(map2);
        System.out.println(loadingCache.asMap());
        System.out.println("-----------------");
        cacheLoader.reload("test1",map);

        System.out.println(loadingCache.get("test1"));
        loadingCache.refresh("test1");
        System.out.println(loadingCache.get("test1"));
    }

    @Test
    public void primitives(){
        final List<Double> doubles = Doubles.asList(1.2d, 2.4d);
        System.out.println(doubles);
        final Double max = Collections.max(doubles);
        System.out.println(max);
    }

}
