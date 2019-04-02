package com.boai.springboot2demo.Util;

import com.boai.springboot2demo.Config.JavaTimeConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.validation.constraints.NotNull;

public class JSONUtil {

    private static final Logger logger_ = LoggerFactory.getLogger(JSONUtil.class);

    public static void put(JSONObject object, String name, Object value) {
        try {
            object.put(name, value);
        } catch (JSONException e) {
            logger_.error("添加json字段出错,jsonObject:" +
                    object + "\n name:" + name + "\n value" + value);
        }
    }

    public static JSONObject parse(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            logger_.warn("字符串转json出错,json:{}", json, e);
        }
        return new JSONObject();
    }

    public static JSONArray parseJSONArray(String json) {
        try {
            return new JSONArray(json);
        } catch (JSONException e) {
            logger_.warn("字符串转json出错,json:{}", json, e);
        }
        return new JSONArray();
    }

    public static String objectToJson(@NotNull Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        return doObjectToJson(object, objectMapper);
    }

    public static String objectToJson2(@NotNull Object object) {
        ObjectMapper objectMapper = JavaTimeConfig.getObjectMapper();
        return doObjectToJson(object, objectMapper);
    }

    private static String doObjectToJson(@NotNull Object object, ObjectMapper objectMapper) {
        String json;
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

        return json;
    }


    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name1", "testName");
            jsonObject.put("name2", "testName2");
            jsonObject.put("name3", "testName3");
            JSONArray jsonArray = new JSONArray();
            jsonObject.put("array", jsonArray);
            jsonArray.put(1);
            jsonArray.put(2);
            jsonArray.put(3);
        } catch (JSONException e) {
            logger_.error("JSON格式化失败", e);
        }

        logger_.info(jsonObject.toString());

        JSONObject test1 = new JSONObject();
        put(test1, "name", "andy");
        logger_.info("test1:" + test1);
        String json = "{\"name\":\"testName\"}";
        String json2 = "{\"name\"}";
        JSONObject parse = parse(json);
        JSONArray jsonArray = parseJSONArray(json2);
        logger_.info("parse:" + parse);
        logger_.info("jsonArray:" + jsonArray);

    }
}
