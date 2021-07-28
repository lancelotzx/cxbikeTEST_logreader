package com.ruoyi.system.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.system.domain.LogParser;
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
 * @author ruoyi
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
     * 日志解析by wj 触发：从table的row的'数据分析'过来
     */
    @GetMapping("/edit2/{id}")
    public String edit2(@PathVariable("id") Long id, ModelMap mmap) throws IOException {
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

//    // 把数据获取单独封装成rest，触发：从/system/server/server页面的ajax请求过来，进入页面后自动请求
//    @GetMapping("/edit2detail/{id}")
//    public LogParser edit2detail(@PathVariable("id") Long id, ModelMap mmap) throws IOException {
//        LogParser lp  = new LogParser();
//        SysLogUpload su = sysLogUploadService.selectSysLogUploadById(id);
//        System.out.println(su.getLogUrl());
//        // TODO： 下面开始进行文本解析的util调用
//        String localPath = RuoYiConfig.getProfile();// Users/lancelot/programming/ruoyiupload
//        String dbPath = su.getLogUrl();
//        localPath = localPath + dbPath.substring("http://localhost/profile".length());
//        System.out.println(localPath);
//        lp = processLog(localPath);
//        System.out.println(lp.getDeviceList().toString());
//        mmap.put("lp", lp);
//        return lp;
//    }


    // 进行omni日志文件的解析工作
    public  LogParser processLog(String url) throws IOException {

        InputStreamReader read = new InputStreamReader(new FileInputStream(url),"UTF-8");
        BufferedReader br= new BufferedReader(read);//BufferedReader读取文件
        String s = null;
        int i = 0;
        LogParser logParser = new LogParser();
        String[] str = new String[]{};
       // List<String> list = Arrays.asList(str);
       // List arrList = new ArrayList(list);
        List<String> strlist=new ArrayList(Arrays.asList(str));//**须定义时就进行转化**
        Date firstLineTime = null ;
        Date lastLineTime = null;
        int lineNum = 0;
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        while((s = br.readLine()) != null){ // 读取每一行数据

            if((lineNum == 0)&&(s.length() > 19)&&(s.substring(0,3).equals("202"))) { // 第一行的时间记录
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
        }
        int a = (int) ((lastLineTime.getTime() - firstLineTime.getTime()) / (1000*60));
        str = strlist.toArray(new String[0]);
        br.close();
        logParser.setDeviceList(str);
        logParser.setLogDuration((long)a);
        return logParser;
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
