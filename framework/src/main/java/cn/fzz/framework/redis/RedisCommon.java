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
    public static String getStringByKey(String key) {
        return RedisUtil.getStringByKey(key);
    }

    public static Boolean saveString(String key, String value) {
        return RedisUtil.saveRedisString(key, value);
    }

    public static String getHashFeildValue(String key, String childKey) {
        return RedisUtil.getHashFeildValue(key, childKey);
    }

    /**
     * 保存Redis程序信息
     *
     * @param redisInfoMap
     * @return
     */
    public static void saveRedisProceedingInfo(Map redisInfoMap) {
        String redisSoftwarePath = String.valueOf(redisInfoMap.get("redisSoftwarePath"));
        String redisConfigPath = String.valueOf(redisInfoMap.get("redisConfigPath"));
        String redisTaskName = redisInfoMap.get("redisTaskName").toString();
        Map<String, String> map = new HashMap<>();
        map.put("port", String.valueOf(redisInfoMap.get("port")));
        map.put("version", "v3.2.100");
        map.put("time", String.valueOf(System.currentTimeMillis()));
        map.put("redisSoftwarePath", redisSoftwarePath);
        map.put("redisConfigPath", redisConfigPath);
        /*
        List<String> redisProceedingNames = RedisUtil.getListByKey("redisProceedingName");
        if (!StringUtils.isEmpty(redisProceedingNames) && !redisProceedingNames.contains(nickname)){
            RedisUtil.saveRedisList("redisTaskName", redisTaskName);
        }else {
            redisConfigPath = RedisUtil.getRedisHashField(redisTaskName, "redisConfigPath");
            if (redisConfigPath != null) {
                Common.killProcessByPort(new Integer(redisInfoMap.get("port").toString()));
            }
        }
        */
        RedisUtil.saveRedisHash(redisTaskName + "State", map);
    }

    /**
     * 更新redis进程状态
     *
     * @param username
     * @param process
     */
    public static void updateRedisProceedingState(String username, Process process) {
        Map<String, String> map = new HashMap<>();
        map.put("state", process.toString());
        RedisUtil.saveRedisHash(username, map);
    }

    public static Boolean saveRedisHash(String key, Map<String, String> map) {
        return RedisUtil.saveRedisHash(key, map);
    }

    public static Map<String, String> getRedisHashAll(String key) {
        return RedisUtil.getRedisHashAll(key);
    }

    /**
     * 取出key对应的list
     *
     * @param key
     * @return
     */
    public static List<String> getListFromRedis(String key) {
        return RedisUtil.getListByKey(key);
    }

    public static Boolean deleteRedisByKey(String key) {
        return RedisUtil.deleteByKey(key);
    }

    public static Boolean delListByValue(String key, String value) {
        return RedisUtil.delListByValue(key, value);
    }

    public static Boolean delHashByFeild(String key, String feild) {
        return RedisUtil.delHashByFeild(key, feild);
    }

    /**
     * 根据key取出redis中的hash
     *
     * @param key
     * @return
     */
    public static Map<String, String> getHashFromRedis(String key) {
        return RedisUtil.getRedisHashAll(key);
    }

    /**
     * 查到所有配置了redis服务的用户， 并查到相应的redis服务
     *
     * @return
     */
    public static List<Map<String, String>> getProcesses() {
        List<Map<String, String>> processes = new ArrayList<>();
        List<String> tasks = getListFromRedis("alreadyList");
        for (String string : tasks) {
            Map<String, String> map = RedisUtil.getRedisHashAll(string + "State");
            if (map != null) {
                map.put("taskName", string);
            }
            processes.add(map);
        }
        return processes;
    }

    /**
     * 生成redis配置文件， 并存储redis信息
     *
     * @param modelMap
     * @return
     */
    public static Boolean writeRedisConfig(Map modelMap) {
        AttributeBean attributeBean;    //用来存储RedisConfigBean中单个属性的type、name、value
        RedisConfigBean redisConfigBean = new RedisConfigBean();    //生成默认配置文件类
        try {
            FileWriter fw = new FileWriter(String.valueOf(modelMap.get("redisConfigPath")));            //
            Field[] fields = Common.getAttributeFields(redisConfigBean);    //获得redisConfigBean中所有的属性名字
            for (Field field : fields) {    //循环取出每个属性的内容
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean saveRedisList(String key, String value) {
        return RedisUtil.saveRedisList(key, value);
    }

    public static void killRedisTaskByName(String redisTaskName, Boolean isNeedDel) {
        Map redisTaskStateMap = getRedisHashAll(redisTaskName + "State");
        if (redisTaskStateMap.size() > 0) {
            int port = Integer.parseInt(redisTaskStateMap.get("port").toString());
            Common.killProcessByPort(port);
            if (isNeedDel) {
                RedisCommon.delListByValue("alreadyList", redisTaskName);
            }
        }
    }

    public static Boolean setExpire(String key, int seconds) {
        return RedisUtil.setExpire(key, seconds);
    }

    public static Boolean isInRedis(String key) {
        Set<String> keys = RedisUtil.getKeys();
        return !StringUtils.isEmpty(keys) && keys.contains(key);
    }

    public static Map<String, Object> getServiceByName(Object taskNameObj) {
        Map<String, Object> returnMap = new HashMap<>();

        Map<String, Object> serviceInfo = getInfoByName(taskNameObj);
        if (!serviceInfo.get("returnCode").equals("0")) {
            return serviceInfo;
        }

        String taskName = taskNameObj.toString();
        String returnCode = serviceInfo.get("returnCode").toString();
        String returnMessage = serviceInfo.get("returnMessage").toString();
        int port = (int) serviceInfo.get("port");

        RedisConnection redisConnection = new RedisConnection(port);
        if (!redisConnection.ping().equals("PONG")) {
            returnCode = "1";
            returnMessage = "Sorry！代号为" + taskName + "的服务未能连通！， 请先确认该服务已启动！";
        } else {
            returnMap.put("redisConnection", redisConnection);
        }

        returnMap.put("returnCode", returnCode);
        returnMap.put("returnMessage", returnMessage);

        return returnMap;
    }

    public static Map<String, Object> getInfoByName(Object taskNameObj) {
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isEmpty(taskNameObj)) {
            returnMap.put("returnCode", "1");
            returnMap.put("returnMessage", "Task name is null !");
            return returnMap;
        }
        String taskName = taskNameObj.toString();
        String returnCode = "0";
        String returnMessage = "";
        int port = 6379;
        if (!RedisCommon.getListFromRedis("alreadyList").contains(taskName)) {
            returnCode = "1";
            returnMessage = "未找到代号为" + taskName + "的服务！";
        } else {
            Map map = RedisCommon.getHashFromRedis(taskName + "State");
            Object portObj = StringUtils.isEmpty(map) ? null : map.get("port");
            if (StringUtils.isEmpty(portObj) || !Common.isInteger(portObj.toString())) {
                returnCode = "1";
                returnMessage = "系统异常，" + taskName + "的服务器信息未找到！";
            } else {
                port = Integer.parseInt(portObj.toString());
            }
        }

        returnMap.put("port", port);
        returnMap.put("returnCode", returnCode);
        returnMap.put("returnMessage", returnMessage);
        return returnMap;
    }

    public static Boolean hExists(String key, String field) {
        return RedisUtil.hExists(key, field);
    }

    public static String hGet(String key, String field) {
        return RedisUtil.hGet(key, field);
    }

    public static Long hDel(String key, String field) {
        return RedisUtil.hDel(key, field);
    }
}