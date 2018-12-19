package com.sec.ilearn.api;

import com.alibaba.fastjson.JSONObject;
import com.sec.ilearn.pojo.DocInfo;
import com.sec.ilearn.util.JsonResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.Date;


@RequestMapping("api")
@Controller
public class GeneratePyApi {

    private static final Logger logger = LogManager.getLogger(GeneratePyApi.class);

    @Autowired
    private DocInfo docInfo;

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


    @ApiOperation(value = "组合python文件生成新文件并运行", notes = "综合测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filename", value = "保存的文件名", required = true, dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "path", value = "要复制的文件,以逗号隔开", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "runall",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> testCombine(@RequestParam(value = "filename")String filename,@RequestParam(value = "path")String path){
        String[] paths = path.split(",");
        String result = "";
        for (String pa : paths){
            result += readFileByLines(pa);
        }
        TextToFile(filename,result);
        ResponseEntity<JsonResult> res = testRunPy("doc/"+filename);
        res.getBody().setMsg("生成文件路径为："+ docInfo.getPath() +filename);
        return res;
    }



    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        String result = "";
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
                result += tempString + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            return result;
        }
    }

    /**
     * 传入文件名以及字符串, 将字符串信息保存到文件中
     *
     * @param strFilename
     * @param strBuffer
     */
    public static void TextToFile(String strFilename, String strBuffer)
    {
        File fileText = null;
        try
        {
            // 创建文件对象
            fileText = new File("doc/"+strFilename);
            // 向文件写入对象写入信息
            FileWriter fileWriter = new FileWriter(fileText);
            // 写文件
            fileWriter.write(strBuffer);
            // 关闭
            fileWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

//        // 创建文件对象   (方法2)
//        fileText = new File("doc/"+strFilename);
//        FileOutputStream fos = null;
//        try {
//            fileText.createNewFile();
//            fos = new FileOutputStream(fileText, false);
//            fos.write(strBuffer.getBytes("UTF-8"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }


    @ApiOperation(value = "解析json生成新文件并运行", notes = "综合测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json", value = "传递来的json字符串", required = true, dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "runjson",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<JsonResult> testCombine(@RequestParam(value = "json")String json){

        JSONObject jsonObject = JSONObject.parseObject(json);
        String filename = jsonObject.getString("filename");
        String[] paths = jsonObject.getString("paths").split(",");
        String result = "";
        for (String pa : paths){
            result += readFileByLines(pa);
        }
        TextToFile(filename,result);
        ResponseEntity<JsonResult> res = testRunPy("doc/"+filename);
        res.getBody().setMsg("生成文件路径为："+ docInfo.getPath() +filename);
        return res;
    }
}
