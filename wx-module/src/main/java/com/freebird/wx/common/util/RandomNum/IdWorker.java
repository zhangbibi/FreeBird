package com.freebird.wx.common.util.RandomNum;

/**
 * Created by zhangyaping on 2017/4/23.
 */
import java.util.Random;


public class IdWorker {

    private static final long workerIdBits = 5L;// 机器标识位数5
    private static final long datacenterIdBits = 5L;// 数据中心标识位数5
    private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);// 机器ID最大值: 31，去括号则是1023
    private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);// 数据中心ID最大值: 31

    private static final long sequenceBits = 12L; // 毫秒内自增位，防时间碰撞
    private static final long workerIdShift = sequenceBits; // 12
    private static final long datacenterIdShift = sequenceBits + workerIdBits;// 17
    private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;// 22
    private static final long sequenceMask = -1L ^ (-1L << sequenceBits); // 4095, 111111111111,12位

    private static final Random r = new Random();

    private final long workerId;
    private final long datacenterId;
    private final long idepoch; // 时间起始标记点，作为基准，一般取系统的最近时间

    private long lastTimestamp = -1L;
    private long sequence;// 0，并发控制


    public IdWorker() {
        this(1458010348569L);
    }

    public IdWorker(long idepoch) {
        this(r.nextInt((int) maxWorkerId), r.nextInt((int) maxDatacenterId), 0, idepoch);
    }

    public IdWorker(long workerId, long datacenterId, long sequence) {
        this(workerId, datacenterId, sequence, 1344322705519L);
    }

    public IdWorker(long workerId, long datacenterId, long sequence, long idepoch) {

        if (workerId < 0 || workerId > maxWorkerId) {
            throw new IllegalArgumentException("Illegal workerId: " + workerId);
        }
        if (datacenterId < 0 || datacenterId > maxDatacenterId) {
            throw new IllegalArgumentException("Illegal datacenterId: " + workerId);
        }
        if (idepoch >= System.currentTimeMillis()) {
            throw new IllegalArgumentException("Illegal idepoch: " + idepoch);
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
        this.idepoch = idepoch;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public long getTime() {
        return System.currentTimeMillis();
    }

    public long getId() {
        return nextId();
    }

    private synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards.");
        }
        // 如果上一个timestamp与新产生的相等，则sequence加一(0-4095循环)
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) timestamp = tilNextMillis(lastTimestamp);// 重新生成timestamp
        } else { // 对新的timestamp，sequence从0开始
            sequence = 0;
        }
        lastTimestamp = timestamp;
        return ((timestamp - idepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    /**
     * 等待下一个毫秒的到来, 保证返回的毫秒数在参数lastTimestamp之后
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获得系统当前毫秒数
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IdWorker{");
        sb.append("workerId=").append(workerId);
        sb.append(", datacenterId=").append(datacenterId);
        sb.append(", idepoch=").append(idepoch);
        sb.append(", lastTimestamp=").append(lastTimestamp);
        sb.append(", sequence=").append(sequence);
        sb.append('}');
        return sb.toString();
    }


    private static IdWorker flowIdWorker = new IdWorker(1);

    public static IdWorker flowIdWorkerInstance() {
        return flowIdWorker;
    }

    public static long nextFlowId() {
        return flowIdWorker.nextId();
    }

    public static String nextFlowIdByBase62() {
        return Base62.encode(nextFlowId());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++)
            System.out.println(nextFlowId() + " - " + nextFlowIdByBase62());
    }
}

