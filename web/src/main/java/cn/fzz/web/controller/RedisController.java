package cn.fzz.web.controller;

import cn.fzz.bean.RedisInfoCPU;
import cn.fzz.bean.RedisInfoClients;
import cn.fzz.bean.RedisInfoMemory;
import cn.fzz.bean.filter.DangerousEventFilter;
import cn.fzz.bean.filter.SubscriberEventFilter;
import cn.fzz.framework.common.Common;
import cn.fzz.framework.redis.RedisCommon;
import cn.fzz.framework.redis.RedisConnection;

import cn.fzz.service.RedisLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fanzezhen on 2017/12/19.
 * Desc:
 */
@Controller
@RequestMapping("/redis")
public class RedisController {
    private final RedisLogService redisLogService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public RedisController(RedisLogService redisLogService) {
        this.redisLogService = redisLogService;
    }

    @RequestMapping(value = "/config")
    public String config(@RequestParam Map<String, String> reqMap, ModelMap modelMap,
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
        for (String machine : machineList) {
            taskList.addAll(RedisCommon.getListFromRedis(machine + "List"));
        }
        modelMap.addAttribute("taskList", taskList);

        modelMap.addAttribute("alreadyList", RedisCommon.getListFromRedis("alreadyList"));

        List<String> failedLogList = new ArrayList<>();
        List<String> failedList = RedisCommon.getListFromRedis("failedList");
        for (String failedName : failedList) {
            failedLogList.add(failedName + "\t" + RedisCommon.getHashFromRedis("failedLog").get(failedName));
        }
        modelMap.addAttribute("failedList", failedLogList);
        return "redis_conf";
    }

    // 停用
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
                if (obj.toString().equals("nickname")) {
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

        modelMap.put("port", Integer.valueOf(modelMap.get("port").toString()) + 1);
        return "redis_conf";
    }

    /**
     * redis进程状态
     *
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/state")
    public String state(ModelMap modelMap) {
        List<Map<String, String>> redisProcesses = RedisCommon.getProcesses();
        List<Map> redisProcessInfo = new ArrayList<>();

        for (Map<String, String> redisProcess : redisProcesses) {
            Map<String, String> map = new HashMap<>();
            map.put("taskName", redisProcess.get("taskName"));
            String portString = redisProcess.get("port");
            map.put("state", StringUtils.isEmpty(portString) ? "fail" :
                    new RedisConnection(Integer.parseInt(portString)).ping().equals("PONG") ? "success" : "fail");
            map.put("version", redisProcess.get("version"));
            String timeString = redisProcess.get("time");
            map.put("duration", StringUtils.isEmpty(timeString) ? "error!" :
                    Common.getDurByTimeMillis(Long.valueOf(timeString)));
            map.put("redisSoftwarePath", redisProcess.get("redisSoftwarePath"));
            map.put("redisConfigPath", redisProcess.get("redisConfigPath"));
            redisProcessInfo.add(map);
        }

        modelMap.addAttribute("processes", redisProcessInfo);
        return "redis_state";
    }

    //停用
    @RequestMapping(value = "/data_back")
    public String data_back(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        List<Map> stringList = new ArrayList<>();
        List<Map> listList = new ArrayList<>();
        List<Map> hashList = new ArrayList<>();
        Map<String, List> redisInfoMap = new HashMap<>();

        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0")) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");

            String redisInfoStr = redisConnection.getInfo().replace("\r", "");
            String[] redisInfoArr = redisInfoStr.split("# ");
            for (String string : redisInfoArr) {
                String[] redisInfoPartArr = string.split("\n");
                if (redisInfoPartArr.length > 1) {
                    List<String> redisInfoPartList = new ArrayList<>(Arrays.asList(redisInfoPartArr)
                            .subList(1, redisInfoPartArr.length));
                    redisInfoMap.put(redisInfoPartArr[0], redisInfoPartList);
                }
            }

            Set<String> keys = redisConnection.getKeys();
            for (String k : keys) {
                Map<String, String> map = new HashMap<>();
                String type = redisConnection.getTypeByKey(k);
                String encoding = redisConnection.getEncodingByKey(k);
//            Long refCount = redisConnection.getCountByKey(k);
                Long idleTime = redisConnection.getIdleTimeByKey(k);
                Long validTime = redisConnection.getValidTimeByKey(k);
                map.put("key", k);
                map.put("encoding", encoding);
//                    map.put("count", refCount.toString());
                map.put("idleTime", idleTime.toString() + "s");
                map.put("validTime", validTime == -1 ? "Forever!" : validTime.toString() + "s");

                switch (type) {
                    case "string":
                        stringList.add(map);
                        break;
                    case "list":
                        listList.add(map);
                        break;
                    case "hash":
                        hashList.add(map);
                        break;
                    default:
                        logger.info("未匹配的数据类型--" + type);
                }
            }
        }
        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("stringList", stringList);
        modelMap.addAttribute("listList", listList);
        modelMap.addAttribute("hashList", hashList);
        modelMap.addAttribute("redisInfoMap", redisInfoMap);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("message", message);
        return "redis_data_back";
    }

    @RequestMapping(value = "/stringData")
    public String stringData(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        List<Map> stringList = new ArrayList<>();
        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0")) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");

            Set<String> keys = redisConnection.getKeys();
            for (String k : keys) {
                Map<String, String> map = new HashMap<>();
                String type = redisConnection.getTypeByKey(k);
                if (type.equals("string")) {
                    String encoding = redisConnection.getEncodingByKey(k);
//            Long refCount = redisConnection.getCountByKey(k);
                    Long idleTime = redisConnection.getIdleTimeByKey(k);
                    Long validTime = redisConnection.getValidTimeByKey(k);
                    map.put("key", k);
                    map.put("encoding", encoding);
//                    map.put("count", refCount.toString());
                    map.put("idleTime", idleTime.toString() + "s");
                    map.put("validTime", validTime == -1 ? "Forever!" : validTime.toString() + "s");
                    stringList.add(map);
                }
            }
        }
        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("stringList", stringList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("message", message);
        modelMap.addAttribute("pageCount", stringList.size() / 10 + 1);
        modelMap.addAttribute("page", StringUtils.isEmpty(reqMap.get("page")) ?
                1 : Integer.parseInt(reqMap.get("page")));
        return "redis_string_data";
    }

    @RequestMapping(value = "/listData")
    public String listData(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        List<Map> stringList = new ArrayList<>();
        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0")) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");

            Set<String> keys = redisConnection.getKeys();
            for (String k : keys) {
                Map<String, String> map = new HashMap<>();
                String type = redisConnection.getTypeByKey(k);
                if (type.equals("list")) {
                    String encoding = redisConnection.getEncodingByKey(k);
                    Long length = redisConnection.getListLen(k);
                    Long idleTime = redisConnection.getIdleTimeByKey(k);
                    Long validTime = redisConnection.getValidTimeByKey(k);
                    map.put("key", k);
                    map.put("encoding", encoding);
                    map.put("length", length.toString());
                    map.put("idleTime", idleTime.toString() + "s");
                    map.put("validTime", validTime == -1 ? "Forever!" : validTime.toString() + "s");
                    stringList.add(map);
                }
            }
        }
        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("stringList", stringList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("message", message);
        modelMap.addAttribute("pageCount", stringList.size() / 10 + 1);
        modelMap.addAttribute("page", StringUtils.isEmpty(reqMap.get("page")) ?
                1 : Integer.parseInt(reqMap.get("page")));
        return "redis_list";
    }

    @RequestMapping(value = "/listDetail")
    public String listDetail(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        List<String> valueList = new ArrayList<>();
        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        String key = reqMap.get("key");
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0") && !StringUtils.isEmpty(key)) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");
            valueList = redisConnection.getListByKey(key);
        }

        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("valueList", valueList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("message", message);
        modelMap.addAttribute("pageCount", valueList.size() / 10 + 1);
        modelMap.addAttribute("page", StringUtils.isEmpty(reqMap.get("page")) ?
                1 : Integer.parseInt(reqMap.get("page")));
        return "redis_list_detail";
    }

    @RequestMapping(value = "/hashData")
    public String hashData(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        List<Map> stringList = new ArrayList<>();
        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0")) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");

            Set<String> keys = redisConnection.getKeys();
            for (String k : keys) {
                Map<String, String> map = new HashMap<>();
                String type = redisConnection.getTypeByKey(k);
                if (type.equals("hash")) {
                    String encoding = redisConnection.getEncodingByKey(k);
                    Long count = redisConnection.getHashCount(k);
                    Long idleTime = redisConnection.getIdleTimeByKey(k);
                    Long validTime = redisConnection.getValidTimeByKey(k);
                    map.put("key", k);
                    map.put("encoding", encoding);
                    map.put("count", count.toString());
                    map.put("idleTime", idleTime.toString() + "s");
                    map.put("validTime", validTime == -1 ? "Forever!" : validTime.toString() + "s");
                    stringList.add(map);
                }
            }
        }
        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("stringList", stringList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("message", message);
        modelMap.addAttribute("pageCount", stringList.size() / 10 + 1);
        modelMap.addAttribute("page", StringUtils.isEmpty(reqMap.get("page")) ?
                1 : Integer.parseInt(reqMap.get("page")));
        return "redis_hash";
    }

    @RequestMapping(value = "/hashDetail")
    public String hashDetail(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        List<Map> hashList = new ArrayList<>();
        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        String key = reqMap.get("key");
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0") && !StringUtils.isEmpty(key)) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");
            Map<String, String> redisMap = redisConnection.getRedisHashAll(key);
            for (Map.Entry<String, String> entry : redisMap.entrySet()) {
                Map<String, String> map = new HashMap<>();
                map.put("field", entry.getKey());
                map.put("value", entry.getValue());
                hashList.add(map);
            }
        }

        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("hashList", hashList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("message", message);
        modelMap.addAttribute("pageCount", hashList.size() / 10 + 1);
        modelMap.addAttribute("page", StringUtils.isEmpty(reqMap.get("page")) ?
                1 : Integer.parseInt(reqMap.get("page")));
        return "redis_hash_detail";
    }

    @RequestMapping(value = "/admin")
    public String admin(ModelMap modelMap) {
        String taskName = "localhost";
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");

        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("message", message);
        return "redis_admin";
    }

    @RequestMapping(value = "/find")
    public @ResponseBody
    Map<String, Object> find(String reqJsonString) {
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(reqJsonString);
        Object type = jsonObject.get("typeFlag");
        Object key = jsonObject.get("key");
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(key)) {
            resultMap.put("returnCode", "1");
            resultMap.put("message", "数据类型或键名无效！");
            return resultMap;
        }

        String taskName = StringUtils.isEmpty(jsonObject.get("taskName")) ?
                "localhost" : jsonObject.get("taskName").toString();
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0")) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");

            switch (type.toString()) {
                case "string":
                    resultMap.put("string", redisConnection.getStringByKey(key.toString()));
                    break;
                case "list":
                    StringBuilder listString = new StringBuilder();
                    for (String field : redisConnection.getListByKey(key.toString())) {
                        listString.append(field).append("\n");
                    }
                    resultMap.put("list", listString.toString());
                    break;
                case "hash":
                    Object childKey = jsonObject.get("child_key");
                    if (StringUtils.isEmpty(childKey)) {
                        StringBuilder hashString = new StringBuilder();
                        for (Map.Entry<String, String> entry : redisConnection.getRedisHashAll(key.toString()).entrySet()) {
                            hashString.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                        }
                        resultMap.put("hash", hashString);
                    } else {
                        resultMap.put("hash", redisConnection.getHashFeildValue(key.toString(), childKey.toString()));
                    }
                    break;
                default:
                    returnCode = "1";
                    message = "数据类型未找到！";
            }
        }

        resultMap.put("returnCode", returnCode);
        resultMap.put("message", message);
        return resultMap;
    }

    @RequestMapping(value = "/change")
    public @ResponseBody
    Map<String, Object> change(String reqJsonString) {
        Map<String, Object> resultMap = new HashMap<>();
        Boolean isOK = false;
        JSONObject jsonObject = JSONObject.parseObject(reqJsonString);
        Object type = jsonObject.get("typeFlag");
        Object key = jsonObject.get("key");
        Object value = jsonObject.get("value");
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(key)) {
            resultMap.put("returnCode", "1");
            resultMap.put("message", "数据类型或键名无效！");
            return resultMap;
        }

        String taskName = StringUtils.isEmpty(jsonObject.get("taskName")) ?
                "localhost" : jsonObject.get("taskName").toString();
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0")) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");
            switch (type.toString()) {
                case "string":
                    isOK = redisConnection.saveRedisString(key.toString(), StringUtils.isEmpty(value) ? "" : value.toString());
                    break;
                case "list":
                    if (StringUtils.isEmpty(value)) {
                        isOK = redisConnection.saveRedisList(key.toString(), "");
                    } else {
                        String[] listFields = value.toString().replace("\r", "").split("\n");
                        for (String field : listFields) {
                            isOK = redisConnection.saveRedisList(key.toString(), field);
                            if (!isOK) {
                                break;
                            }
                        }
                    }
                    break;
                case "hash":
                    Object childKey = jsonObject.get("child_key");
                    Map<String, String> map = new HashMap<>();
                    if (StringUtils.isEmpty(childKey)) {
                        String[] hashFields = value.toString().replace("\r", "").split("\n");
                        for (String field : hashFields) {
                            if (field.contains(":")) {
                                String[] keyAndValue = field.split(":", 2);
                                map.put(Common.trim(keyAndValue[0]), keyAndValue.length > 1 ? Common.trim(keyAndValue[1]) : "");
                            } else if (field.contains("：")) {
                                resultMap.put("returnCode", "1");
                                resultMap.put("message", "请使用英文冒号分割 k v ！");
                                return resultMap;
                            }
                        }
                    } else {
                        map.put(childKey.toString(), value.toString());
                    }
                    isOK = redisConnection.saveRedisHash(key.toString(), map);
                    break;
                default:
                    resultMap.put("returnCode", "1");
                    resultMap.put("message", "数据类型未找到！");
            }
        }

        returnCode = isOK ? "0" : "1";
        resultMap.put("returnCode", returnCode);
        resultMap.put("message", message);
        return resultMap;
    }

    @RequestMapping(value = "/delete")
    public @ResponseBody
    Map<String, Object> delete(String reqJsonString) {
        Map<String, Object> resultMap = new HashMap<>();
        Boolean isOK = false;
        JSONObject jsonObject = JSONObject.parseObject(reqJsonString);
        Object type = jsonObject.get("typeFlag");
        Object key = jsonObject.get("key");
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(key)) {
            resultMap.put("returnCode", "1");
            resultMap.put("message", "数据类型或键名无效！");
            return resultMap;
        }

        String taskName = StringUtils.isEmpty(jsonObject.get("taskName")) ?
                "localhost" : jsonObject.get("taskName").toString();
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0")) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");
            switch (type.toString()) {
                case "string":
                case "list":
                    isOK = redisConnection.deleteByKey(key.toString());
                    break;
                case "hash":
                    Object childKey = jsonObject.get("child_key");
                    if (StringUtils.isEmpty(childKey)) {
                        isOK = redisConnection.deleteByKey(key.toString());
                    } else {
                        isOK = redisConnection.delHashByField(key.toString(), childKey.toString());
                    }
                    break;
                default:
                    resultMap.put("returnCode", "1");
                    resultMap.put("message", "数据类型未找到！");
            }
        }

        returnCode = isOK ? "0" : "1";
        resultMap.put("returnCode", returnCode);
        resultMap.put("message", message);
        return resultMap;
    }

    @RequestMapping(value = "/expire")
    public @ResponseBody
    Map<String, Object> expire(String reqJsonString) {
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(reqJsonString);
        Object validTime = jsonObject.get("valid_time");
        Object key = jsonObject.get("key");
        if (StringUtils.isEmpty(validTime) || StringUtils.isEmpty(key) ||
                !(Common.isInteger(validTime.toString()) && RedisCommon.isInRedis(key.toString()))) {
            resultMap.put("returnCode", "1");
            resultMap.put("message", "参数无效！");
            return resultMap;
        }

        String taskName = StringUtils.isEmpty(jsonObject.get("taskName")) ?
                "localhost" : jsonObject.get("taskName").toString();
        Map<String, Object> redisServiceMap = RedisCommon.getServiceByName(taskName);
        String returnCode = redisServiceMap.get("returnCode").toString();
        String message = redisServiceMap.get("returnMessage").toString();

        if (returnCode.equals("0")) {
            RedisConnection redisConnection = (RedisConnection) redisServiceMap.get("redisConnection");
            returnCode = redisConnection.setExpire(key.toString(), Integer.parseInt(validTime.toString())) ? "0" : "1";
            if (!returnCode.equals("0")) {
                message = "未知原因导致设置失败！";
            }
        }
        resultMap.put("returnCode", returnCode);
        resultMap.put("message", message);
        return resultMap;
    }

    @RequestMapping(value = "/controlSwitch")
    public @ResponseBody
    Map<String, Object> controlSwitch(String reqJsonString) {
        Map<String, Object> resultMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(reqJsonString);
        Object taskName = jsonObject.get("taskName");
        Object directive = jsonObject.get("directive");
        if (StringUtils.isEmpty(directive)) {
            resultMap.put("returnCode", "1");
            resultMap.put("returnMessage", "Directive is null !");
            return resultMap;
        }
        Map<String, Object> infoMap = RedisCommon.getInfoByName(taskName);
        if (!infoMap.get("returnCode").equals("0")) {
            return infoMap;
        }

        switch (directive.toString()) {
            case "start":
                RedisCommon.saveRedisList(taskName.toString().split("-NO.")[0] + "RestartList", taskName.toString());
                break;
            case "stop":
                RedisCommon.saveRedisList(taskName.toString().split("-NO.")[0] + "StopList", taskName.toString());
                break;
            default:
                resultMap.put("returnCode", "1");
                resultMap.put("returnMessage", "Directive is wrong !");
                return resultMap;
        }

        String returnCode = "0";
        String returnMessage = "";
        Long time = System.currentTimeMillis();
        while (!RedisCommon.hExists("switchResultHash", taskName.toString())) {
            if (System.currentTimeMillis() - time > 3000) {
                logger.info("获取返回结果超时！");
                returnCode = "1";
                returnMessage = "Timeout !";
                break;
            }
            logger.info("正在等待服务器" + taskName.toString() + "的返回结果...");

            try {
                //睡3秒
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(Arrays.toString(e.getStackTrace()));
            }
        }

        if (returnCode.equals("0")) {
            returnCode = RedisCommon.hGet("switchResultHash", taskName.toString());
            switch (returnCode) {
                case "0":
                    returnMessage = "success!";
                    break;
                case "1":
                    returnMessage = "error!";
                    break;
                default:
                    returnMessage = "未知错误!";
            }
        }
        resultMap.put("returnCode", returnCode);
        resultMap.put("returnMessage", returnMessage);
        resultMap.put("state", new RedisConnection(Integer.parseInt(infoMap.get("port").toString()))
                .ping().equals("PONG") ? "success" : "fail");
        return resultMap;
    }

    @RequestMapping(value = "/cpu")
    public String cpu(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        String period = StringUtils.isEmpty(reqMap.get("period")) ? "minute" : reqMap.get("period");
        Map<String, Object> redisInfoMap = RedisCommon.getInfoByName(taskName);
        String returnCode = redisInfoMap.get("returnCode").toString();
        String returnMessage = redisInfoMap.get("returnMessage").toString();
        if (returnCode.equals("0")) {
            List<Float> sysList = new ArrayList<>();
            List<String> abscissa = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            if (period.equals("minute")) {
                for (RedisInfoCPU redisInfoCPU : redisLogService.getSevenCPUByName(taskName)) {
                    sysList.add(redisInfoCPU.getUsed_cpu_sys());
                    abscissa.add(simpleDateFormat.format(redisInfoCPU.getDate()));
                }
            } else {
                simpleDateFormat = new SimpleDateFormat("HH:00:00");
                Long time = System.currentTimeMillis();
                Date date1 = new Date(time);
                date1.setMinutes(0);
                date1.setSeconds(0);
                Date date2 = new Date(time);
                date2.setMinutes(0);
                date2.setSeconds(0);
                for (int i = 0; i < 7; i++) {
                    date2.setHours(date1.getHours() + 1);
                    RedisInfoCPU redisInfoCPU = redisLogService.getRedisCPUByDate1(taskName, date1, date2);
                    sysList.add(StringUtils.isEmpty(redisInfoCPU) ? 0 : redisInfoCPU.getUsed_cpu_sys());
                    abscissa.add(StringUtils.isEmpty(redisInfoCPU) ?
                            simpleDateFormat.format(date1) : simpleDateFormat.format(redisInfoCPU.getDate()));
                    date1.setHours(date1.getHours() - 1);
                }
            }
            modelMap.addAttribute("used_cpu_sys", sysList);
            modelMap.addAttribute("abscissa", abscissa);
        }
        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("period", period);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("returnMessage", returnMessage);
        return "redis_cpu";
    }

    @RequestMapping(value = "/clients")
    public String clients(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        String period = StringUtils.isEmpty(reqMap.get("period")) ? "minute" : reqMap.get("period");
        Map<String, Object> redisInfoMap = RedisCommon.getInfoByName(taskName);
        String returnCode = redisInfoMap.get("returnCode").toString();
        String returnMessage = redisInfoMap.get("returnMessage").toString();
        if (returnCode.equals("0")) {
            List<Integer> clientsList = new ArrayList<>();
            List<String> abscissa = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            if (period.equals("minute")) {
                for (RedisInfoClients redisInfoClients : redisLogService.getSevenClientsByName(taskName)) {
                    clientsList.add(redisInfoClients.getConnected_clients());
                    abscissa.add(simpleDateFormat.format(redisInfoClients.getDate()));
                }
            } else {
                simpleDateFormat = new SimpleDateFormat("HH:00:00");
                Long time = System.currentTimeMillis();
                Date date1 = new Date(time);
                date1.setMinutes(0);
                date1.setSeconds(0);
                Date date2 = new Date(time);
                date2.setMinutes(0);
                date2.setSeconds(0);
                for (int i = 0; i < 7; i++) {
                    date2.setHours(date1.getHours() + 1);
                    RedisInfoClients redisInfoClients = redisLogService.getRedisClientsByDate1(taskName, date1, date2);
                    clientsList.add(StringUtils.isEmpty(redisInfoClients) ? 0 : redisInfoClients.getConnected_clients());
                    abscissa.add(StringUtils.isEmpty(redisInfoClients) ?
                            simpleDateFormat.format(date1) : simpleDateFormat.format(redisInfoClients.getDate()));
                    date1.setHours(date1.getHours() - 1);
                }
            }
            modelMap.addAttribute("connected_clients", clientsList);
            modelMap.addAttribute("abscissa", abscissa);
        }
        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("returnMessage", returnMessage);
        return "redis_clients";
    }

    @RequestMapping(value = "/memory")
    public String memory(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        String period = StringUtils.isEmpty(reqMap.get("period")) ? "minute" : reqMap.get("period");
        Map<String, Object> redisInfoMap = RedisCommon.getInfoByName(taskName);
        String returnCode = redisInfoMap.get("returnCode").toString();
        String returnMessage = redisInfoMap.get("returnMessage").toString();
        if (returnCode.equals("0")) {
            List<Float> luaList = new ArrayList<>();
            List<Float> rssList = new ArrayList<>();
            List<Float> usedList = new ArrayList<>();
            List<Float> peakList = new ArrayList<>();
            List<String> abscissa = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            if (period.equals("minute")) {
                for (RedisInfoMemory redisInfoMemory : redisLogService.getSevenMemoryByName(taskName)) {
                    luaList.add(redisInfoMemory.getUsed_memory_lua_human());
                    rssList.add(redisInfoMemory.getUsed_memory_rss_human());
                    usedList.add(redisInfoMemory.getUsed_memory_human());
                    peakList.add(redisInfoMemory.getUsed_memory_peak_human());
                    abscissa.add(simpleDateFormat.format(redisInfoMemory.getDate()));
                }
            } else {
                simpleDateFormat = new SimpleDateFormat("HH:00:00");
                Long time = System.currentTimeMillis();
                Date date1 = new Date(time);
                date1.setMinutes(0);
                date1.setSeconds(0);
                Date date2 = new Date(time);
                date2.setMinutes(0);
                date2.setSeconds(0);
                for (int i = 0; i < 7; i++) {
                    date2.setHours(date1.getHours() + 1);
                    RedisInfoMemory redisInfoMemory = redisLogService.getRedisMemoryByDate1(taskName, date1, date2);
                    if (!StringUtils.isEmpty(redisInfoMemory)) {
                        luaList.add(redisInfoMemory.getUsed_memory_lua_human());
                        rssList.add(redisInfoMemory.getUsed_memory_rss_human());
                        usedList.add(redisInfoMemory.getUsed_memory_human());
                        peakList.add(redisInfoMemory.getUsed_memory_peak_human());
                        abscissa.add(simpleDateFormat.format(redisInfoMemory.getDate()));
                    } else {
                        luaList.add(0f);
                        rssList.add(0f);
                        usedList.add(0f);
                        peakList.add(0f);
                        abscissa.add(simpleDateFormat.format(date1));
                    }
                    date1.setHours(date1.getHours() - 1);
                }
            }
            modelMap.addAttribute("used_memory_lua_human", luaList);
            modelMap.addAttribute("used_memory_rss_human", rssList);
            modelMap.addAttribute("used_memory_human", usedList);
            modelMap.addAttribute("used_memory_peak_human", peakList);
            modelMap.addAttribute("abscissa", abscissa);
        }
        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("returnMessage", returnMessage);
        return "redis_memory";
    }

    @RequestMapping(value = "/data")
    public String data(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        List<String> alreadyList = RedisCommon.getListFromRedis("alreadyList");
        String taskName = StringUtils.isEmpty(reqMap.get("taskName")) ? "localhost" : reqMap.get("taskName");
        String period = StringUtils.isEmpty(reqMap.get("period")) ? "minute" : reqMap.get("period");
        Map<String, Object> redisInfoMap = RedisCommon.getInfoByName(taskName);
        String returnCode = redisInfoMap.get("returnCode").toString();
        String returnMessage = redisInfoMap.get("returnMessage").toString();
        if (returnCode.equals("0")) {
            List<String> abscissa = new ArrayList<>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            List<Float> sysList = new ArrayList<>();
            List<Integer> clientsList = new ArrayList<>();
            List<Float> luaList = new ArrayList<>();
            List<Float> rssList = new ArrayList<>();
            List<Float> usedList = new ArrayList<>();
            List<Float> peakList = new ArrayList<>();
            if (period.equals("minute")) {
                for (RedisInfoCPU redisInfoCPU : redisLogService.getSevenCPUByName(taskName)) {
                    sysList.add(redisInfoCPU.getUsed_cpu_sys());
                    abscissa.add(simpleDateFormat.format(redisInfoCPU.getDate()));
                }
                for (RedisInfoClients redisInfoClients : redisLogService.getSevenClientsByName(taskName)) {
                    clientsList.add(redisInfoClients.getConnected_clients());
                }
                for (RedisInfoMemory redisInfoMemory : redisLogService.getSevenMemoryByName(taskName)) {
                    usedList.add(redisInfoMemory.getUsed_memory_human());
                    luaList.add(redisInfoMemory.getUsed_memory_lua_human());
                    rssList.add(redisInfoMemory.getUsed_memory_rss_human());
                    peakList.add(redisInfoMemory.getUsed_memory_peak_human());
                }
            } else {
                simpleDateFormat = new SimpleDateFormat("HH:00:00");
                Long time = System.currentTimeMillis();
                Date date1 = new Date(time);
                date1.setMinutes(0);
                date1.setSeconds(0);
                Date date2 = new Date(time);
                date2.setMinutes(0);
                date2.setSeconds(0);
                for (int i = 0; i < 7; i++) {
                    date2.setHours(date1.getHours() + 1);
                    RedisInfoCPU redisInfoCPU = redisLogService.getRedisCPUByDate1(taskName, date1, date2);
                    sysList.add(StringUtils.isEmpty(redisInfoCPU) ? 0 : redisInfoCPU.getUsed_cpu_sys());
                    abscissa.add(StringUtils.isEmpty(redisInfoCPU) ?
                            simpleDateFormat.format(date1) : simpleDateFormat.format(redisInfoCPU.getDate()));

                    RedisInfoClients redisInfoClients = redisLogService.getRedisClientsByDate1(taskName, date1, date2);
                    clientsList.add(StringUtils.isEmpty(redisInfoClients) ? 0 : redisInfoClients.getConnected_clients());
                    abscissa.add(StringUtils.isEmpty(redisInfoClients) ?
                            simpleDateFormat.format(date1) : simpleDateFormat.format(redisInfoClients.getDate()));

                    RedisInfoMemory redisInfoMemory = redisLogService.getRedisMemoryByDate1(taskName, date1, date2);
                    if (!StringUtils.isEmpty(redisInfoMemory)) {
                        usedList.add(redisInfoMemory.getUsed_memory_human());
                        luaList.add(redisInfoMemory.getUsed_memory_lua_human());
                        rssList.add(redisInfoMemory.getUsed_memory_rss_human());
                        peakList.add(redisInfoMemory.getUsed_memory_peak_human());
                        abscissa.add(simpleDateFormat.format(redisInfoMemory.getDate()));
                    } else {
                        luaList.add(0.0f);
                        rssList.add(0.0f);
                        usedList.add(0.0f);
                        peakList.add(0.0f);
                        abscissa.add(simpleDateFormat.format(date1));
                    }

                    date1.setHours(date1.getHours() - 1);
                }
            }
            modelMap.addAttribute("abscissa", abscissa);
            modelMap.addAttribute("used_cpu_sys", sysList);
            modelMap.addAttribute("connected_clients", clientsList);
            modelMap.addAttribute("used_memory_lua_human", luaList);
            modelMap.addAttribute("used_memory_rss_human", rssList);
            modelMap.addAttribute("used_memory_human", usedList);
            modelMap.addAttribute("used_memory_peak_human", peakList);
        }
        modelMap.addAttribute("taskName", taskName);
        modelMap.addAttribute("alreadyList", alreadyList);
        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("returnMessage", returnMessage);
        return "redis_data";
    }

    // expireEvents  过期事件
    @RequestMapping(value = "/subscriberEvents")
    public String subscriberEvents(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        int returnCode = 0;
        String returnMessage = "";
        String event_id = reqMap.get("event_id");
        if (!StringUtils.isEmpty(event_id)){
            returnCode =  redisLogService.solveSubscriberEvent(event_id);
            if (returnCode == 1){
                returnCode = 0;
            }else {
                returnCode = 1;
            }
        }

        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("returnMessage", returnMessage);
        return "redis_subscriber";
    }

    @RequestMapping(value = "/getSubscriberEvents")
    public @ResponseBody
    Map<String, Object> getSubscriberEvents(@RequestParam Map<Object, Object> reqMap, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        int returnCode = 0;
        String returnMessage = "";

        // 取出reqMap中的有效 K V
        SubscriberEventFilter subscriberEventFilter = new SubscriberEventFilter();
        Map map = new HashMap();
        for (Object obj : reqMap.keySet()) {
            if (obj.toString().equals("key")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    subscriberEventFilter.setRedis_key(value.toString());
                }
            }

            if (obj.toString().equals("server")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    subscriberEventFilter.setServer(value.toString());
                }
            }

            if (obj.toString().equals("event_type")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    subscriberEventFilter.setEvent_type_list(Arrays.asList(value.toString().split(",")));
                }
            }

            if (obj.toString().equals("notice_type")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    subscriberEventFilter.setNotice_type_list(Arrays.asList(value.toString().split(",")));
                }
            }

            if (obj.toString().equals("is_resolved")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    if (value.toString().equals("true"))
                        subscriberEventFilter.setIs_resolved(true);
                }
            }

            if (obj.toString().equals("start_date")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    subscriberEventFilter.setCreate_time_start(value.toString());
                }
            }

            if (obj.toString().equals("end_date")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    subscriberEventFilter.setCreate_time_end(value.toString());
                }
            }
        }

        List expireEvents = redisLogService.getSubscriberEventsByFilter(subscriberEventFilter);
        resultMap.put("total", redisLogService.getSubscriberEventsCountByFilter(subscriberEventFilter));
        resultMap.put("rows", expireEvents);


        resultMap.put("returnCode", returnCode);
        resultMap.put("returnMessage", returnMessage);
        return resultMap;
    }

    // dangerousEvents  报警事件
    @RequestMapping(value = "/dangerousEvents")
    public String dangerousEvents(@RequestParam Map<String, String> reqMap, ModelMap modelMap) {
        int returnCode = 0;
        String returnMessage = "";
        String event_id = reqMap.get("event_id");
        if (!StringUtils.isEmpty(event_id)){
            returnCode =  redisLogService.solveDangerousEvent(event_id);
            if (returnCode == 1){
                returnCode = 0;
            }else {
                returnCode = 1;
            }
        }

        modelMap.addAttribute("returnCode", returnCode);
        modelMap.addAttribute("returnMessage", returnMessage);
        return "redis_dangerous";
    }

    @RequestMapping(value = "/getDangerousEvents")
    public @ResponseBody
    Map<String, Object> getDangerousEvents(@RequestParam Map<Object, Object> reqMap, HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<>();
        int returnCode = 0;
        String returnMessage = "";

        // 取出reqMap中的有效 K V
        DangerousEventFilter dangerousEventFilter = new DangerousEventFilter();
        for (Object obj : reqMap.keySet()) {
            if (obj.toString().equals("server")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    dangerousEventFilter.setServer(value.toString());
                }
            }

            if (obj.toString().equals("message")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    dangerousEventFilter.setMessage(value.toString());
                }
            }

            if (obj.toString().equals("type")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    dangerousEventFilter.setType_list(Arrays.asList(value.toString().split(",")));
                }
            }

            if (obj.toString().equals("is_resolved")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    dangerousEventFilter.setIs_resolved(value.toString().equals("true"));
                }
            }

            if (obj.toString().equals("start_date")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    dangerousEventFilter.setDate_start(value.toString());
                }
            }

            if (obj.toString().equals("end_date")){
                Object value = reqMap.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    dangerousEventFilter.setDate_end(value.toString());
                }
            }
        }
        List dangerousEvents = redisLogService.getDangerousEventsByFilter(dangerousEventFilter);

        resultMap.put("total", redisLogService.getDangerousEventsCountByFilter(dangerousEventFilter));
        resultMap.put("rows", dangerousEvents);
        resultMap.put("returnCode", returnCode);
        resultMap.put("returnMessage", returnMessage);
        return resultMap;
    }

    @RequestMapping(value = "/systemSetting")
    public String systemSetting(@RequestParam Map<String, String> reqMap, HttpServletRequest request, ModelMap modelMap){
        String noticeType;
        String CPUWarningValue;
        String MemoryWarningValue;
        String[] keyPatterns;
        StringBuilder keyPatternsString = new StringBuilder();
        if (request.getMethod().toLowerCase().equals("post")) {
            Common.stopSubscriber();
            noticeType = reqMap.get("notice_type");
            RedisCommon.saveString("notice_type", noticeType);
            keyPatterns = reqMap.get("key_patterns").split("\n");
            RedisCommon.deleteRedisByKey("key_patterns");
            for (String keyPattern : keyPatterns) {
                RedisCommon.saveRedisList("key_patterns", keyPattern);
            }
            Common.startSubscriber();

            CPUWarningValue = reqMap.get("CPUWarningValue");
            RedisCommon.saveString("CPUWarningValue", StringUtils.isEmpty(CPUWarningValue)?"" :CPUWarningValue);
            MemoryWarningValue = reqMap.get("MemoryWarningValue");
            RedisCommon.saveString("MemoryWarningValue", StringUtils.isEmpty(MemoryWarningValue)?"" :MemoryWarningValue);
        }else {
            noticeType = RedisCommon.getStringByKey("notice_type");
            List<String> keyPatternsList = RedisCommon.getListFromRedis("key_patterns");
            keyPatterns = new String[keyPatternsList.size()];
            CPUWarningValue =  RedisCommon.getStringByKey("CPUWarningValue");
            MemoryWarningValue =  RedisCommon.getStringByKey("MemoryWarningValue");
        }

        for (String keyPattern:keyPatterns){
            keyPatternsString.append(keyPattern);
        }
        modelMap.addAttribute("notice_type", noticeType);
        modelMap.addAttribute("key_patterns", keyPatternsString);
        modelMap.addAttribute("CPUWarningValue", CPUWarningValue);
        modelMap.addAttribute("MemoryWarningValue", MemoryWarningValue);
        return "system_setting";
    }
}
