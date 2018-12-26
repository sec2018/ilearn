package com.sec.ilearn.api;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

@Component
@Configurable
@EnableScheduling
public class ScheduleTask {


    @Scheduled(cron = "*/1 * * * * *")
    public void init(){
        System.out.println(">>>>>>>>>>>");
        try {
//            watchdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //适用于linux系统，监控文件夹
    private void watchdir()  throws IOException {
        String newdirpath = "/Users/idaims/Documents/Projects/source/new/";
        String olddirpath = "/Users/idaims/Documents/Projects/source/old/";
        File file = new File(newdirpath);		//获取其file对象
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
        for(File f:fs){					//遍历File[]数组
            if(!f.isDirectory()){
                System.out.println(f);
                System.out.println(runPy(f.getPath()));
                String cmd = "mv "+f.getPath()+" "+olddirpath+f.getName();
                Runtime.getRuntime().exec(cmd);
            }
        }
    }

    //运行py文件
    public String runPy(String path) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        //执行命令
        Process process = runtime.exec("python "+ path);
        //接收命令执行的输出信息
        BufferedReader infoReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
        String info = "";
        StringBuilder sb = new StringBuilder();
        while((info = infoReader.readLine()) != null){
            sb.append(info+"\n");
        }
        //接收命令执行的错误信息
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(),"UTF-8"));
        String error = "";
        StringBuilder sbErr = new StringBuilder();
        while((error = errorReader.readLine()) != null){
            sbErr.append(error+"\n");
        }
        String result = sb.toString();
        error = sbErr.toString();
        String pathName = path.substring(path.lastIndexOf("/")+1);

        if("".equals(error)){

        }else{
            return "名称："+pathName+"\n运行："+error+" system:系统异常";
        }
        return new Date().toLocaleString()+"运行python文件成功  "+pathName + " 运行结果： \n" +result;
    }
}
