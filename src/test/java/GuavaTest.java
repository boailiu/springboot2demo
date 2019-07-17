import com.google.common.cache.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
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

        final LoadingCache<Object, Object> build = CacheBuilder
                .newBuilder()
                .maximumSize(10)
                .build(
                        new CacheLoader<Object, Object>() {
                            @Override
                            public Object load(Object o) {
                                return "test";
                            }
                        });

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
}
