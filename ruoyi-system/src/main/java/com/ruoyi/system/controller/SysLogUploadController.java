package com.ruoyi.system.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.system.domain.LogParser;
import com.ruoyi.system.domain.SingleDevice;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysLogUpload;
import com.ruoyi.system.service.ISysLogUploadService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 日志上传查看Controller,add部分和upload部分进行整合，不会手动add，每次用户upload之后都会自动新增一条，
 *
 * 对于日志格式：注意，现在是以OMNI设备的日志格式为基准的
 * @author wangjia
 * @date 2021-07-20
 */
@Controller
@RequestMapping("/system/upload")
public class SysLogUploadController extends BaseController
{
    private String prefix = "system/upload";

    @Autowired
    private ISysLogUploadService sysLogUploadService;

    @RequiresPermissions("system:upload:view")
    @GetMapping()
    public String upload()
    {
        return prefix + "/upload";
    }

    /**
     * 查询日志上传查看列表
     */
    @RequiresPermissions("system:upload:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysLogUpload sysLogUpload)
    {
        startPage();
        List<SysLogUpload> list = sysLogUploadService.selectSysLogUploadList(sysLogUpload);
        return getDataTable(list);
    }

    /**
     * 导出日志上传查看列表
     */
    @RequiresPermissions("system:upload:export")
    @Log(title = "日志上传查看", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysLogUpload sysLogUpload)
    {
        List<SysLogUpload> list = sysLogUploadService.selectSysLogUploadList(sysLogUpload);
        ExcelUtil<SysLogUpload> util = new ExcelUtil<SysLogUpload>(SysLogUpload.class);
        return util.exportExcel(list, "日志上传查看数据");
    }

    /**
     * 新增日志上传查看
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存日志上传查看
     */
    @RequiresPermissions("system:upload:add")
    @Log(title = "日志上传查看", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysLogUpload sysLogUpload)
    {
        return toAjax(sysLogUploadService.insertSysLogUpload(sysLogUpload));
    }

    /**
     * 修改日志上传查看
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysLogUpload sysLogUpload = sysLogUploadService.selectSysLogUploadById(id);
        mmap.put("sysLogUpload", sysLogUpload);
        return prefix + "/edit";
    }


    /**
     * 日志解析by wj 触发：从table的row的'数据分析'按钮点击触发
     */
    @GetMapping("/edit2/{id}")
    public String edit2(@PathVariable("id") Long id, ModelMap mmap) throws IOException, ParseException {
        // SysLogUpload sysLogUpload = sysLogUploadService.selectSysLogUploadById(id);
        // mmap.put("sysLogUpload", sysLogUpload);
        // System.out.println(id);
        LogParser lp  = new LogParser();
        SysLogUpload su = sysLogUploadService.selectSysLogUploadById(id);
        System.out.println(su.getLogUrl());
        // TODO： 下面开始进行文本解析的util调用
        String localPath = RuoYiConfig.getProfile();// Users/lancelot/programming/ruoyiupload
        String dbPath = su.getLogUrl();
        localPath = localPath + dbPath.substring("http://localhost/profile".length());
        System.out.println(localPath);
        lp = processLog(localPath);
        System.out.println(lp.getDeviceList().toString());
        mmap.put("lp", lp);
        return "system/server/server";
    }



    // 进行omni日志文件的解析工作
    public  LogParser processLog(String url) throws IOException, ParseException {

        InputStreamReader read = new InputStreamReader(new FileInputStream(url),"UTF-8");
        BufferedReader br= new BufferedReader(read);//BufferedReader读取文件
        String s = null;
        int i = 0;
        LogParser logParser = new LogParser();
        String[] str = new String[]{};
       // List<String> list = Arrays.asList(str);
       // List arrList = new ArrayList(list);
        List<String> logTotalList=new ArrayList<>();  // 日志全集数据,第一次while后保存在内存中
        List<String> strlist=new ArrayList(Arrays.asList(str));//**须定义时就进行转化**

        Date firstLineTime = null ;
        Date lastLineTime = null;
        int lineNum = 0;
        int idleNum = 0;
        int receivedMsgNum = 0;
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        while((s = br.readLine()) != null){ // 读取每一行数据
            logTotalList.add(s);
            if((lineNum == 0)&&(s.length() > 19)&&(s.substring(0,3).equals("202"))) { // 第一行的时间记录，202x年开头
                try {
                    firstLineTime =sdf.parse( s.substring(0, 19) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if((lineNum > 0)&&(s.length() > 19)&&(s.substring(0,3).equals("202"))) {
                try {
                    lastLineTime =sdf.parse( s.substring(0, 19) );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            lineNum++;
            String re = "[^\\d](\\d{15})[^\\d]"; // 匹配imei值
            Pattern p = Pattern.compile(re);
            Matcher m = p.matcher(s);
            if(m.find()) { // 找到匹配项,则先判断是否在str数组中，若不在则加到str后面
               // System.out.println(m.group(1));
                if(!strlist.contains(m.group(1))){
                    strlist.add(m.group(1));
                }
            }
            String re2 = ".*_IDLE.*";
            Pattern p2 = Pattern.compile(re2);
            Matcher m2 = p2.matcher(s);
            if(m2.find()) { // 找到匹配项,则先判断是否在str数组中，若不在则加到str后面
                idleNum++;
            }
            String re3 = ".*receive ip.*";
            Pattern p3 = Pattern.compile(re3);
            Matcher m3 = p3.matcher(s);
            if(m3.find()) { // 找到匹配项,则先判断是否在str数组中，若不在则加到str后面
                receivedMsgNum++;
            }
        }
        int logDuration = (int) ((lastLineTime.getTime() - firstLineTime.getTime()) / (1000*60)); // 单位是分钟
        str = strlist.toArray(new String[0]);
        br.close();
        logParser.setDeviceList(str);
        // 将设备单体的对象属性进行填充。扫描str数组，循环开销为设备个数n
        SingleDevice[] sd = new SingleDevice[str.length]; // 初始化设备数的对象
        SingleDevice[] sdnew = new SingleDevice[str.length]; // 初始化设备数的对象
        for( int k = 0; k < str.length; k++) { // str.length 为当前设备总数
          //  System.out.println(str[k]);
            sd[k] = new SingleDevice();  // 初始化设备单体对象
            sd[k].setDeviceId(str[k]); // 设置单体设备的imei
        }
        logParser.setLogDuration((long)logDuration); // 时间跨度秒
        logParser.setIdleInstanceCount(idleNum); // read_idle 或 write_idle 的次数
        logParser.setReceiveMsgCount(receivedMsgNum);

        for(int ii = 0 ; ii < sd.length; ii++ ) {
            SingleDevice ss = getOneDeviceProperty(logTotalList, sd[ii]);
            sdnew[ii] =  ss;
        }
        logParser.setDeviceDetail(sdnew);
        int overTimeDeviceCount = 0; // 超时设备的个数计数
        int maxOverTimeInAll = 0; // 所有设备的最长超时时间
        int maxOverTimeInDevice = 0;
        for(int ii = 0; ii < sdnew.length; ii++) {
            if(sdnew[ii].getOverTimeCount() > 0){
                overTimeDeviceCount ++;  // 超时设备个数统计
            }
            if(sdnew[ii].getMaxOverTimeHeartBeatGap() > maxOverTimeInAll) {
                maxOverTimeInAll = sdnew[ii].getMaxOverTimeHeartBeatGap(); // 最长的gap时间
            }
            if(sdnew[ii].getOverTimeCount() > maxOverTimeInDevice) {
                maxOverTimeInDevice = sdnew[ii].getOverTimeCount(); // 所有设备超时次数的最大值
            }

        }
        logParser.setOverTimeDeviceNum(overTimeDeviceCount);
        logParser.setMaxOverTimeInAll(maxOverTimeInAll);
        logParser.setMaxOverTimeNumOfDevice(maxOverTimeInDevice);

        br.close();
        read.close();
        return logParser;
    }

    /**
     * 重写 py:getTimeFromLog(logLine)
     * 获取某一行文本的时间datetime
     * line：某一行原始日志文本
     * return: Date d 对象
     */
    Date getTimeInLine(String line) {
        Date d = null;
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        if((line.length() > 19) && (line.substring(0,3).equals("202"))) {
            try {
                d =sdf.parse( line.substring(0, 19) ); // 截取到秒级别
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return d;
    }

    /*
    * 重写 calcHeartBeatGap(imeiId, ContentLines)
    *
    * 输入参数：br：日志文本
    *         sd：设备单体对象，输入时只有imei是存在的
    * 返回：   sd：填充了其他数据的sd
    *
    * 解析某一个imei 设备 的心跳数据，并返回一个对象，对象属性如下：
    * SingleDevice的注释 2 3 4 5 6 7
    * */
    SingleDevice getOneDeviceProperty(List<String> list, SingleDevice sd) throws IOException, ParseException {

       // String s = null;
        Date d = null;
        List<Date> dateList= new ArrayList<>();
        List<Integer> timeInterval = new ArrayList<>();
        List<Integer> overTimeSec = new ArrayList<>();

        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        // SingleDevice的注释-2  心跳接收时间属性填充
        for(String ss: list) { // 读取每一行数据
            String re = sd.getDeviceId() + ".*type=H0" ; // 匹配imei值以及H0的特征pattern
            Pattern p = Pattern.compile(re);
            Matcher m = p.matcher(ss);
            if(m.find()) { // 找到匹配项，获取时间字符串
                d = sdf.parse( ss.substring(0, 19) );
                dateList.add(d);
            }
        }
        // SingleDevice的注释-3  心跳接收时间间隔属性填充,遍历设备的心跳时间数组并进行计算
        timeInterval.add(240); //第一个元是0
        for(int i = 1 ; i < dateList.size(); i++) { // 从第二个元素开始计算 Tn - Tn-1
            timeInterval.add( (int) ((dateList.get(i).getTime() - dateList.get(i - 1).getTime()) / 1000) );
        }

        // SingleDevice的注释-4 计算间隔是否超时，间隔基准为240s，计算超过240s的case
        for (Integer integer : timeInterval) { // 从第二个元素开始计算 Tn - Tn-1
            overTimeSec.add(integer - 240);
        }

        sd.setTotalHBCount(dateList.size());
        sd.setHeartBeatReceiveTime(dateList);
        sd.setHeartBeatIntervals(timeInterval);
        sd.setOverTimePoint(overTimeSec);
        sd.setMaxOverTimeHeartBeatGap(Collections.max(overTimeSec));
        sd.setMinOverTimeHeartBeatGap(Collections.min(overTimeSec));
        int count  = 0;
        for (Integer integer : overTimeSec) { // 从第二个元素开始计算 Tn - Tn-1
            if(integer > 3) { // 大于3s算为超时
                 count++;
            }
        }
        java.text.NumberFormat numberFormat = java.text.NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        String percent = numberFormat.format((float)count / (float)dateList.size() *100 ) + "%";
        sd.setOverTimeCount(count);
        sd.setOverTimeCountPercent(percent);
        return sd;
    }





    /**
     * 修改保存日志上传查看
     */
    @RequiresPermissions("system:upload:edit")
    @Log(title = "日志上传查看", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysLogUpload sysLogUpload)
    {
        return toAjax(sysLogUploadService.updateSysLogUpload(sysLogUpload));
    }

    /**
     * 删除日志上传查看
     */
    @RequiresPermissions("system:upload:remove")
    @Log(title = "日志上传查看", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysLogUploadService.deleteSysLogUploadByIds(ids));
    }
}
