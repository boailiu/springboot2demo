package com.boai.springboot2demo.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

public class JavaTimeConfig {

    private static final DateTimeFormatter globalDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter globalDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Supplier<DateFormat> globalLegacyDateFormat = () -> new SimpleDateFormat("yyyy-MM-dd");
    private static final Supplier<DateFormat> globalLegacyDateTimeFormat =
            () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = newObjectMapper();
        return objectMapper;
    }

    private static ObjectMapper newObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(globalDateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(globalDateFormatter));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(globalDateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(globalDateTimeFormatter));
        javaTimeModule.addSerializer(java.sql.Date.class,
                new DateSerializer(false, globalLegacyDateFormat.get()));
        javaTimeModule.addSerializer(java.sql.Timestamp.class,
                new DateSerializer(false, globalLegacyDateTimeFormat.get()));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.setDateFormat(globalLegacyDateFormat.get());
        return objectMapper;
    }
}
