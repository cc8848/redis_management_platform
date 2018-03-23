package cn.fzz.service;

import cn.fzz.bean.RedisDangerousEvent;
import cn.fzz.bean.RedisInfoCPU;
import cn.fzz.bean.RedisInfoClients;
import cn.fzz.bean.RedisInfoMemory;

import java.util.List;

/**
 * Created by Administrator on 2018/3/18.
 * Desc:
 */
public interface RedisLogService {
    public int saveCPU (RedisInfoCPU redisInfoCPU);
    public int saveMemory(RedisInfoMemory redisInfoMemory);
    public int saveClients(RedisInfoClients redisInfoClients);
    public int saveEvent(RedisDangerousEvent redisDangerousEvent);
    public List<RedisInfoMemory> getSevenMemory();
    public List<RedisInfoMemory> getSevenMemoryByName(String taskName);
    public List<RedisInfoCPU> getSevenCPU();
    public List<RedisInfoCPU> getSevenCPUByName(String taskName);
    public List<RedisInfoClients> getSevenClientsByName(String taskName);
}
