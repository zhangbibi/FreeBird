package com.freebird.wx.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LogUtils {
    private static final Logger threeguys = LoggerFactory.getLogger("threeguys");
    private static final Logger joblog = LoggerFactory.getLogger("job");

    public static Logger getLogger() {
        return threeguys;
    }

    public static Logger getJobLogger() {
        return joblog;
    }

    public static Logger getLogger(String logName) {
        return LoggerFactory.getLogger(logName);
    }

}
