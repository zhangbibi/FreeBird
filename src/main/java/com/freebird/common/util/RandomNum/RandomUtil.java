package com.freebird.common.util.RandomNum;

import java.util.UUID;

/**
 * Created by zhangyaping on 2017/4/23.
 */
public class RandomUtil {

    public static String gen18RandomNo(String prefix) {
        // 前10位从时间戳取后10位
        String timeGen = System.currentTimeMillis() + "";
        timeGen = timeGen.substring(3, timeGen.length());
        // 后8位随机生成
        String randomStr = IdWorker.nextFlowId() + "";
        randomStr = randomStr.substring(randomStr.length() - 7, randomStr.length());
        String ran = prefix + timeGen + randomStr;
        return ran;
    }

    public static String gen32UUID() {
        String ran32 = UUID.randomUUID().toString().replace("-", "");
        return ran32;
    }

}
