package com.boai.springboot2demo.Lombok;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class LombokTest {

    public static void main(String[] args) {
        Lombok test = new Lombok(1, "test","testValue");
        log.debug(test.getId().toString());
    }
}
