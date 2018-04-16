package cn.fzz.service.impl;

import cn.fzz.bean.*;
import cn.fzz.dao.mapper.RedisMapper;
import cn.fzz.service.RedisLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/18.
 * Desc:
 */
@Component
@ComponentScan(basePackages = {"cn.fzz.dao"})
public class RedisLogImpl implements RedisLogService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisMapper redisMapper;

    @Override
    public int saveCPU(RedisInfoCPU redisInfoCPU) {
        return redisMapper.saveCPU(redisInfoCPU);
    }

    @Override
    public int saveMemory(RedisInfoMemory redisInfoMemory) {
        return redisMapper.saveMemory(redisInfoMemory);
    }

    @Override
    public int saveClients(RedisInfoClients redisInfoClients) {
        return redisMapper.saveClients(redisInfoClients);
    }

    @Override
    public int saveEvent(RedisDangerousEvent redisDangerousEvent) {
        return redisMapper.saveEvent(redisDangerousEvent);
    }

    @Override
    public int saveExpireEvent(RedisExpireEvent redisExpireEvent){
        return redisMapper.saveExpireEvent(redisExpireEvent);
    }

    @Override
    public List<RedisExpireEvent> getExpireEvents(){
        return redisMapper.getExpireEvents();
    }

    @Override
    public List<RedisInfoMemory> getSevenMemory() {
        return redisMapper.getSevenMemory();
    }

    @Override
    public List<RedisInfoMemory> getSevenMemoryByName(String taskName) {
        return redisMapper.getSevenMemoryByName(taskName);
    }

    @Override
    public List<RedisInfoCPU> getSevenCPU() {
        return redisMapper.getSevenCPU();
    }

    @Override
    public List<RedisInfoCPU> getSevenCPUByName(String taskName) {
        return redisMapper.getSevenCPUByName(taskName);
    }

    @Override
    public RedisInfoCPU getRedisCPUByDate(String taskName, Date date){
        return redisMapper.getRedisCPUByDate(taskName, date);
    }

    @Override
    public RedisInfoClients getRedisClientsByDate1(String taskName, Date date1, Date date2){
        return redisMapper.getRedisClientsByDate1(taskName, date1, date2);
    }

    @Override
    public RedisInfoMemory getRedisMemoryByDate1(String taskName, Date date1, Date date2){
        return redisMapper.getRedisMemoryByDate1(taskName, date1, date2);
    }

    @Override
    public RedisInfoCPU getRedisCPUByDate1(String taskName, Date date1, Date date2){
        return redisMapper.getRedisCPUByDate1(taskName, date1, date2);
    }

    @Override
    public List<RedisInfoClients> getSevenClientsByName(String taskName) {
        return redisMapper.getSevenClientsByName(taskName);
    }
}
