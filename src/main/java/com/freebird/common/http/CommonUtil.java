package com.freebird.common.http;

import com.threeguys.common.util.LogUtils;
import org.slf4j.Logger;

import java.io.*;

/**
 *
 */
public class CommonUtil {
    private static Logger log = LogUtils.getLogger();

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
