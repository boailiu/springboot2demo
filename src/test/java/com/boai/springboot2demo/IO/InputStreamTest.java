package com.boai.springboot2demo.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class InputStreamTest {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream =
                new FileInputStream(new File("D:\\home\\gamma.log"));

        FileOutputStream fileOutputStream =
                new FileOutputStream("D:\\home\\gamma-copy.log");
        StringBuilder builder = new StringBuilder();
        int a;
        while ((a = fileInputStream.read()) != -1) {
            builder.append((char) a);
        }
        fileInputStream.close();
        System.out.println(builder.toString());


        fileOutputStream.write(builder.toString().getBytes());
        fileOutputStream.close();
    }
}
