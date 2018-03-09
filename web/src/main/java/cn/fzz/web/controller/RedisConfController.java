package cn.fzz.web.controller;

import cn.fzz.framework.common.Common;
import cn.fzz.framework.redis.RedisCommon;
import cn.fzz.framework.redis.RedisConnection;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/redisConf")
    public String createRedisConfig(@RequestParam Map<String, String> reqMap, ModelMap modelMap,
                                    HttpServletRequest request) {
        if (!"GET".equals(request.getMethod())) {
            Map<String, String> redisConf = new HashMap<>();

            // 取出reqMap中的有效 K V
            for (Object obj : reqMap.keySet()) {
                String value = reqMap.get(obj.toString());
                if (!StringUtils.isEmpty(value)) {
                    redisConf.put(obj.toString(), value);
                    modelMap.addAttribute(obj.toString(), value);
                }
            }
            if (!StringUtils.isEmpty(reqMap.get("machine")) && !StringUtils.isEmpty(reqMap.get("NO."))) {
                redisConf.remove("machine");
                redisConf.remove("NO.");
                String redisName = reqMap.get("machine") + "-NO." + reqMap.get("NO.");
                RedisCommon.delListByValue("failedList", redisName);
                RedisCommon.saveRedisList(reqMap.get("machine") + "List", redisName);
                RedisCommon.saveRedisHash(redisName, redisConf);
            }
        }

        List<String> taskList = new ArrayList<>();
        List<String> machineList = RedisCommon.getListFromRedis("machineList");
        for (String machine : machineList){
            taskList.addAll(RedisCommon.getListFromRedis(machine + "List"));
        }
        modelMap.addAttribute("taskList", taskList);

        modelMap.addAttribute("alreadyList", RedisCommon.getListFromRedis("alreadyList"));

        List<String> failedLogList = new ArrayList<>();
        List<String> failedList = RedisCommon.getListFromRedis("failedList");
        for (String failedName : failedList){
            failedLogList.add(failedName + "\t" + RedisCommon.getHashFromRedis("failedLog").get(failedName));
        }
        modelMap.addAttribute("failedList", failedLogList);
        return "redis_conf";
    }

    /**
     * 接收web转过来的配置参数并转换相应格式后写入文件
     * returnCode 0--success 1--false
     *
     * @param reqMap
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/configs")
    public String createConfig(@RequestParam Map<String, String> reqMap, ModelMap modelMap, HttpServletRequest request) {
        if ("GET".equals(request.getMethod())) {
            return "redis_conf";
        }

        // 取出reqMap中的有效 K V
        for (Object obj : reqMap.keySet()) {
            String value = reqMap.get(obj.toString());
            if (!StringUtils.isEmpty(value)) {
                if (obj.toString().equals("nickname")){
                    continue;
                }

                modelMap.addAttribute(obj.toString(), value);
            }
        }

        //校验端口号是否可用, 若未接收到port则自动生成
        Integer port = Common.checkPort(reqMap.get("port")); //如果用户传入的端口号已被占用, 则port为null
        if (StringUtils.isEmpty(port)) {
            modelMap.addAttribute("returnCode", "1");
            modelMap.addAttribute("error", "端口号已被占用！");
            return "redis_conf";
        }
        modelMap.put("port", port.toString());

        String redisSoftwarePath = "../redis/redis-server.exe"; //定义redis程序位置
        String redisConfigPath = "web/src/main/resources/configs/redis" + port + ".conf";    //定义redis配置文件的位置
        modelMap.put("redisSoftwarePath", redisSoftwarePath);
        modelMap.put("redisConfigPath", redisConfigPath);
        if (!RedisCommon.writeRedisConfig(modelMap)) //生成redis配置文件并存储信息
        {
            modelMap.addAttribute("returnCode", "1");
            modelMap.addAttribute("error", "生成配置文件失败！");
            return "redis_conf";
        }

        //判断是否要立即启动redis服务
        modelMap.put("isStart", "true");    //测试阶段手动赋值， 实际应是从页面获取
        if ("true".equals(modelMap.get("isStart"))) {
            Process process = Common.createProcess(redisSoftwarePath, redisConfigPath);
            if (process == null) {    //如果用户传入的端口号已被占用
                modelMap.addAttribute("returnCode", "1");
                modelMap.addAttribute("error", "子进程启动失败！");
                return "redis_conf";
            }
//            RedisCommon.updateRedisProceedingState(RedisCommon.nickname, process);
        }

        modelMap.put("port", Integer.valueOf(modelMap.get("port").toString())+1);
        return "redis_conf";
    }

    /**
     * redis进程状态
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/state")
    public String state(ModelMap modelMap) {
        List<Map<String, String>> redisProcesses = RedisCommon.getProcesses();
        List<Map> redisProcessInfo = new ArrayList<>();

        for (Map<String, String> redisProcess:redisProcesses){
            Map<String, String> map = new HashMap<>();
            map.put("taskName", redisProcess.get("taskName"));
            map.put("state", new RedisConnection(Integer.parseInt(redisProcess.get("port"))).ping().equals("PONG") ?
                    "success" : "fail");
            map.put("version", redisProcess.get("version"));
            map.put("duration", Common.getDurByTimeMillis(Long.valueOf(redisProcess.get("time"))));
            map.put("redisSoftwarePath", redisProcess.get("redisSoftwarePath"));
            map.put("redisConfigPath", redisProcess.get("redisConfigPath"));
            redisProcessInfo.add(map);
        }

        modelMap.addAttribute("processes", redisProcessInfo);
        return "redis_state";
    }
}
