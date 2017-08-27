package com.sbtest.ignite.cluster.node1;

import com.sbtest.ignite.MyLifecycleBean;
import com.sbtest.utils.SpringContextUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zhangyaping on 17/8/27.
 */
public class IgniteNode1 {

    public static void startIgnite() {
        Ignite ignite = Ignition.start("/Users/zhangyaping/workspace/SpringBootTest/src/main/java/com/sbtest/ignite/cluster/node1/ignite-node1.xml");
    }

    public static void main(String[] args) {
        startIgnite();
    }
}
