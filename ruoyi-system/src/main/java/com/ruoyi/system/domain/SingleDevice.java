package com.ruoyi.system.domain;

import java.util.HashMap;

public class SingleDevice {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String[] getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String[] deviceIp) {
        this.deviceIp = deviceIp;
    }

    public long getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public String getOverTimeH0Count() {
        return overTimeH0Count;
    }

    public void setOverTimeH0Count(String overTimeH0Count) {
        this.overTimeH0Count = overTimeH0Count;
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    public String[] getMsgTypes() {
        return msgTypes;
    }

    public void setMsgTypes(String[] msgTypes) {
        this.msgTypes = msgTypes;
    }

    // 整个日志中该设备使用的ip列表
    private String[] deviceIp;

    // 整个日志中该设备最长的心跳间隔
    private long maxDuration;

    // 整个日志中该设备心跳次数统计
    private int heartCount;

    // 心跳超时的次数
    private String overTimeH0Count;

    // 心跳超时的line 数据结构：key:String为发生的确切time，value:String为完整的日志行
    private HashMap<String, String> map = new HashMap<String, String>();

    // 该设备发出的报文类型集合
    private String[] msgTypes;
}
