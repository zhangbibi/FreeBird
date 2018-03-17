package com.freebird.wx.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by zhangyaping on 16-7-19.
 */
public class SpringUtils implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static SpringUtils instance = new SpringUtils();

    private SpringUtils() {}

    public static SpringUtils getInstance() {
        return instance;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> T  getBean(String name, Class<T> clazz) {
        return (T) applicationContext.getBean(name);
    }

}
