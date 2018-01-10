package cn.fzz.web.controller;

import cn.fzz.framework.common.Common;
import cn.fzz.framework.redis.RedisCommon;
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
        if (!RedisCommon.writeRedisConfig(modelMap)) //生成redis配置文件
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
            RedisCommon.updateRedisProceedingState("username", process);
        }

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
            map.put("username", redisProcess.get("username"));
            map.put("state", StringUtils.isEmpty(redisProcess.get("state"))? "false":"success");
            map.put("version", redisProcess.get("version"));
            map.put("duration", String.valueOf((System.currentTimeMillis()-Long.valueOf(redisProcess.get("time")))
                    /(1000*60)) + "minutes");
            map.put("redisSoftwarePath", redisProcess.get("redisSoftwarePath"));
            map.put("redisConfigPath", redisProcess.get("redisConfigPath"));
            redisProcessInfo.add(map);
        }

        modelMap.addAttribute("processes", redisProcessInfo);
        return "redis_state";
    }

//    @RequestMapping(value = "/state")
//    public @ResponseBody
}
