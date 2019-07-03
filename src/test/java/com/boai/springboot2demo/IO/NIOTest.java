package com.boai.springboot2demo.IO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOTest {

    private static final Logger logger = LoggerFactory.getLogger(NIOTest.class);

    /**
     *
     * IO 面向流 阻塞IO
     * NIO 面向缓冲 非阻塞IO
     *
     * 主要操作：文件的复制或者文本内容的读取
     * 创建文件的操作 Path替换File
     *
     */

    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("D:\\home\\gamma\\logs\\gamma.log", "rw");
        FileOutputStream out = new FileOutputStream(new File("D:\\home\\gamma\\logs\\gammaCopy.log"));
        FileChannel outChannel = out.getChannel();
        FileChannel inChannel = file.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(48); // create buffer with capacity of 48 bytes

        int read = inChannel.read(buffer); //read into buffer

        while (read != -1) {
            buffer.flip(); //make buffer ready for read
            outChannel.write(buffer);
/*            while (buffer.hasRemaining()) {
                logger.info(String.valueOf((char) buffer.get()));
            }*/

            buffer.clear(); //make buffer ready for writing
            read = inChannel.read(buffer);
        }

        file.close();

    }
}
