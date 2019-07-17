package com.boai.springboot2demo.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static final Pattern pattern = Pattern.compile("(?<date>\\d{4}-\\d{2}-\\d{2})" +
                                                                   "(\\s+)" +
                                                                   "(?<time>\\d{1,2}:\\d{1,2}:\\d{1,2},\\d{1,3})" +
                                                                   "(\\s+)" +
                                                                   "(?<level>[A-Z]+)" +
                                                                   "(\\s+)" +
                                                                   "(?<info>.+)");

    public static RandomAccessFile getLogFile(String logPath) {
        Path path;
        path = Paths.get(logPath);
        if (path == null) return null;
        File logFile = path.toFile();
        RandomAccessFile rw = null;
        try {
            rw = new RandomAccessFile(logFile, "r");
        } catch (IOException e) {
            logger.error("gamma-web 读取日志文件出错", e);
        }
        return rw;
    }

    @SuppressWarnings("Duplicates")
    private static StringBuilder getExceptionContent(Pattern pattern, RandomAccessFile logFile) throws IOException {
        String logDate/*日志打印date*/, logTime/*日志打印time*/,
                logInfo/*日志详细信息date*/, logLevel/*日志级别date*/ = null;
        String temp;
        StringBuilder builder = new StringBuilder();
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

        return builder;
    }


}
