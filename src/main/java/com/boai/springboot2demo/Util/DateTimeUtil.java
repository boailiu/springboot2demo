package com.boai.springboot2demo.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public class DateTimeUtil {

    private static final Logger logger_ = LoggerFactory.getLogger(DateTimeUtil.class);

    public static void main(String[] args) {
        String formatDate = formatDate("yyyy-MM", LocalDate.now());
        logger_.info(formatDate);

        String formatDateTime = formatDateTime("yyyy-MM-dd HH:mm:ss", LocalDateTime.now());
        logger_.info(formatDateTime);

        LocalDate localDate = parseDate("2019-03-01", "yyyy-MM-dd");
        logger_.info(localDate.toString());

        LocalDateTime localDateTime = parseDateTime("2019-03-01 11:22:33", "yyyy-MM-dd HH:mm:ss");
        logger_.info(localDateTime.toString());

        String yyyyMM = formatTemporal("yyyyMM", LocalDate.now());
        logger_.info(yyyyMM);

    }

    public static String formatDate(String ofPattern, LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ofPattern);
        return date.format(dateTimeFormatter);
    }

    public static String formatDateTime(String ofPattern, LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ofPattern);
        return time.format(formatter);
    }

    public static LocalDate parseDate(String date, String ofPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ofPattern);
        return LocalDate.parse(date, formatter);
    }

    public static LocalDateTime parseDateTime(String dateTime, String ofPattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ofPattern);
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static String formatTemporal(String ofPattern, Temporal temporal) {
        if (temporal == null) {
            return "";
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ofPattern);
            return formatter.format(temporal);
        }

    }
}
