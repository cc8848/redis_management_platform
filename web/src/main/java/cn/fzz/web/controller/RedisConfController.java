package cn.fzz.web.controller;

import cn.fzz.bean.RedisConfigBean;
import cn.fzz.bean.common.AttributeBean;
import cn.fzz.common.Common;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by fanzezhen on 2017/12/19.
 * Desc:
 */
@Controller
@RequestMapping("/redis")
public class RedisConfController {
    @RequestMapping(value = "/configs", method = RequestMethod.GET)
    public String login(@Valid String port){

        try {
            //        FileWriter fw = null;
//        File directory = new File("");//设定为当前文件夹
//        System.out.println(directory.getCanonicalPath());//获取标准的路径
//        System.out.println(directory.getAbsolutePath());//获取绝对路径
            FileWriter fw = new FileWriter("web/src/main/resources/static/configs/redis.conf");

            AttributeBean attributeBean;
            RedisConfigBean redisConfigBean = new RedisConfigBean();
            redisConfigBean.setPort(6380);

            Field[] fields = Common.getAttributeFields(redisConfigBean);
            for (Field field : fields) {    //循环取出每个属性的内容
                StringBuilder strToWrite = new StringBuilder();
                attributeBean = Common.getAttributes(field, redisConfigBean);
                if (attributeBean.getType().equals("class [[Ljava.lang.String;")){  //如果是字符串二维数组
                    String [][] strArr = (String[][])attributeBean.getValue();
                    for (String[] strArrRow : strArr){  //循环取出二维字符串数组的内容
                        for (String str: strArrRow){
                            if (StringUtils.isEmpty(str)){
                                str = "\"\"";
                            }
                            strToWrite.append(str).append(" ");
                        }
                        strToWrite.append("\n");
                    }
                }else { //除二维字符串数组以外的其它类型数据
                    //拼接K-V， 并将'_'替换为'-'.
                    strToWrite.append(attributeBean.getName().replace("_", "-")).append(" ");
                    Object v = attributeBean.getValue();
                    String value = attributeBean.getValue().toString();
                    if(StringUtils.isEmpty(value)) {
                        value = "\"\"";
                    }
                    strToWrite.append(value).append("\n");
                }

                fw.write(String.valueOf(strToWrite));   //将一个属性的内容写入文件
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Process pr = Common.createProcess("D:\\Users\\fanzezhen\\code\\redis\\redis-server.exe", "D:\\Users\\fanzezhen\\code\\redis_management_platform\\web\\src\\main\\resources\\static\\configs\\redis.conf");

        return "redis_conf";
    }
}
