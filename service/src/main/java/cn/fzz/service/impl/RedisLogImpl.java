package cn.fzz.service.impl;

import cn.fzz.bean.RedisInfoCPU;
import cn.fzz.bean.RedisInfoMemory;
import cn.fzz.dao.mapper.RedisMapper;
import cn.fzz.service.RedisLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/3/18.
 * Desc:
 */
@Component
@ComponentScan(basePackages = {"cn.fzz.dao"})
public class RedisLogImpl implements RedisLogService{
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
}
