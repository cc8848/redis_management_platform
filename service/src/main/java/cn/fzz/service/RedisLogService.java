package cn.fzz.service;

import cn.fzz.bean.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/18.
 * Desc:
 */
public interface RedisLogService {
    public int saveCPU (RedisInfoCPU redisInfoCPU);
    public int saveMemory(RedisInfoMemory redisInfoMemory);
    public int saveClients(RedisInfoClients redisInfoClients);
    public int saveDangerousEvent(RedisDangerousEvent redisDangerousEvent);
    public int saveSubscriberEvent(RedisSubscriberEvent redisExpireEvent);
    public int solveSubscriberEvent(String event_id);
    public int solveDangerousEvent(String event_id);
    public List<RedisSubscriberEvent> getSubscriberEventsByMap(Map map);
    public List<RedisDangerousEvent> getDangerousEventsByMap(Map map);
    public List<RedisSubscriberEvent> getSubscriberEvents();
    public List<RedisInfoMemory> getSevenMemory();
    public List<RedisInfoMemory> getSevenMemoryByName(String taskName);
    public List<RedisInfoCPU> getSevenCPU();
    public List<RedisInfoCPU> getSevenCPUByName(String taskName);
    public RedisInfoCPU getRedisCPUByDate(String taskName, Date date);
    public RedisInfoCPU getRedisCPUByDate1(String taskName, Date date1, Date date2);
    public RedisInfoClients getRedisClientsByDate1(String taskName, Date date1, Date date2);
    public RedisInfoMemory getRedisMemoryByDate1(String taskName, Date date1, Date date2);
    public List<RedisInfoClients> getSevenClientsByName(String taskName);
}
