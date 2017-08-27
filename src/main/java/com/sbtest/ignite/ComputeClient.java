package com.sbtest.ignite;

import com.sbtest.utils.SpringContextUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.configuration.IgniteConfiguration;

/**
 * Created by zhangyaping on 17/8/27.
 */
public class ComputeClient {

    public static void compute() {

        // Create new configuration.
        IgniteConfiguration cfg = (IgniteConfiguration) SpringContextUtil.getBean("ignite.cache.cfg");
        try (Ignite ignite = Ignition.start(cfg);) {

            ClusterGroup clientGroup = ignite.cluster().forClients();
            IgniteCompute clientCompute = ignite.compute(clientGroup);
// Execute computation on the client nodes.
            clientCompute.broadcast(() -> System.out.println("Hello Client"));
        }
    }
}
