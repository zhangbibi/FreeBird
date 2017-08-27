package com.sbtest.ignite;

import com.sbtest.utils.SpringContextUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;

/**
 * Created by zhangyaping on 17/8/27.
 */
public class CacheServer {

    public static void putDataInCache() {

        IgniteConfiguration cfg = (IgniteConfiguration) SpringContextUtil.getBean("ignite.cache.cfg");

        try (Ignite ignite = Ignition.start(cfg)) {
            CacheConfiguration cacheCfg = new CacheConfiguration("myCache");

            IgniteCache<String, String> cache = ignite.getOrCreateCache(cacheCfg);

            cache.put("Hello", "Kitty");

            System.out.println("CacheServer--->getCache : " + cache.get("Hello"));
        }

    }

}
