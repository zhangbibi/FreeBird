package com.sbtest.ignite.cluster.node3;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCluster;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by zhangyaping on 17/8/27.
 */
public class IgniteNode3 {

    public static void startIgnite() {

        Ignite ignite = Ignition.start("/Users/zhangyaping/workspace/SpringBootTest/src/main/java/com/sbtest/ignite/cluster/node3/ignite-node3.xml");

        ClusterGroup workers = ignite.cluster().forServers();
        Collection<ClusterNode> nodes = workers.nodes();

        Iterator<ClusterNode> it = nodes.iterator();
        while (it.hasNext()) {
            System.out.println("-------------------");
            ClusterNode clusterNode = it.next();

            System.out.println("ClusterServer ---> getClusterNode" + clusterNode.toString());
        }

    }

    public static void main(String[] args) {
        startIgnite();
    }
}
