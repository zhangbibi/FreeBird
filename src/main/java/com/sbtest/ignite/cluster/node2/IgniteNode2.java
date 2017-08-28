package com.sbtest.ignite.cluster.node2;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/**
 * Created by zhangyaping on 17/8/27.
 */
public class IgniteNode2 {

    public static void startIgnite() {
        Ignite ignite = Ignition.start("C:\\workspace\\SpringBootTest\\src\\main\\java\\com\\sbtest\\ignite\\cluster\\node2\\ignite-node2.xml");
    }

    public static void main(String[] args) {
        startIgnite();
    }
}
