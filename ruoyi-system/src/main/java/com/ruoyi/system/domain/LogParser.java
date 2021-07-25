package com.ruoyi.system.domain;

import java.util.HashMap;

// 先考虑server端收到的数据分析
public class LogParser {
    // 日志持续时间-总的
    private Long logDuration;

    // 设备总数 直接计算设备列表的length即可
    // private int deviceNum;

    public Long getLogDuration() {
        return logDuration;
    }

    public void setLogDuration(Long logDuration) {
        this.logDuration = logDuration;
    }

    public String[] getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(String[] deviceList) {
        this.deviceList = deviceList;
    }

    public String[] getMsgTypes() {
        return msgTypes;
    }

    public void setMsgTypes(String[] msgTypes) {
        this.msgTypes = msgTypes;
    }

    public HashMap<String, Object> getDeviceTotalData() {
        return deviceTotalData;
    }

    public void setDeviceTotalData(HashMap<String, Object> deviceTotalData) {
        this.deviceTotalData = deviceTotalData;
    }

    // 设备列表
    private String[] deviceList;

    // 接收报文类型-总的
    private String[] msgTypes;

    // 发送报文类型-分设备分类型，key:String为设备id，value为SingleDevice对象数组
    private HashMap<String, Object> deviceTotalData = new HashMap<String, Object>();

}
