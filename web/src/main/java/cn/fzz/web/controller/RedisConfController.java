package cn.fzz.web.controller;

import cn.fzz.bean.RedisConfigBean;
import cn.fzz.bean.common.AttributeBean;
import cn.fzz.common.Common;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanzezhen on 2017/12/19.
 * Desc:
 */
@Controller
@RequestMapping("/redis")
public class RedisConfController {
    private static List<Integer> ports = new ArrayList<>();
    private static int port = 6080;

    @PostMapping("/tmpConfigs")
    public void createUserByMap(@RequestBody Map<String, Object> reqMap) {
        String tel = reqMap.get("tel").toString();
        String pwd = reqMap.get("pwd").toString();
    }

    @RequestMapping(value = "/configs")
    public String writeConfigs(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {

        //校验端口号是否可用
        String reqPort = reqMap.get("port");
        if (StringUtils.isEmpty(reqPort)){  //如果未传入端口号， 则自动生成
            while (ports.indexOf(port) >= 0) {
                port++;
            }
            reqMap.put("port", String.valueOf(port));
        }else if(ports.indexOf(Integer.parseInt(reqPort)) >= 0){    //如果用户传入的端口号已被占用
            for (Object obj: reqMap.keySet()){
                String value = reqMap.get(obj.toString());
                if (!StringUtils.isEmpty(value)){
                    modelMap.addAttribute(obj.toString(), value);
                }
            }
            modelMap.addAttribute("error", "端口号已被占用！");
            return "redis_conf";
        }

        //取出reqMap中的有效 K V
        HashMap<String, String> requestMap = new HashMap<>();
        for (Object obj: reqMap.keySet()){
            String value = reqMap.get(obj.toString());
            if (!StringUtils.isEmpty(value)){
                requestMap.put(obj.toString(), value);
            }
        }

        //设置redis程序和redis配置文件的位置
        String redisConfPath = "web/src/main/resources/static/configs/redis" + requestMap.get("port") + ".conf";
        String redisExePath = "../redis/redis-server.exe";

        //生成redis配置文件
        try {
            //        FileWriter fw = null;
//        File directory = new File("");//设定为当前文件夹
//        System.out.println(directory.getCanonicalPath());//获取标准的路径
//        System.out.println(directory.getAbsolutePath());//获取绝对路径
            FileWriter fw = new FileWriter(redisConfPath);

            AttributeBean attributeBean;
            RedisConfigBean redisConfigBean = new RedisConfigBean();
            redisConfigBean.setPort(6380);

            Field[] fields = Common.getAttributeFields(redisConfigBean);    //获得redisConfigBean中所有的属性名字
            for (Field field : fields) {    //循环取出每个属性的内容
                StringBuilder strToWrite = new StringBuilder(); //将要写入文件的字符串拼接成一个StringBuilder
                attributeBean = Common.getAttribute(field, redisConfigBean);   //将一个属性的type、name、value放入attributeBean
                String name = attributeBean.getName().replace("_", "-");    //将属性名转换为redis配置文件中的字段名
                if (attributeBean.getType().equals("class [[Ljava.lang.String;")) {  //如果是字符串二维数组
                    String[][] twoDimensional;
                    if (requestMap.containsKey(name)) {
                        String[] rowArr = requestMap.get(name).split(",");
                        twoDimensional = new String[rowArr.length][];
                        for (int i = 0; i < rowArr.length; i++) {
                            String[] arr = rowArr[i].trim().split(" ");
                            twoDimensional[i] = arr;
                        }
                    } else {
                        twoDimensional = (String[][]) attributeBean.getValue();
                    }
                    for (String[] strArrRow : twoDimensional) {  //循环取出二维字符串数组的内容
                        for (String str : strArrRow) {
                            if (StringUtils.isEmpty(str)) {
                                str = "\"\"";
                            }
                            strToWrite.append(str).append(" ");
                        }
                        strToWrite.append("\n");
                    }
                } else { //除二维字符串数组以外的其它类型数据
                    strToWrite.append(name).append(" ");
                    Object object = requestMap.containsKey(name)?requestMap.get(name):attributeBean.getValue();
                    String value = StringUtils.isEmpty(object)?"\"\"":object.toString();
                    strToWrite.append(value).append("\n");
                }

                fw.write(String.valueOf(strToWrite));   //将一个属性的内容写入文件
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //判断是否要立即启动redis服务
        if (false) {
            Process pr = Common.createProcess(redisExePath, redisConfPath);
        }

        ports.add(port);    //更新已用的端口号列表
        return "redis_conf";
    }
}
