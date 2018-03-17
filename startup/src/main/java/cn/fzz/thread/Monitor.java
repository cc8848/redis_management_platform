package cn.fzz.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.fzz.framework.common.Common;
import cn.fzz.framework.redis.RedisCommon;
import org.springframework.util.StringUtils;

/**
 * Created by Administrator on 2018/3/8.
 * Desc:
 */
public class Monitor extends Thread {
    public void run() {
        while (true) {
            Map<String, String> resultMap;
            System.out.println(new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz").format(new Date()) +
                    "\tmachine1正在执行扫描任务...");

            //新的redis任务
            List<String> configList = RedisCommon.getListFromRedis("machine1List");
            RedisCommon.deleteRedisByKey("machine1List");
            while (configList.size() > 0) {
                String key = configList.get(0);
                configList.remove(key);
                Map<String, String> map = RedisCommon.getRedisHashAll(key);
                if (!StringUtils.isEmpty(map)) {
                    System.out.println("成功接收" + key + "， 正在启动redis...\nconfig关键参数：" + map);
                    map.put("redisTaskName", key);
                    resultMap = createRedis(map, false);
                    if (resultMap.get("returnCode").equals("0")) {
                        RedisCommon.saveRedisList("alreadyList", key);
                        System.out.println(key + "已启动！");
                    } else {
                        Map<String, String> errorMap = new HashMap<>();
                        errorMap.put(key, resultMap.get("error"));
                        RedisCommon.saveRedisList("failedList", key);
                        RedisCommon.saveRedisHash("failedLog", errorMap);
                        System.out.println(key + resultMap.get("error"));
                    }
                }
                RedisCommon.deleteRedisByKey(key);
            }

            //重启
            List<String> restartList = RedisCommon.getListFromRedis("machine1RestartList");
            RedisCommon.deleteRedisByKey("machine1RestartList");
            while (restartList.size() > 0) {
                String key = restartList.get(0);
                restartList.remove(key);
                Map<String, String> map = RedisCommon.getRedisHashAll(key + "State");
                if (!StringUtils.isEmpty(map)) {
                    System.out.println("成功接收到重启任务， 代号" + key + "\n正在启动redis...\nconfig关键参数：" + map);
                    map.put("redisTaskName", key);
                    resultMap = createRedis(map, true);
                    if (resultMap.get("returnCode").equals("0")) {
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("returnCode", "0");
                        RedisCommon.saveRedisHash("switchResultHash", map1);
                        System.out.println(key + "已重新启动！");
                    } else {
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("returnCode", "1");
                        RedisCommon.saveRedisHash("switchResultHash", map1);
                        System.out.println(key + resultMap.get("error"));
                    }
                }
            }

            //stop
            List<String> stopList = RedisCommon.getListFromRedis("machine1StopList");
            RedisCommon.deleteRedisByKey("machine1StopList");
            while (stopList.size() > 0) {
                String key = stopList.get(0);
                stopList.remove(key);
                if (!StringUtils.isEmpty(key)) {
                    System.out.println("成功接收到关闭任务， 代号" + key + "\n正在关闭...");
                    RedisCommon.killRedisTaskByName(key, false);
                    Map<String, String> map1 = new HashMap<>();
                    map1.put(key, "0");
                    RedisCommon.saveRedisHash("switchResultHash", map1);
                    System.out.println(key + "已关闭！");
                }
            }

            try {
                //睡3秒
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, String> createRedis(Map<String, String> config, Boolean isRestart) {
        Map<String, String> resultMap = new HashMap<>();

        //若存在相同编号的任务， 则杀死旧任务
        RedisCommon.killRedisTaskByName(config.get("redisTaskName"), !isRestart);

        String redisSoftwarePath; //定义redis程序位置
        String redisConfigPath;    //定义redis配置文件的位置
        if (isRestart) {
            redisSoftwarePath = config.get("redisSoftwarePath"); //定义redis程序位置
            redisConfigPath = config.get("redisConfigPath");    //定义redis配置文件的位置
        } else {
            //校验端口号是否可用, 若未接收到port则自动生成
            Integer port = Common.checkPort(config.get("port")); //如果用户传入的端口号已被占用, 则port为null
            System.out.println("monitor: " + port);
            if (StringUtils.isEmpty(port)) {
                resultMap.put("returnCode", "1");
                resultMap.put("error", "端口号已被占用！");
                return resultMap;
            }
            config.put("port", port.toString());

            redisSoftwarePath = "../redis/redis-server.exe"; //定义redis程序位置
            redisConfigPath = "web/src/main/resources/configs/redis" + port + ".conf";    //定义redis配置文件的位置
            config.put("redisConfigPath", redisConfigPath);
            config.put("redisSoftwarePath", redisSoftwarePath);
            if (!RedisCommon.writeRedisConfig(config)) //生成redis配置文件并存储信息
            {
                resultMap.put("returnCode", "1");
                resultMap.put("error", "生成配置文件失败！");
                return resultMap;
            }
        }

        Process process = Common.createProcess(redisSoftwarePath, redisConfigPath);
        if (process == null) {
            resultMap.put("returnCode", "1");
            resultMap.put("error", "Create process failed！");
            return resultMap;
        }

        RedisCommon.saveRedisProceedingInfo(config); //保存Redis程序信息
        resultMap.put("returnCode", "0");
        return resultMap;
    }
}
