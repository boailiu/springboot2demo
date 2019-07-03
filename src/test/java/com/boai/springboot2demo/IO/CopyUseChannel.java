package com.boai.springboot2demo.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class CopyUseChannel {

    public static void main(String[] args) throws IOException {
        //通道之间的数据传输
        // position处开始 count最大传输数 target目标文件
        FileInputStream ins = new FileInputStream(new File("D:\\home\\gamma\\logs\\gamma.log"));
        File file = new File("D:\\home\\gamma\\logs\\gamma_copy.log");
        if(!file.exists()){
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);
        FileChannel outChannel = out.getChannel();
        FileChannel insChannel = ins.getChannel();
        insChannel.transferTo(0,insChannel.size(),outChannel);
        outChannel.close();
        insChannel.close();
        ins.close();
        out.close();

    }
}
