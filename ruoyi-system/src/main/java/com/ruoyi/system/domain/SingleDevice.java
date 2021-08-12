package com.ruoyi.system.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *   单独设备的属性对象，关注点有：
 *   1. 设备imeiId    deviceId [ok]
 *   2. 设备每次心跳的接收时间Date 的对象List     heartBeatReceiveTime [ok]
 *   3. 通过心跳接收时间可以计算得到一个 int[] List,
 *      长度和2一致，元素0为0，其中元素为从第二次到最后一次心跳的间隔(秒)。  heartBeatIntervals [ok]
 *   4. 通过扫描3心跳间隔可以得到超时记录的一个int[] list，其中list长度为和2一致，其中
 *      大于0为超时秒数，0为不超时。int[] overTimePoint [ok]
 *   5. 还可以得到4中的MAX超时和MIN超时值，保存到本对象   maxOverTimeHeartBeatGap minOverTimeHeartBeatGap  [ok]
 *   6. 可以通过2得到总心跳次数int totalHBCount [ok] ，可以通过4得到总超时次数int overTimeCount [ok]，保存到本对象
 *   7. 通过4和6可以得到一个百分比，即本设备超时占比的百分比。 保存到本对象  String overTimeCountPercent[ok]
 *   进一步通过7和6可以进行多设备的属性排序。
 */


public class SingleDevice {
    private String deviceId;

    public int getTotalHBCount() {
        return totalHBCount;
    }

    public String getOverTimeCountPercent() {
        return overTimeCountPercent;
    }

    public void setOverTimeCountPercent(String overTimeCountPercent) {
        this.overTimeCountPercent = overTimeCountPercent;
    }

    private String overTimeCountPercent;

    public void setTotalHBCount(int totalHBCount) {
        this.totalHBCount = totalHBCount;
    }

    public int getOverTimeCount() {
        return overTimeCount;
    }

    public void setOverTimeCount(int overTimeCount) {
        this.overTimeCount = overTimeCount;
    }

    public int getMaxOverTimeHeartBeatGap() {
        return maxOverTimeHeartBeatGap;
    }

    public void setMaxOverTimeHeartBeatGap(int maxOverTimeHeartBeatGap) {
        this.maxOverTimeHeartBeatGap = maxOverTimeHeartBeatGap;
    }

    private int totalHBCount;
    private int overTimeCount;


    public List<Date> getHeartBeatReceiveTime() {
        return heartBeatReceiveTime;
    }

    public void setHeartBeatReceiveTime(List<Date> heartBeatReceiveTime) {
        this.heartBeatReceiveTime = heartBeatReceiveTime;
    }

    private List<Date> heartBeatReceiveTime;

    public int[] getOverTimePoint() {
        return overTimePoint;
    }

    public void setOverTimePoint(int[] overTimePoint) {
        this.overTimePoint = overTimePoint;
    }

    private int[] overTimePoint;

    public int getMinOverTimeHeartBeatGap() {
        return minOverTimeHeartBeatGap;
    }

    public void setMinOverTimeHeartBeatGap(int minOverTimeHeartBeatGap) {
        this.minOverTimeHeartBeatGap = minOverTimeHeartBeatGap;
    }

    private int minOverTimeHeartBeatGap;

     private int maxOverTimeHeartBeatGap;


    public int[] getHeartBeatIntervals() {
        return heartBeatIntervals;
    }

    public void setHeartBeatIntervals(int[] heartBeatIntervals) {
        this.heartBeatIntervals = heartBeatIntervals;
    }

    private  int[] heartBeatIntervals;

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
