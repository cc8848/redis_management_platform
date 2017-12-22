package cn.fzz.common;

import cn.fzz.bean.RedisConfigBean;
import cn.fzz.bean.common.AttributeBean;
import cn.fzz.util.RedisUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.springframework.util.StringUtils;

/**
 * Created by fanzezhen on 2017/12/19.
 * Desc:
 */
public class Common {
    //记录当前已使用的端口号
    private static List<Long> ports = new ArrayList<>();
    private static Long port = 6380L;

    /**
     * @param bean
     * @return
     */
    public static Field[] getAttributeFields(Object bean) {
        return bean.getClass().getDeclaredFields();
    }

    /**
     * @param field
     * @param bean
     * @return
     */
    public static AttributeBean getAttribute(Field field, Object bean) {
        String name = field.getName();
        String type = field.getGenericType().toString();                //获取属性的类型

        Object value = null;
        try {
            //将属性的首字符大写，构造get方法
            Method m = bean.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
            value = m.invoke(bean);     //调用getter方法获取属性值
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new AttributeBean(type, name, value);
    }

    /**
     * 根据软件路径和配置文件路径启动进程
     *
     * @param exePath
     * @param confPath
     * @return
     */
    public static Process createProcess(String exePath, String confPath) {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(exePath);
        commands.add(confPath);
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(commands);
        try {
            return pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

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
            return createProcess(redisSoftwarePath, redisConfigPath);
        }
        return null;
    }

    /**
     * 保存Redis程序信息
     *
     * @param username
     * @param redisSoftwarePath
     * @param redisConfigPath
     * @return
     */
    public static void saveRedisProceedingInfo(String username, String redisSoftwarePath, String redisConfigPath) {
        Map<String, String> map = new HashMap<>();
        map.put("state", "");
        map.put("version", "v3.2.100");
        map.put("time", String.valueOf(System.currentTimeMillis()));
        map.put("redisSoftwarePath", redisSoftwarePath);
        map.put("redisConfigPath", redisConfigPath);
        RedisUtil.saveRedisHash(username, map);
        RedisUtil.saveRedisList("redisProceedingName", username);
    }

    /**
     * 更新进程状态
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
            map.put("username", string);
            processes.add(map);
        }
        return processes;
    }

    /**
     * 校验端口号是否可用
     *
     * @param object
     * @return
     */
    public static Long checkPort(Object object) {
        if (StringUtils.isEmpty(object)) {  //如果未传入端口号， 则自动生成
            while (ports.indexOf(port) >= 0) {
                port++;
            }
        } else if (ports.indexOf(Long.valueOf(object.toString())) >= 0) {    //如果用户传入的端口号已被占用
            return null;
        }
        ports.add(port);
        return port;
    }

    /**
     * 生成redis配置文件
     *
     * @param redisConfigPath
     * @param modelMap
     * @return
     */
    public static Boolean writeRedisConfig(String redisConfigPath, Map modelMap) {
        AttributeBean attributeBean;    //用来存储RedisConfigBean中单个属性的type、name、value
        RedisConfigBean redisConfigBean = new RedisConfigBean();    //生成默认配置文件类
        try {
            FileWriter fw = new FileWriter(redisConfigPath);            //
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
}
