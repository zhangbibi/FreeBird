package com.sbtest.ignite.load;

import com.sbtest.dto.Voucher;
import com.sbtest.utils.SpringContextUtil;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.util.List;

public class TestLoadCache {

    public static void testLoad() {

        IgniteConfiguration icf = (IgniteConfiguration) SpringContextUtil.getBean("load.ignite.cfg");
//        icf.setClientMode(true);

        try (Ignite ignite = Ignition.start(icf)) {

            try (IgniteCache<String, Voucher> cache = ignite.getOrCreateCache("voucherCache")) {

                // Load cache with data from the database.
                cache.loadCache(null);
                // Execute query on cache.
                QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery("SELECT vouName FROM Voucher limit 1"));
                System.out.println("data : " + cursor.getAll());

            }

        }
    }
}