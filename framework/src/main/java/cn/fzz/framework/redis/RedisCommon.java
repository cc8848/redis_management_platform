package cn.fzz.framework.redis;

import cn.fzz.framework.common.Common;
import cn.fzz.bean.RedisConfigBean;
import cn.fzz.bean.common.AttributeBean;
import cn.fzz.util.RedisUtil;
import org.springframework.util.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Administrator on 2018/1/5.
 * Desc:
 */
public class RedisCommon {


    /**
     * 根据用户名启动redis服务
     *
     * @param username
     * @return
     */
    public static Process createRedisProcessByUser(String username) {
        Map<String, String> configMap = RedisUtil.getRedisHashAll(username);
        if (configMap != null) {
            String redisSoftwarePath = configMap.get("redisSoftwarePath");
            String redisConfigPath = configMap.get("redisConfigPath");
            return Common.createProcess(redisSoftwarePath, redisConfigPath);
        }
        return null;
    }

    /**
     * 保存Redis程序信息
     *
     * @param modelMap
     * @return
     */
    public static void saveRedisProceedingInfo(Map modelMap) {
        String redisSoftwarePath = String.valueOf(modelMap.get("redisSoftwarePath"));
        String redisConfigPath = String.valueOf(modelMap.get("redisConfigPath"));
        String username = "username";
        Map<String, String> map = new HashMap<>();
        map.put("port", String.valueOf(modelMap.get("port")));
        map.put("state", "");
        map.put("version", "v3.2.100");
        map.put("time", String.valueOf(System.currentTimeMillis()));
        map.put("redisSoftwarePath", redisSoftwarePath);
        map.put("redisConfigPath", redisConfigPath);
        List<String> redisProceedingNames = RedisUtil.getListByKey("redisProceedingName");
        if (StringUtils.isEmpty(redisProceedingNames) || !redisProceedingNames.contains("username")){
            RedisUtil.saveRedisList("redisProceedingName", username);
        }else {
            redisConfigPath = RedisUtil.getRedisHashField(username, "redisConfigPath");
            if (redisConfigPath != null) {
                Common.killProcessByPort(new Integer(modelMap.get("port").toString()));
            }
        }
        RedisUtil.saveRedisHash(username, map);
    }

    /**
     * 更新redis进程状态
     * @param username
     * @param process
     */
    public static void updateRedisProceedingState(String username, Process process) {
        Map<String, String> map = new HashMap<>();
        map.put("state", process.toString());
        RedisUtil.saveRedisHash(username, map);
    }

    /**
     * 取出key对应的list
     * @param key
     * @return
     */
    public static List<String> getListFromRedis(String key){
        return RedisUtil.getListByKey(key);
    }

    /**
     * 根据key取出redis中的hash
     * @param key
     * @return
     */
    public static Map<String, String> getHashFromRedis(String key){
        return RedisUtil.getRedisHashAll(key);
    }

    /**
     * 查到所有配置了redis服务的用户， 并查到相应的redis服务
     * @return
     */
    public static List<Map<String, String>> getProcesses(){
        List<Map<String, String>> processes = new ArrayList<>();
        List<String> users = getListFromRedis("redisProceedingName");
        for (String string:users){
            Map<String, String> map = RedisUtil.getRedisHashAll(string);
            if (map != null) {
                map.put("username", string);
            }
            processes.add(map);
        }
        return processes;
    }

    /**
     * 生成redis配置文件
     *
     * @param modelMap
     * @return
     */
    public static Boolean writeRedisConfig(Map modelMap) {
        AttributeBean attributeBean;    //用来存储RedisConfigBean中单个属性的type、name、value
        RedisConfigBean redisConfigBean = new RedisConfigBean();    //生成默认配置文件类
        redisConfigBean.setSoftwarePath(String.valueOf(modelMap.get("redisSoftwarePath")));
        redisConfigBean.setConfigPath(String.valueOf(modelMap.get("redisConfigPath")));
        try {
            FileWriter fw = new FileWriter(redisConfigBean.getConfigPath());            //
            Field[] fields = Common.getAttributeFields(redisConfigBean);    //获得redisConfigBean中所有的属性名字
            for (Field field : fields) {    //循环取出每个属性的内容
                if ("softwarePath".equals(field.toString()) || "configPath".equals(field.toString())){
                    continue;
                }
                StringBuilder strToWrite = new StringBuilder(); //将要写入文件的字符串拼接成一个StringBuilder
                attributeBean = Common.getAttribute(field, redisConfigBean);   //将一个属性的type、name、value放入attributeBean
                String name = attributeBean.getName().replace("_", "-");    //将属性名转换为redis配置文件中的字段名
                if (attributeBean.getType().equals("class [[Ljava.lang.String;")) {  //如果是字符串二维数组
                    String[][] twoDimensional;
                    if (modelMap.containsKey(name)) {
                        String[] rowArr = modelMap.get(name).toString().split(",");
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
                } else { //除字符串二维数组以外的其它类型数据
                    strToWrite.append(name).append(" ");
                    Object object = modelMap.containsKey(name) ? modelMap.get(name) : attributeBean.getValue();
                    String value = StringUtils.isEmpty(object) ? "\"\"" : object.toString();
                    strToWrite.append(value).append("\n");
                }

                fw.write(String.valueOf(strToWrite));   //将一个属性的内容写入文件
            }
            fw.close();
            saveRedisProceedingInfo(modelMap); //保存Redis程序信息
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
