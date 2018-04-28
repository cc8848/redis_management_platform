package cn.fzz.service.impl;

import cn.fzz.bean.*;
import cn.fzz.bean.filter.DangerousEventFilter;
import cn.fzz.bean.filter.SubscriberEventFilter;
import cn.fzz.dao.mapper.RedisMapper;
import cn.fzz.service.RedisLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public int saveDangerousEvent(RedisDangerousEvent redisDangerousEvent) {
        return redisMapper.saveDangerousEvent(redisDangerousEvent);
    }

    @Override
    public int saveSubscriberEvent(RedisSubscriberEvent redisExpireEvent){
        return redisMapper.saveSubscriberEvent(redisExpireEvent);
    }

    @Override
    public int solveSubscriberEvent(String event_id){
        return redisMapper.solveSubscriberEvent(event_id);
    }

    @Override
    public int solveDangerousEvent(String event_id){
        return redisMapper.solveDangerousEvent(event_id);
    }

    @Override
    public List<RedisSubscriberEvent> getSubscriberEvents(){
        return redisMapper.getSubscriberEvents();
    }

    @Override
    public List<RedisSubscriberEvent> getSubscriberEventsByMap(Map map){
        return redisMapper.getSubscriberEventsByMap(map);
    }

    @Override
    public List<RedisDangerousEvent> getDangerousEventsByMap(Map map){
        return redisMapper.getDangerousEventsByMap(map);
    }

    @Override
    public List<SubscriberEventFilter> getSubscriberEventsByFilter(SubscriberEventFilter subscriberEventFilter) {
        return redisMapper.getSubscriberEventsByFilter(subscriberEventFilter);
    }

    @Override
    public int getSubscriberEventsCountByFilter(SubscriberEventFilter subscriberEventFilter) {
        return redisMapper.getSubscriberEventsCountByFilter(subscriberEventFilter);
    }

    @Override
    public List<RedisDangerousEvent> getDangerousEventsByFilter(DangerousEventFilter dangerousEventFilter){
        return redisMapper.getDangerousEventsByFilter(dangerousEventFilter);
    }

    @Override
    public int getDangerousEventsCountByFilter(DangerousEventFilter dangerousEventFilter){
        return redisMapper.getDangerousEventsCountByFilter(dangerousEventFilter);
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
