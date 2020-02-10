package com.github.guang19.cos.config;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yangguang
 * @date 2020/2/5
 * @description <p>
 *                  COS客户端属性
 *              </p>
 */
@Getter
@Setter
public abstract class COSClientProperties
{
    //object template
    private String objectTemplateBucket = null;

    //上传对象的限制大小
    private final int DEFAULT_OBJECT_SIZE_LIMIT = 20;
    protected int uploadLimitSize = DEFAULT_OBJECT_SIZE_LIMIT;

    //transfer protocol
    private final String DEFAULT_PROTOCOL = "http";
    protected String protocol = DEFAULT_PROTOCOL;

    //socket数据传输超时时间
    private final int DEFAULT_SOCKET_TIMEOUT = 30000;
    protected int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    //客户端与cos服务器建立连接的超时时间
    private final int DEFAULT_CONNECTION_TIMEOUT = 30000;
    protected int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;

    //从连接池中获取连接的超时时间
    private final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = -1;
    protected int connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;

    //允许打开的最大http连接数
    private final int DEFAULT_MAX_CONNECTIONS = 1024;
    protected int maxConnections = DEFAULT_MAX_CONNECTIONS;

    //代理服务器的主机地址
    protected String proxyIP = null;

    //代理服务器的主机端口
    protected int proxyPort = -1;

    //代理服务器验证的用户名
    protected String proxyUsername = null;

    //代理服务器验证的密码
    protected String proxyPassword = null;

    //线程池核心处理数
    protected final int THREAD_POOL_CORE_SIZE = Runtime.getRuntime().availableProcessors();

    //线程池允许最大执行线程的数量
    protected final int THREAD_POOL_MAX_SIZE = THREAD_POOL_CORE_SIZE << 1;

    //线程池中闲置线程的存活时间
    protected final int THREAD_KEEPALIVE = 2000;

    //默认创建线程的工厂
    protected final ThreadFactory DEFAULT_THREAD_FACTORY = Executors.defaultThreadFactory();

    //默认线程池的拒绝策略
    protected final RejectedExecutionHandler DEFAULT_REJECTED_POLICY =  new ThreadPoolExecutor.DiscardOldestPolicy();

    //空构造函数
    protected COSClientProperties()
    {}

    //全参构造函数
    protected COSClientProperties(String objectTemplateBucket, int uploadLimitSize, String protocol, int socketTimeout, int connectionTimeout, int connectionRequestTimeout, int maxConnections, String proxyIP, int proxyPort, String proxyUsername, String proxyPassword)
    {
        this.objectTemplateBucket = objectTemplateBucket;
        this.uploadLimitSize = uploadLimitSize;
        this.protocol = protocol;
        this.socketTimeout = socketTimeout;
        this.connectionTimeout = connectionTimeout;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.maxConnections = maxConnections;
        this.proxyIP = proxyIP;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }
}
