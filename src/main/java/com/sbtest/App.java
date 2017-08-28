package com.sbtest;

import com.sbtest.ignite.load.TestLoadCache;
import org.mybatis.spring.annotation.MapperScan;

import com.sbtest.ignite.CacheServer;
import com.sbtest.ignite.ComputeClient;
import com.sbtest.ignite.ComputeServer;
import com.sbtest.ignite.IgniteServer;
import com.sbtest.ignite.cluster.ClusterServer;
import com.sbtest.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;


@ImportResource(locations = {"classpath:spring.xml"})
@SpringBootApplication
@MapperScan("com.sbtest.mapper")
public class App {
    public static void main(String[] args) {


        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
        SpringContextUtil.setApplicationContext(applicationContext);

        //测试ignite基本功能
        //testIgnite();

        //测试ignite集群
        //testIgniteCluster();

        //测试数据加载到ignite内存
        testLoadData();
    }

    static void testIgnite() {
        //启动一个计算应用
        IgniteServer.startIgnite();

        //启动一个缓存应用
        CacheServer.putDataInCache();

        //服务端计算节点
        ComputeServer.compute();

        //客户端计算节点
        ComputeClient.compute();
    }

    static void testIgniteCluster() {
        ClusterServer.getClusterNode();
    }

    static void testLoadData() {
        TestLoadCache.testLoad();
    }
}
