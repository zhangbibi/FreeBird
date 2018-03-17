package com.freebird.common.http;

import com.google.gson.Gson;
import com.threeguys.common.util.LogUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 网络层httpclient封装
 */
public class NetUtil {
    private int socket_timeout = 8000;
    private int connect_timeout = 8000;
    private int connect_request_timeout = 2000;
    private int connect_max_total = 800;
    private int connect_max_per_route = 500;
    private PoolingHttpClientConnectionManager poolConnManager = null;
    private Logger log = LogUtils.getLogger();
    private Gson gson = new Gson();
    private String UTF8 = "utf-8";
    private IdleConnectionMonitorThread idleConnectionMonitorThread = null;

    private NetUtil() {
        init();
    }

    private static class SingletonContainer {
        private static NetUtil _hinstance = new NetUtil();
    }

    public static NetUtil getInstance() {
        return SingletonContainer._hinstance;
    }

    private void init() {
        try {
            ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainsf)
                    .register("https", sslsf).build();
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // Increase max total connection to 200
            poolConnManager.setMaxTotal(connect_max_total);
            // Increase default max connection per route to 20
            poolConnManager.setDefaultMaxPerRoute(connect_max_per_route);
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socket_timeout).build();
            poolConnManager.setDefaultSocketConfig(socketConfig);

            // 定期清理无效、空闲连接
            idleConnectionMonitorThread = new IdleConnectionMonitorThread(poolConnManager);
            idleConnectionMonitorThread.start();
            // 增加关闭钩子
            addHook();
        } catch (Exception e) {
            log.warn("NetUtil init Exception", e);
        }
    }

    private CloseableHttpClient getConnection() {
        return getConnection(connect_request_timeout, connect_timeout, socket_timeout);
    }

    private CloseableHttpClient getConnection(int connectReqTimeout, int connectTimeout, int socketTimeout) {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolConnManager).setConnectionManagerShared(true)
                .setDefaultRequestConfig(buildRequestConfig(connectReqTimeout, connectTimeout, socketTimeout)).build();
        return httpClient;
    }

    private RequestConfig buildRequestConfig(int connectReqTimeout, int connectTimeout, int socketTimeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectReqTimeout > 0 ? connectReqTimeout : connect_request_timeout). //从connect Manager获取Connection 超时时间
                setConnectTimeout(connectTimeout > 0 ? connectTimeout : connect_timeout). //连接超时时间
                setSocketTimeout(socketTimeout > 0 ? socketTimeout : socket_timeout).build(); //请求获取数据超时时间
        return requestConfig;
    }

    public BusinessResponse<Object> get(String url, String contentType) {
        return get(url, contentType, null);
    }

    public BusinessResponse<Object> get(String url, String contentType, Map<String, String> headers) {
        CloseableHttpClient httpclient = null;
        BusinessResponse<Object> result = new BusinessResponse<Object>();
        result.setRt_code(BusinessResponse.RESPONSE_ERROR);
        CloseableHttpResponse response = null;
        HttpGet httpGet = null;
        try {
            httpclient = getConnection();
            httpGet = new HttpGet(url);
            httpGet.addHeader("Content-Type", contentType);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.addHeader(entry.getKey(), entry.getValue());
                }
            }
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socket_timeout).setConnectTimeout(connect_timeout).build();
            httpGet.setConfig(requestConfig);
            response = httpclient.execute(httpGet);
            result = parseResponse(url, response);
        } catch (ClientProtocolException e) {
            log.error("httpclient get ClientProtocolException, url={}", url, e);
        } catch (IOException e) {
            log.error("httpclient get IOException, url={}", url, e);
        } catch (Throwable e) {
            log.error("httpclient get Throwable, url={}", url, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("httpclient get close response exception, url={}", url, e);
                }
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
        return result;
    }

    public BusinessResponse<Object> post(String url, String postParams, String contentType) {
        return post(url, postParams, contentType, null);
    }


    public BusinessResponse<Object> post(String url, String postParams, String contentType, Map<String, String> headers) {
        CloseableHttpClient httpclient = null;
        BusinessResponse<Object> result = new BusinessResponse<Object>();
        result.setRt_code(BusinessResponse.RESPONSE_ERROR);
        CloseableHttpResponse response = null;
        HttpPost httpPost = null;
        try {
            httpclient = getConnection();
            httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", contentType);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            if (StringUtils.isNotEmpty(postParams)) {
                StringEntity entity = new StringEntity(postParams, UTF8);
                httpPost.setEntity(entity);
            }
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socket_timeout).setConnectTimeout(connect_timeout).build();
            httpPost.setConfig(requestConfig);
            response = httpclient.execute(httpPost);
            result = parseResponse(url, response);
        } catch (ClientProtocolException e) {
            log.error("httpclient post ClientProtocolException, url={}", url, e);
        } catch (IOException e) {
            log.error("httpclient post IOException, url={}", url, e);
        } catch (Throwable e) {
            log.error("httpclient post Throwable, url={}", url, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("httpclient post close response exception, url={}", url, e);
                }
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
        }
        return result;
    }

    public BusinessResponse<Object> put(String url, String postParams, String contentType) {
        return put(url, postParams, contentType, null);
    }

    public BusinessResponse<Object> put(String url, String postParams, String contentType, Map<String, String> headers) {
        CloseableHttpClient httpclient = null;
        BusinessResponse<Object> result = new BusinessResponse<Object>();
        result.setRt_code(BusinessResponse.RESPONSE_ERROR);
        CloseableHttpResponse response = null;
        HttpPut httpPut = null;
        try {
            httpclient = getConnection();
            httpPut = new HttpPut(url);
            httpPut.addHeader("Content-Type", contentType);
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPut.addHeader(entry.getKey(), entry.getValue());
                }
            }
            if (StringUtils.isNotEmpty(postParams)) {
                StringEntity entity = new StringEntity(postParams, UTF8);
                httpPut.setEntity(entity);
            }
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socket_timeout).setConnectTimeout(connect_timeout).build();
            httpPut.setConfig(requestConfig);
            response = httpclient.execute(httpPut);
            result = parseResponse(url, response);
        } catch (ClientProtocolException e) {
            log.error("httpclient put ClientProtocolException, url={}", url, e);
        } catch (IOException e) {
            log.error("httpclient put IOException, url={}", url, e);
        } catch (Throwable e) {
            log.error("httpclient put Throwable, url={}", url, e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("httpclient put close response exception, url={}", url, e);
                }
            }
            if (httpPut != null) {
                httpPut.releaseConnection();
            }
        }
        return result;
    }

    public String readBufferFromStream(InputStream input) {
        if (input == null) {
            return null;
        }
        String charSet = UTF8;
        BufferedInputStream buffer = new BufferedInputStream(input);
        ByteArrayOutputStream baos = null;
        String str = null;
        try {
            baos = new ByteArrayOutputStream();

            byte[] byteChunk = new byte[1024 * 16];
            int len = -1;
            while ((len = buffer.read(byteChunk)) > -1) {
                baos.write(byteChunk, 0, len);
            }
            baos.flush();
            byte[] bytes = baos.toByteArray();
            str = new String(bytes, charSet);
        } catch (IOException e) {
            log.error("readBufferFromStream error", e);
        } finally {
            CommonUtil.safeCloseOutputStream(baos);
            CommonUtil.safeCloseInputStream(buffer);
        }
        return str;
    }

    public byte[] readBytesFromStream(InputStream input) {
        if (input == null) {
            return null;
        }
        BufferedInputStream buffer = new BufferedInputStream(input);
        ByteArrayOutputStream baos = null;
        byte[] datas = null;
        try {
            baos = new ByteArrayOutputStream();
            byte[] byteChunk = new byte[1024 * 16];
            int len = -1;
            while ((len = buffer.read(byteChunk)) > -1) {
                baos.write(byteChunk, 0, len);
            }
            baos.flush();
            datas = baos.toByteArray();
        } catch (IOException e) {
            log.error("readBufferFromStream error", e);
        } finally {
            CommonUtil.safeCloseOutputStream(baos);
            CommonUtil.safeCloseInputStream(buffer);
        }
        return datas;
    }

    public String getParamsPost(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        InputStream postStream = null;
        BufferedReader streamReader = null;
        try {
            postStream = request.getInputStream();
            streamReader = new BufferedReader(new InputStreamReader(postStream, UTF8));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            return responseStrBuilder.toString();
        } catch (IOException e) {
            log.warn("getParamsPost IOException", e);
        } catch (Throwable e) {
            log.warn("getParamsPost throwable", e);
        } finally {
            CommonUtil.safeCloseInputStream(postStream);
            CommonUtil.safeCloseReader(streamReader);
        }
        return null;
    }

    public String getParamsForm(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (request.getParameterMap() != null) {
            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                String key = entry.getKey();
                if (!StringUtils.equals("timestamp", key) && !StringUtils.equals("token", key) && !StringUtils.equals("sign", key)
                        && !StringUtils.equals("nsukey", key)) {
                    map.put(key, request.getParameter(key));
                }
            }
        }
        return gson.toJson(map);
    }

    public BusinessResponse<Object> parseResponse(String url, CloseableHttpResponse response) {
        InputStream inputStream = null;
        BusinessResponse<Object> result = new BusinessResponse<Object>();
        result.setRt_code(response.getStatusLine().getStatusCode());
        try {
            if (response.getEntity() != null) {
                inputStream = response.getEntity().getContent();
                String output = readBufferFromStream(inputStream);
                if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() >= 300) {
                    result.setRt_msg(output);
                    log.warn("parseResponse unexpected status, url={} status={}", url, response.getStatusLine().getStatusCode());
                } else {
                    result.setData(output);
                }
            }
        } catch (IOException e) {
            log.error("parseResponse IOException", e);
        } catch (Throwable e) {
            log.error("parseResponse Throwable", e);
        } finally {
            CommonUtil.safeCloseInputStream(inputStream);
        }
        return result;
    }

    private void close() {
        if (poolConnManager != null) {
            poolConnManager.close();
        }
        if (idleConnectionMonitorThread != null) {
            idleConnectionMonitorThread.shutdown();
        }
    }

    private void addHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("shutdown hook get");
                close();
                log.info("shutdown hook end");
            }
        }));
    }

    class IdleConnectionMonitorThread extends Thread {
        private final PoolingHttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionMonitorThread(PoolingHttpClientConnectionManager connMgr) {
            super();
            this.setName("idle-connection-monitor");
            this.setDaemon(true);
            this.connMgr = connMgr;
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000);
                        // Close expired connections
                        connMgr.closeExpiredConnections();
                        // Optionally, close connections
                        // that have been idle longer than 30 sec
                        connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                    }
                }
            } catch (InterruptedException ex) {
                // terminate
            }
        }

        public void shutdown() {
            synchronized (this) {
                shutdown = true;
                notifyAll();
            }
        }
    }
}
