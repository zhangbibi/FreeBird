package com.freebird.wx.common.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 *
 */
public class CommonUtil {
    private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);

    public static void safeCloseInputStream(InputStream in) {
        try {
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (IOException e) {
            log.error("safeClose InputStream exception", e);
        }
    }

    public static void safeCloseOutputStream(OutputStream out) {
        try {
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (IOException e) {
            log.error("safeClose OutputStream exception", e);
        }
    }

    public static void safeCloseReader(Reader in) {
        try {
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (IOException e) {
            log.error("safeClose reader exception", e);
        }
    }

    public static void safeCloseWriter(Writer out) {
        try {
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (IOException e) {
            log.error("safeClose writer exception", e);
        }
    }

}
