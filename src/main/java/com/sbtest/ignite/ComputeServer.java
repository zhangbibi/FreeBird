package com.sbtest.ignite;

import com.sbtest.utils.SpringContextUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zhangyaping on 17/8/27.
 */
public class ComputeServer {

    public static void compute() {

        // Create new configuration.
        IgniteConfiguration cfg = (IgniteConfiguration) SpringContextUtil.getBean("grid.cfg");

        try (Ignite ignite = Ignition.start(cfg);) {

            IgniteCompute compute = ignite.compute();
            // Execute computation on the server nodes (default behavior).
            compute.broadcast(() -> System.out.println("Hello Server"));
        }
    }


}
