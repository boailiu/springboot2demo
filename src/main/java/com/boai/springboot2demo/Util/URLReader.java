package com.boai.springboot2demo.Util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * URL 读取器，用于读取指定 URL 文档内所有内容<br>
 * 仅支持 Http / Https 协议
 *
 * @author kumasuke120
 * @version 1.6
 * @since 1.8
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class URLReader {
    private static final int BUFFER_SIZE = 4096;

    static {
        dealWithSSLConnection();
    }

    private final HttpURLConnection urlConn;
    private String requestBody;
    private String charsetName = "UTF-8";
    private String bodyCharsetName = "UTF-8";
    private InputStream is;
    private int retryTime = 5;
    private boolean usePost = false;
    private boolean isFinished = false;
    private boolean isSubmitted = false;
    private boolean hasUsedRequestFormData = false;

    /**
     * 构造一个 {@code URLReader} 对象。<br>
     *
     * @param url URL 文档所在地址
     * @throws IOException           URL 连接失败
     * @throws MalformedURLException URL 格式错误
     */
    private URLReader(String url) throws IOException {
        if (!url.matches("https?://.*"))                                // 验证 url
            throw new MalformedURLException("不受支持的连接协议或者 URL 格式错误.\nURL: " + url);

        urlConn = (HttpURLConnection) new URL(url).openConnection();
        urlConn.setConnectTimeout(5000);                                // 设置默认等待延迟
    }

    private static void dealWithSSLConnection() {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            TrustManager[] managers = new TrustManager[]{new IgnoreAllTrustManager()};
            sslContext.init(null, managers, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new AssertionError("不会发生");
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    /**
     * 获取一个 {@code URLReader} 对象，该对象连接到指定 url。<br>
     * 仅支持Http / Https 协议。
     *
     * @param url URL 文档所在地址
     * @return {@code URLReader} 对象
     * @throws IOException           URL 连接失败
     * @throws MalformedURLException URL 格式错误
     */
    public static URLReader connect(String url) throws IOException {
        return new URLReader(url);
    }

    private static String encodeAndFormatEntry(String name, String value) {
        try {
            // 按照 URL 格式对 value 编码并返回，不对 name 进行编码
            return String.format("%s=%s", name, URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException ignored) {
            throw new AssertionError("不该发生");
        }
    }

    public static String encodeAndFormatEntry(Map.Entry<String, String> entry) {
        return encodeAndFormatEntry(entry.getKey(), entry.getValue());
    }

    private void checkSubmit() {
        if (isSubmitted)
            throw new IllegalStateException("请求已被提交");
    }

    /**
     * 设置读取 URL 文档时使用的字符集。<br>
     * 字符集的选择不会影响到{@link URLReader#getInputStream()} 的结果。<br>
     * 如若未设置，则默认为 {@code UTF-8}。
     *
     * @param charsetName 字符集名称
     * @return {@code URLReader} 对象，便于链式编程
     * @throws UnsupportedCharsetException 字符集不支持
     * @throws IllegalStateException       请求已经提交
     */
    public URLReader charset(String charsetName) {
        checkSubmit();

        if (!Charset.isSupported(charsetName))                          // 检查指定的字符集是否支持
            throw new UnsupportedCharsetException("指定的字符集不受支持");

        this.charsetName = charsetName;

        return this;
    }

    /**
     * 设置访问 URL 文档的重试次数。<br>
     * 如若未设置，则默认为 5 次。
     *
     * @param retryTime 重试次数
     * @return {@code URLReader} 对象，便于链式编程
     */
    public URLReader retryTime(int retryTime) {
        this.retryTime = retryTime;
        return this;
    }

    /**
     * 设置访问 URL 文档的等待延迟。<br>
     * 如若未设置，则默认为 5000 {@code ms}。
     *
     * @param timeout 延迟值（单位：{@code ms}），设置 0 代表不限制
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader timeout(int timeout) {
        checkSubmit();

        urlConn.setConnectTimeout(timeout);

        return this;
    }

    /**
     * 设置访问 URL 文档的读取延迟。<br>
     *
     * @param timeout 延迟值（单位：{@code ms}），设置 0 代表不限制
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader readTimeout(int timeout) {
        checkSubmit();

        urlConn.setReadTimeout(timeout);

        return this;
    }

    /**
     * 设置访问 URL 文档的 Http / Https 请求中的 {@code User-Agent} 字段。
     *
     * @param userAgent {@code User-Agent} 字符串
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader userAgent(String userAgent) {
        checkSubmit();

        urlConn.setRequestProperty("User-Agent", requireNonNull(userAgent, "User-Agent 不可为 null"));

        return this;
    }

    /**
     * 设置访问 URL 文档的的 Http / Https 请求中的 {@code Referer} 字段。
     *
     * @param referrer {@code Referer} 字符串
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader referrer(String referrer) {
        checkSubmit();

        urlConn.setRequestProperty("Referer", requireNonNull(referrer, "Referer 不可为 null"));

        return this;
    }

    /**
     * 添加访问 URL 文档的的 Http / Https 请求中的 {@code Cookie} 字段。
     *
     * @param cookie {@code Cookie} 字符串
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader cookie(String cookie) {
        checkSubmit();

        urlConn.addRequestProperty("Cookie", requireNonNull(cookie, "Cookie 不可为 null"));

        return this;
    }

    /**
     * 添加一条或多条访问 URL 文档的的 Http / Https 请求中的 {@code Cookie} 字段。
     *
     * @param cookies {@code Cookie} 字符串集合对象
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader cookies(Collection<String> cookies) {
        checkSubmit();

        cookies.forEach(s -> urlConn.addRequestProperty("Cookie", requireNonNull(s, "Cookie 不可为 null")));

        return this;
    }

    /**
     * 设置访问 URL 文档的的 Http / Https 请求中的 {@code X-Requested-With} 字段。
     *
     * @param xRequestedWith {@code X-Requested-With} 字符串
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader xRequestedWith(String xRequestedWith) {
        checkSubmit();

        urlConn.setRequestProperty("X-Requested-With",
                requireNonNull(xRequestedWith, "X-Requested-With 不可为 null"));

        return this;
    }

    /**
     * 设置访问 URL 文档的的 Http / Https 请求中的 {@code Content-Type} 字段。<br>
     * 仅 POST 方式下可以使用。如未设置，默认值为 {@code application/octet-stream}。
     *
     * @param contentType {@code Content-Type} 字符串
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException       未使用 POST 方式
     * @throws IllegalStateException       请求已经提交
     * @throws UnsupportedCharsetException 字符集无法解析或不受支持
     */
    public URLReader contentType(String contentType) {
        requireUsePost();

        checkSubmit();

        urlConn.setRequestProperty("Content-Type",
                requireNonNull(contentType, "Content-Type 不可为 null"));

        String bodyCharsetName = parseCharsetFromContentType(contentType);
        if (!Strings.isNullOrEmpty(bodyCharsetName)) {
            if (Charset.isSupported(bodyCharsetName)) {
                this.bodyCharsetName = bodyCharsetName;
            } else {
                throw new UnsupportedCharsetException("Content-Type 中的字符集无法解析或不受支持");
            }
        }

        return this;
    }


    private String parseCharsetFromContentType(String contentType) {
        String charset = "";
        int startPos = contentType.toLowerCase().indexOf("charset=");
        startPos = (startPos == -1 ? startPos : startPos + 8);
        if (startPos != -1) {
            StringBuilder tmp = new StringBuilder();
            for (int i = startPos; i < contentType.length(); i++) {
                int c = contentType.codePointAt(i);
                if (Character.isSpaceChar(c)) {
                    break;
                } else {
                    tmp.appendCodePoint(c);
                }
            }

            if (tmp.length() != 0) {
                charset = tmp.toString();
            }
        }
        return charset;
    }

    /**
     * 设置访问 URL 文档的 Http / Https 请求使用 POST 方式。<br>
     * 默认不采用 POST 方式，即使用 GET 方式。
     *
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader usePost() {
        checkSubmit();

        if (!usePost) {
            // 使能向 OutputStream 发送请求
            urlConn.setDoOutput(true);
            // 设置为 POST 请求方式
            try {
                urlConn.setRequestMethod("POST");
            } catch (ProtocolException ignored) {
                throw new AssertionError("不该发生");
            }
            usePost = true;
        }

        return this;
    }

    private void requireUsePost() {
        if (!usePost)
            throw new IllegalStateException("需要使用 POST 方式");
    }

    /**
     * 设置一条访问 URL 文档的 Http / Https 请求的属性，之前设置的同名数据会被覆盖。
     *
     * @param name  请求字段名称
     * @param value 请求字段值
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader requestHeader(String name, String value) {
        checkSubmit();

        urlConn.setRequestProperty(name, requireNonNull(value, name + " 不可为 null"));

        return this;
    }

    /**
     * 设置一条或多条访问 URL 文档的 Http / Https 请求的属性，之前设置的同名数据会被覆盖。
     *
     * @param headers 请求字段 {@code Map} 对象
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader requestHeaders(Map<String, String> headers) {
        checkSubmit();

        headers.forEach((k, v) -> urlConn.setRequestProperty(k, requireNonNull(v, k + " 不可为 null")));

        return this;
    }

    /**
     * 添加一条访问 URL 文档的 Http / Https 请求的表单参数。<br>
     * 只能够在使用 POST 方式时调用，GET 请求数据请直接添加至
     * {@link URLReader#connect(String) connect(String)} 的参数中。
     *
     * @param name  请求属性名称
     * @param value 请求属性值
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 未使用 POST 方式
     * @throws IllegalStateException 请求已经提交
     * @throws IllegalStateException 已经设置请求体
     */
    public URLReader requestFormDatum(String name, String value) {
        requireUsePost();
        checkSubmit();

        // 将表单参数编码连接成 URL 形式
        String encodedString = encodeAndFormatEntry(name, value);

        appendFormDataToRequestBody(encodedString);

        return this;
    }

    /**
     * 添加一条或多条访问 URL 文档的 Http / Https 请求的表单参数。<br>
     * 只能够在使用 POST 方式时调用，GET 请求数据请直接添加至
     * {@link URLReader#connect(String) connect(String)} 的参数中。
     *
     * @param formData 请求参数 {@code Map} 对象
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 未使用 POST 方式
     * @throws IllegalStateException 请求已经提交
     * @throws IllegalStateException 已经设置请求体
     */
    public URLReader requestFormData(Map<String, String> formData) {
        requireUsePost();
        checkSubmit();

        // 将表单参数编码连接成 URL 形式
        String encodedString = formData.entrySet()
                .stream()
                .map(URLReader::encodeAndFormatEntry)
                .collect(Collectors.joining("&"));

        appendFormDataToRequestBody(encodedString);

        return this;
    }

    private void appendFormDataToRequestBody(String encodedString) {
        // 如果当前存在参数，则继续添加，否则直接赋值
        if (!Strings.isNullOrEmpty(requestBody)) {
            if (hasUsedRequestFormData) {
                requestBody += String.format("&%s", encodedString);
            } else {
                throw new
                        IllegalStateException("当前请求体非空，请检查是否已使用 URLReader#requetBody(String) 设置了请求体");
            }
        } else {
            requestBody = encodedString;
            hasUsedRequestFormData = true;
        }
    }

    /**
     * 设置 POST 请求的请求体。
     *
     * @param requestBody 请求体
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IllegalStateException 未使用 POST 方式
     * @throws IllegalStateException 已经设置请求体
     */
    public URLReader requestBody(String requestBody) {
        requireUsePost();

        if (hasUsedRequestFormData)
            throw new IllegalStateException
                    ("已使用 URLReader#requestFormDatum(String, String) 或 " +
                            "URLReader#requestFormData(String, String) 设置了请求体，无法重新设置");
        checkSubmit();

        this.requestBody = requestBody;

        return this;
    }

    /**
     * 提交 URL 访问请求。<br>
     * 如果没有手动调用该方法，将会在获取数据时自动提交。
     *
     * @return {@code URLReader} 对象，便于链式编程
     * @throws IOException           提交请求失败
     * @throws IllegalStateException 请求已经提交
     */
    public URLReader submit() throws IOException {
        checkSubmit();

        boolean finished = false;
        IOException lastException = null;

        final byte[] requestBodyBytes;

        // 如果使用 POST 方式且存在表单数据，设置请求头有且仅有一次
        if (usePost && !Strings.isNullOrEmpty(requestBody)) {
            // 转换请求体内容至字节数组
            Charset bodyCharset = Charset.forName(bodyCharsetName);
            requestBodyBytes = requestBody.getBytes(bodyCharset);

            // 设置 POST 请求相关属性字段
            if (hasUsedRequestFormData) {
                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else {
                String currentContentType = urlConn.getRequestProperty("Content-Type");

                if (Strings.isNullOrEmpty(currentContentType)) { // 设定请求体默认 Content-Type 为 UTF-8
                    urlConn.setRequestProperty("Content-Type", "application/octet-stream");
                }
            }
            urlConn.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
        } else {
            requestBodyBytes = new byte[0];
        }

        int retryCount = 0;
        while (!finished && retryCount < retryTime) {
            try {
                // 如果使用 POST 方式且存在请求体数据
                if (usePost && requestBodyBytes.length != 0) {
                    // 提交请求表单数据
                    OutputStream os = urlConn.getOutputStream();
                    os.write(requestBodyBytes);
                }

                // 获得输入流并存储
                is = urlConn.getInputStream();
                isSubmitted = true;
                finished = true;
            } catch (IOException e) {
                retryCount += 1;
                lastException = e;
            }
        }

        if (Objects.nonNull(lastException)) throw lastException;

        return this;
    }

    private void autoSubmit() throws IOException {
        // 如果当前尚未提交，则进行提交
        if (!isSubmitted)
            submit();
    }

    /**
     * 读取 URL 文档内的内容至 {@code String} 对象中。<br>
     * 如果尚未提交请求，调用该方法会自动调用 {@link URLReader#submit() submit()} 以提交请求。<br>
     * 调用该方法后，将无法再次调用该方法或 {@link URLReader#getReader()} 或 {@link URLReader#getInputStream()}。
     *
     * @return 文档内容字符串
     * @throws IOException           提交请求失败
     * @throws IOException           读取文档失败
     * @throws IllegalStateException 已调用过同类方法，无法再次调用
     */
    public String getText() throws IOException {
        StringBuilder doc = new StringBuilder();

        // 逐字符读取数据并添加到 StringBuilder
        try (Reader in = getReader()) {
            int nChars;
            char[] buffer = new char[BUFFER_SIZE];
            while ((nChars = in.read(buffer)) != -1)
                doc.append(buffer, 0, nChars);
        }

        return doc.toString();
    }

    /**
     * 获取用于读取 URL 文档的 {@code Reader} 对象，便于后续处理。<br>
     * 如果尚未提交请求，调用该方法会自动调用 {@link URLReader#submit() submit()} 以提交请求。<br>
     * 使用完毕后需将 {@code Reader} 对象关闭。<br>
     * 调用该方法后，将无法再次调用该方法或 {@link URLReader#getText()} 或 {@link URLReader#getInputStream()}。
     *
     * @return {@code Reader} 对象
     * @throws IOException           提交请求失败，获取 {@code Reader} 失败
     * @throws IllegalStateException 已调用过同类方法，无法再次调用
     */
    public Reader getReader() throws IOException {
        InputStreamReader isr = new InputStreamReader(getInputStream(), charsetName);

        return new BufferedReader(isr);
    }

    /**
     * 获取用于读取 URL 连接的 {@code InputStream} 对象，便于后续处理。<br>
     * 如果尚未提交请求，调用该方法会自动调用 {@link URLReader#submit() submit()} 以提交请求。<br>
     * 使用完毕后需将 {@code InputStream} 对象关闭。<br>
     * 调用该方法后，将无法再次调用该方法或 {@link URLReader#getText()} 或 {@link URLReader#getReader()}。
     *
     * @return {@code InputStream} 对象
     * @throws IOException           提交请求失败，获取输入流失败
     * @throws IllegalStateException 已调用过同类方法，无法再次调用
     */
    public InputStream getInputStream() throws IOException {
        if (isFinished)
            throw new IllegalStateException("无法再次调用本方法");

        // 可能抛出异常，先提交请求以获取 InputStream，保证 isFinished 值的正确
        autoSubmit();
        isFinished = true;

        return is;
    }

    /**
     * 获取所有访问的 URL 文档返回请求中的 {@code Set-Cookie} 字段。<br>
     * 如果尚未提交请求，调用该方法会自动调用 {@link URLReader#submit() submit()} 以提交请求。
     *
     * @return 返回的 {@code Cookie} 字符串
     * @throws IOException 提交请求失败
     */
    public List<String> getSetCookies() throws IOException {
        autoSubmit();

        List<String> result = new ArrayList<>();

        // 逐条遍历请求字段，查找 Set-Cookie 对应值并存储在 List 中
        String key;
        for (int i = 1; (key = urlConn.getHeaderFieldKey(i)) != null; i++)
            if (key.equals("Set-Cookie")) {
                String value = urlConn.getHeaderField(i);
                result.add(value);
            }

        return result;
    }

    /**
     * 获取所有访问的 URL 文档返回请求中的 {@code Set-Cookie} 字段，去除过期时间等信息并作为单个字符串返回。<br>
     * 该返回的字符串可以直接用于 {@link URLReader#cookie(String)} 的参数。
     *
     * @return 单字符串化的 {@code Cookie} 字符串
     * @throws IOException 提交请求失败
     */
    public String getStringifiedSetCookies() throws IOException {
        List<String> setCookies = getSetCookies();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < setCookies.size(); i++) {
            String e = setCookies.get(i);
            int endPos = e.indexOf(';');
            String cookie = e.substring(0, endPos);

            builder.append(cookie);
            if (i != setCookies.size() - 1)
                builder.append(';');
        }

        return builder.toString();
    }

    /**
     * 获取访问的 URL 文档返回请求中的指定字段。<br>
     * 如果有多条同名的字段，则会返回最后一条。<br>
     * 如果尚未提交请求，调用该方法会自动调用 {@link URLReader#submit() submit()} 以提交请求。
     *
     * @param name 指定字段名称
     * @return 指定字段值
     * @throws IOException 提交请求失败
     */
    public String getHeader(String name) throws IOException {
        autoSubmit();

        return urlConn.getHeaderField(name);
    }

    /**
     * 获取访问的 URL 文档返回请求中的全部字段。<br>
     * 如果有多条同名的字段，则会返回最后一条。<br>
     * 如果尚未提交请求，调用该方法会自动调用 {@link URLReader#submit() submit()} 以提交请求。
     *
     * @return 字段 {@code Map} 对象
     * @throws IOException 提交请求失败
     */
    public Map<String, String> getAllHeaders() throws IOException {
        autoSubmit();

        Map<String, String> result = new HashMap<>();

        String key;
        for (int i = 1; (key = urlConn.getHeaderFieldKey(i)) != null; i++) {
            String value = urlConn.getHeaderField(i);
            result.put(key, value);
        }

        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", urlConn.getURL())
                .add("requestBody", requestBody)
                .add("usePost", usePost)
                .add("isFinished", isFinished)
                .add("isSubmitted", isSubmitted)
                .toString();
    }

    /**
     * 忽略 SSL 证书校验的 TrustManager
     */
    private static class IgnoreAllTrustManager extends X509ExtendedTrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}