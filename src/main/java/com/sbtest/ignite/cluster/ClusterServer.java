package com.sbtest.ignite.cluster;

import com.sbtest.utils.SpringContextUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterMetrics;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by zhangyaping on 17/8/27.
 */
public class ClusterServer {

    public static void getClusterNode() {

        IgniteConfiguration cfg = (IgniteConfiguration) SpringContextUtil.getBean("cluster.grid.cfg");

        try (Ignite ignite = Ignition.start(cfg)) {
            ClusterGroup workers = ignite.cluster().forAttribute("ROLE", "worker");
            Collection<ClusterNode> nodes = workers.nodes();

            Iterator<ClusterNode> it = nodes.iterator();
            while (it.hasNext()) {
                ClusterNode clusterNode = it.next();
                ClusterMetrics metrics = clusterNode.metrics();

                // Get some metric values.
                double cpuLoad = metrics.getCurrentCpuLoad();
                long usedHeap = metrics.getHeapMemoryUsed();
                int numberOfCores = metrics.getTotalCpus();
                int activeJobs = metrics.getCurrentActiveJobs();

                System.out.println("ClusterServer ---> getClusterNode" + clusterNode.toString());
            }
        }
    }
}
