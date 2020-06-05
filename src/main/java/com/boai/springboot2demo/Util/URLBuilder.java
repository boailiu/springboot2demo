package com.boai.springboot2demo.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class URLBuilder {
    private StringBuilder url;
    private StringBuilder parameters;

    private URLBuilder(String baseUrl) {
        this.url = new StringBuilder();
        this.parameters = new StringBuilder();
        this.url.append(baseUrl);
    }

    public static URLBuilder baseUrl(String baseUrl) {
        return new URLBuilder(baseUrl);
    }

    public URLBuilder addParameter(String name, Object value) {
        if (parameters.length() != 0) {
            parameters.append("&");
        }

        if (value == null) value = "";

        String encodedName = encodeWithUTF8(name);
        String encodedValue = encodeWithUTF8(String.valueOf(value));
        parameters.append(encodedName).append("=").append(encodedValue);

        return this;
    }

    private String encodeWithUTF8(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("不该发生", e);
        }
    }

    public String build() {
        if (parameters.length() != 0) {
            return url.append("?")
                    .append(parameters.toString())
                    .toString();
        } else {
            return url.toString();
        }
    }
}
