package com.freebird.common.http;

import com.threeguys.common.util.LogUtils;
import com.threeguys.common.util.PropertiesUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

/**
 * 微信支付带证书HTTPS请求类
 */
public class WechatPayHttpClientUtil {

    private final static org.slf4j.Logger logger = LogUtils.getLogger();

    private int socket_timeout = 8000;
    private int connect_timeout = 8000;
    private int connect_request_timeout = 2000;
    private int connect_max_total = 800;
    private int connect_max_per_route = 500;
    private PoolingHttpClientConnectionManager poolConnManager = null;
    private WechatPayHttpClientUtil.IdleConnectionMonitorThread idleConnectionMonitorThread = null;


    private WechatPayHttpClientUtil() {
        init();
    }

    private static class SingletonContainer {
        private static WechatPayHttpClientUtil _hinstance = new WechatPayHttpClientUtil();
    }

    public static WechatPayHttpClientUtil getInstance() {
        return WechatPayHttpClientUtil.SingletonContainer._hinstance;
    }

    private void init() {
        try {
            ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();

//          SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);
            /*****************************/
            String p12Path = PropertiesUtils.getPropertyValues("wechat.pay.apiclient.dir", "/data/appuser/apiclient_cert.p12");
            String password = PropertiesUtils.getPropertyValues("wechat.pay.mchid");

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(p12Path));
            try {
                keyStore.load(instream, password.toCharArray());
            } finally {
                instream.close();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, password.toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            /*****************************/

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", plainsf).register("https", sslsf).build();
            poolConnManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            // Increase max total connection to 200
            poolConnManager.setMaxTotal(connect_max_total);
            // Increase default max connection per route to 20
            poolConnManager.setDefaultMaxPerRoute(connect_max_per_route);
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socket_timeout).build();
            poolConnManager.setDefaultSocketConfig(socketConfig);

            // 定期清理无效、空闲连接
            idleConnectionMonitorThread = new WechatPayHttpClientUtil.IdleConnectionMonitorThread(poolConnManager);
            idleConnectionMonitorThread.start();
            // 增加关闭钩子
            addHook();
        } catch (Exception e) {
            logger.warn("NetUtil init Exception", e);
        }
    }

    public String httpsP12Post(String url, String params) {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = getConnection();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpPost httpPost = new HttpPost(url);
            StringEntity requestEntity = new StringEntity(params, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/xml");
            httpPost.setEntity(requestEntity);
            String returnValue = httpClient.execute(httpPost, responseHandler); //调接口获取返回值时，必须用此方法
            returnValue = new String((returnValue.getBytes(Charset.forName("ISO-8859-1"))));
            return returnValue;
//            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//            RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
//            return restTemplate.postForObject(url, params, String.class);
        } catch (Exception e) {
            logger.error("Exception when httpsP12Request.", e);
            return null;
        } finally {
            WechatPayHttpClientUtil.close(httpClient);
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

    public static void close(CloseableHttpClient httpClient) {
        try {
            httpClient.close();
        } catch (Exception e) {
            logger.error("Exception when close httpClient.", e);
        }
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

    private void addHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("shutdown hook get");
                close();
                logger.info("shutdown hook end");
            }
        }));
    }


    private void close() {
        if (poolConnManager != null) {
            poolConnManager.close();
        }
        if (idleConnectionMonitorThread != null) {
            idleConnectionMonitorThread.shutdown();
        }
    }
}
