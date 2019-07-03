package com.boai.springboot2demo.IO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTest {

    private static final Logger logger = LoggerFactory.getLogger(PathTest.class);

    public static void main(String[] args) {
        //创建绝对路径path
        Path path = Paths.get("D:\\home\\gamma\\logs\\gamma.log");
        logger.info(path.toString());

        Path path2 = Paths.get("D:\\home\\gamma","logs\\gamma.log");
    }
}
