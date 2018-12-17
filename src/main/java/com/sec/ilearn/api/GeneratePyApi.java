package com.sec.ilearn.api;

import com.sec.ilearn.util.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@RequestMapping("api")
@Controller
public class GeneratePyApi {

    @ApiOperation(value = "生成py文件", notes = "生成py文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonparam", value = "json参数", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "generatepy",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> searchSysUserAddr(@RequestParam(value = "jsonparam")String jsonparam){
        JsonResult r = new JsonResult();
        try {
            r.setCode("200");
            r.setMsg("生成成功！");
            r.setData("....生成xxx文件内容： "+jsonparam);
            r.setSuccess(true);
        } catch (Exception e) {
            r.setCode("500");
            r.setMsg("生成失败！");
            r.setData(null);
            r.setSuccess(true);
            e.printStackTrace();
        }
        return ResponseEntity.ok(r);
    }


    @ApiOperation(value = "运行py文件", notes = "运行py文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "path路径", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "runpy",method = RequestMethod.GET)
    @ResponseBody
    public String testRunPy(@RequestParam(value = "path")String path){
        String res = "default";
        try {
            res = runPy(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
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
//            logger.error("名称："+name+"\n运行："+pathName+":"+error,"system","系统异常");
            System.out.println("名称："+name+"\n运行："+pathName+":"+error+" system:系统异常");
            return "名称："+name+"\n运行："+pathName+":"+error+" system:系统异常";
        }
        System.out.println(pathName + " " +result);
        return pathName + " 运行结果： " +result;
    }
}
