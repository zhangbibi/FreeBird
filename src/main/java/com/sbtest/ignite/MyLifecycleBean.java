package com.sbtest.ignite;

import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;

/**
 * Created by zhangyaping on 17/8/27.
 */

public class MyLifecycleBean implements LifecycleBean {
    @Override
    public void onLifecycleEvent(LifecycleEventType evt) {
        if (evt == LifecycleEventType.BEFORE_NODE_START) {
            // Do something.
            System.out.println("MyLifecycleBean ---> onLifecycleEvent");
        }
    }
}
