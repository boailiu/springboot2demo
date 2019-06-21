package com.boai.springboot2demo.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class GZIPFileUtil {

    private static Logger logger = LoggerFactory.getLogger(GZIPFileUtil.class);

    private static int lastIndex = 102400;

    private static final Pattern pattern = Pattern.compile("(?<date>\\d{4}-\\d{2}-\\d{2})" +
                                                                   "(\\s+)" +
                                                                   "(?<time>\\d{1,2}:\\d{1,2}:\\d{1,2},\\d{1,3})" +
                                                                   "(\\s+)" +
                                                                   "(?<level>[A-Z]+)" +
                                                                   "(\\s+)" +
                                                                   "(?<info>.+)");

    /**
     * 解压文件
     */
    public static void doUnCompressFile(String fileDir) {
        String compressFilePath = getLastestLogFile(fileDir);
        if (compressFilePath == null) return;
        Path path = Paths.get(compressFilePath);
        GZIPInputStream gzipInput = null;
        try {
            gzipInput = new GZIPInputStream(new FileInputStream(path.toFile()));
        } catch (IOException e) {
            logger.error("读取压缩文件出错", e);
        }

        FileOutputStream out = null;
        String outFileName = "D:\\home\\gamma\\new\\logs\\gamma-2019-06-19-02-1.log";
        try {
            out = new FileOutputStream(outFileName);
        } catch (FileNotFoundException e) {
            logger.error("初始化输出文件失败", e);
        }

        byte[] buf = new byte[1024];
        int len;
        try {
            assert gzipInput != null;
            while ((len = gzipInput.read(buf)) > 0) {
                assert out != null;
                out.write(buf, 0, len);
//                logger.info(new String(buf, StandardCharsets.UTF_8));
            }
            gzipInput.close();
            assert out != null;
            out.close();
        } catch (Exception e) {
            logger.error("读取文件出错", e);
        }

        File file = new File(outFileName);
        try {
            RandomAccessFile logFile = new RandomAccessFile(file, "r");
            logFile.seek(lastIndex);
            StringBuilder builder = new StringBuilder();
            String logDate/*日志打印date*/, logTime/*日志打印time*/, logThreadName/*日志线程名称date*/,
                    logInfo/*日志详细信息date*/, logLevel/*日志级别date*/ = null;
            String temp;
            while ((temp = logFile.readLine()) != null) {
                temp = new String(temp.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                Matcher matcher = pattern.matcher(temp);
                if (matcher.matches()) {
                    logLevel = matcher.group("level");
                    if (logLevel.equals("ERROR")) {
                        logDate = matcher.group("date");
                        logTime = matcher.group("time");
                        logInfo = matcher.group("info");
                        builder.append(logDate).append(" ").append(logTime)
                                .append("<br>")
                                .append(logInfo)
                                .append("<br>");
                    }
                } else {
                    if ("ERROR".equals(logLevel)) {
                        builder.append(temp).append("\r").append("<br>");
                    }
                }

            }
            logFile.close();
            file.delete();

            if (builder.length() > 0) {
//                logger.error("gamma-web 错误信息概要:" + builder.toString());
                String emailContent = new String(builder.toString().getBytes(), StandardCharsets.UTF_8);
                String emailAddress = "baliu@che300.com";
                logger.info("gamma-web 错误信息概要:");
                logger.info(emailContent);
                EmailUtil.sendMail(emailAddress, LocalDateTime.now() + " 压缩日志exception", emailContent);
            } else {
                logger.info("gamma-web log no error");
            }
        } catch (IOException e) {
            logger.error("读取解压缩之后的日志文件出错", e);
        }
    }

    /**
     * 获取文件的创建时间
     *
     * @param filePath
     */
    public static LocalDateTime getFileCreateTime(String filePath) {
        Path path = Paths.get(filePath);
        BasicFileAttributeView basicView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        try {
            BasicFileAttributes attrs = basicView.readAttributes();
            FileTime fileTime = attrs.creationTime();
            logger.info("文件创建时间:" + fileTime);

            long fileTimeMillis = fileTime.toMillis();
            LocalDateTime fileCreateTime =
                    LocalDateTime.ofEpochSecond(fileTimeMillis / 1000, 0,
                                                ZoneOffset.ofHours(8));
            logger.info("文件创建时间:" + fileCreateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return fileCreateTime;


        } catch (IOException e) {
            logger.error("获取文件属性失败", e);
        }

        return null;

    }

    /**
     * 获取最新的压缩日志文件
     *
     * @param fileDir
     * @return
     */
    public static String getLastestLogFile(String fileDir) {
        String lastestFilePath = null;
        LocalDateTime lastestFileCreateTime = null;
        File file = new File(fileDir);
        File[] files = file.listFiles();
        if (files == null) return null;
        for (File fileItem : files) {
            logger.info(fileItem.getAbsolutePath());
            LocalDateTime fileCreateTime = getFileCreateTime(fileItem.getAbsolutePath());
            if (lastestFilePath == null) {
                lastestFilePath = fileItem.getAbsolutePath();
                lastestFileCreateTime = fileCreateTime;
            } else if (fileCreateTime != null && lastestFileCreateTime != null
                    && fileCreateTime.isAfter(lastestFileCreateTime)) {
                lastestFilePath = fileItem.getAbsolutePath();
                lastestFileCreateTime = fileCreateTime;
            }
        }
        return lastestFilePath;
    }

    public static void main(String[] args) {
//        doUnCompressFile();
//        getFileCreateTime("D:\\home\\gamma\\new\\logs\\gamma-2019-06-19-02-1.log.gz");
        getLastestLogFile("D:\\home\\gamma\\new\\logs");
    }
}
