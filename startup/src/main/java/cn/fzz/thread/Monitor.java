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
public class Monitor extends Thread{
    public void run(){
        while (true) {
            Map<String, String> resultMap;
            System.out.println(new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz").format(new Date()) +
                    "\tmachine1正在执行扫描任务...");
            List<String> configList = RedisCommon.getListFromRedis("machine1List");
            RedisCommon.deleteRedisByKey("machine1List");
            while (configList.size() > 0) {
                String key = configList.get(0);
                configList.remove(key);
                Map<String, String> map = RedisCommon.getRedisHashAll(key);
                if (!StringUtils.isEmpty(map)) {
                    System.out.println("成功接收" + key + "， 正在启动redis...\nconfig关键参数：" + map);
                    map.put("redisTaskName", key);
                    resultMap = createRedis(map);
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

            try {
                //睡3秒
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> createRedis(Map<String, String> config){
        Map<String, String> resultMap = new HashMap<>();

        //若存在相同编号的任务， 则杀死旧任务
        RedisCommon.killRedisTaskByName(config.get("redisTaskName"));

        //校验端口号是否可用, 若未接收到port则自动生成
        Integer port = Common.checkPort(config.get("port")); //如果用户传入的端口号已被占用, 则port为null
        if (StringUtils.isEmpty(port)) {
            resultMap.put("returnCode", "1");
            resultMap.put("error", "端口号已被占用！");
            return resultMap;
        }
        config.put("port", port.toString());

        String redisSoftwarePath = "../redis/redis-server.exe"; //定义redis程序位置
        String redisConfigPath = "web/src/main/resources/configs/redis" + port + ".conf";    //定义redis配置文件的位置
        config.put("redisConfigPath", redisConfigPath);
        config.put("redisSoftwarePath", redisSoftwarePath);
        if (!RedisCommon.writeRedisConfig(config)) //生成redis配置文件并存储信息
        {
            resultMap.put("returnCode", "1");
            resultMap.put("error", "生成配置文件失败！");
            return resultMap;
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
