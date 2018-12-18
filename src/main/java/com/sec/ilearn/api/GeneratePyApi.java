package com.sec.ilearn.api;

import com.sec.ilearn.util.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;


@RequestMapping("api")
@Controller
public class GeneratePyApi {

    private static final Logger logger = LogManager.getLogger(GeneratePyApi.class);

    @ApiOperation(value = "生成py文件", notes = "生成py文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonparam", value = "json参数", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "generatepy",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> generatePy(@RequestParam(value = "jsonparam")String jsonparam){
        JsonResult r = new JsonResult();
        try {
            r.setCode("200");
            r.setMsg("生成成功！");
            r.setData("....生成xxx文件内容： "+jsonparam);
            r.setSuccess(true);
            logger.info(new Date().toLocaleString()+"调用生成py文件成功");
        } catch (Exception e) {
            r.setCode("500");
            r.setMsg("生成失败！");
            r.setData(null);
            r.setSuccess(false);
            e.printStackTrace();
            logger.error(new Date().toLocaleString()+"调用生成py文件失败");
        }
        return ResponseEntity.ok(r);
    }


    @ApiOperation(value = "运行py文件", notes = "运行py文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "path路径", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "runpy",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> testRunPy(@RequestParam(value = "path")String path){
        JsonResult r = new JsonResult();
        String res = "default";
        try {
            res = runPy(path);
            r.setCode("200");
            r.setMsg("生成成功！");
            r.setData(res);
            r.setSuccess(true);
            logger.info(new Date().toLocaleString()+"运行py文件成功");

        } catch (IOException e) {
            e.printStackTrace();
            r.setCode("500");
            r.setMsg("生成失败！");
            r.setData(res);
            r.setSuccess(false);
            e.printStackTrace();
            logger.error(new Date().toLocaleString()+"运行py文件失败");
        }
        return ResponseEntity.ok(r);
    }

    //运行py文件
    public String runPy(String path) throws IOException {
//        String path = "D:\\PycharmProjects\\MachineLearning\\Test\\TestRunPy.py";
        String name = "测试方法";
        Runtime runtime = Runtime.getRuntime();
        //执行命令
        Process process = runtime.exec("python "+path);
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
        String pathName = path.substring(path.lastIndexOf("\\")+1);
        if("".equals(error)){

        }else{
            logger.error("名称："+name+"\n运行："+pathName+":"+error,"system","系统异常");
//            System.out.println("名称："+name+"\n运行："+pathName+":"+error+" system:系统异常");
            return "名称："+name+"\n运行："+pathName+":"+error+" system:系统异常";
        }
//        System.out.println(pathName + " " +result);
        logger.info(new Date().toLocaleString()+"运行py文件成功  "+pathName + " " +result);
        return pathName + " 运行结果： " +result;
    }

    @ApiOperation(value = "执行shell命令", notes = "执行shell命令")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "相关参数", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "runshell",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> testShell(@RequestParam(value = "param")String param){
        JsonResult r = new JsonResult();
        String[] cmds = {"/bin/sh","-c","ps -ef|grep java"};
        String result = "";
        Process pro = null;
        try {
            pro = Runtime.getRuntime().exec(cmds);
            pro.waitFor();
            InputStream in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = read.readLine())!=null){
                result += line;
            }
            r.setCode("200");
            r.setMsg("执行shell命令成功！");
            r.setData(result);
            r.setSuccess(true);
            logger.info(new Date().toLocaleString()+"执行shell命令成功");
        } catch (Exception e) {
            e.printStackTrace();
            r.setCode("500");
            r.setMsg("执行shell命令失败！");
            r.setData(null);
            r.setSuccess(false);
            e.printStackTrace();
            logger.error(new Date().toLocaleString()+"执行shell命令失败");
        }
        return ResponseEntity.ok(r);
    }


    @ApiOperation(value = "综合测试", notes = "综合测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filename", value = "保存的文件名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "path", value = "要复制的文件,以逗号隔开", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "runall",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> testShell(@RequestParam(value = "filename")String filename,@RequestParam(value = "path")String path){
        JsonResult r = new JsonResult();
        return ResponseEntity.ok(r);
    }
}
