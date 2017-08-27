package com.sbtest.ignite;

import com.sbtest.utils.SpringContextUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by zhangyaping on 17/8/27.
 */
public class IgniteServer {

    public static void startIgnite() {

        // Create new configuration.
        IgniteConfiguration cfg = (IgniteConfiguration) SpringContextUtil.getBean("grid.cfg");
        cfg.setLifecycleBeans(new MyLifecycleBean());

        try (Ignite ignite = Ignition.start(cfg);) {

            Collection<IgniteCallable<Integer>> calls = new ArrayList<>();
            // Iterate through all the words in the sentence and create Callable jobs.
            for (final String word : "Count characters using callable".split(" ")) {
                calls.add(new IgniteCallable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return word.length();
                    }
                });
            }
            // Execute collection of Callables on the grid.
            Collection<Integer> res = ignite.compute().call(calls);
            int sum = 0;
            // Add up individual word lengths received from remote nodes.
            for (int len : res)
                sum += len;
            System.out.println(">>> Total number of characters in the phrase is '" + sum + "'.");

        }
    }
}
