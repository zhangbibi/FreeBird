package com.sbtest.ignite.load;

import com.sbtest.dto.Voucher;
import com.sbtest.utils.SpringContextUtil;
import com.sun.istack.internal.Nullable;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.SpringResource;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoucherStore extends CacheStoreAdapter<String, Voucher> {

    private DataSource dataSource = (DataSource) SpringContextUtil.getBean("dataSource");
    // This method is called whenever IgniteCache.loadCache() method is called.
    @Override
    public void loadCache(IgniteBiInClosure<String, Voucher> clo, @Nullable Object... objects) throws CacheLoaderException {
        System.out.println(">> Loading cache from store...");
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT vou_name, vou_id, user_id FROM tb_voucher")) {
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        Voucher voucher = new Voucher(rs.getString(1), rs.getString(2), rs.getString(3));
                        clo.apply(voucher.getUserId(), voucher);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new CacheLoaderException("Failed to load values from cache store.", e);
        }
    }

    // This method is called whenever IgniteCache.get() method is called.
    @Override
    public Voucher load(String key) throws CacheLoaderException {
        System.out.println(">> Loading voucher from store...");
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT vou_name, vou_id, user_id FROM tb_voucher where user_id = ?")) {
                st.setString(1, key.toString());
                ResultSet rs = st.executeQuery();
                return rs.next() ? new Voucher(rs.getString(1), rs.getString(2), rs.getString(3)) : null;
            }
        }
        catch (SQLException e) {
            throw new CacheLoaderException("Failed to load values from cache store.", e);
        }
    }

    @Override
    public void write(Cache.Entry<? extends String, ? extends Voucher> entry) throws CacheWriterException {
        System.out.println("VoucherStore write");
    }

    @Override
    public void delete(Object o) throws CacheWriterException {
        System.out.println("VoucherStore delete");
    }
}