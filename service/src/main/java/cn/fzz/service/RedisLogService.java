package cn.fzz.service;

import cn.fzz.bean.RedisInfoCPU;
import cn.fzz.bean.RedisInfoMemory;
import cn.fzz.bean.UserBean;

/**
 * Created by Administrator on 2018/3/18.
 * Desc:
 */
public interface RedisLogService {
    public int saveCPU (RedisInfoCPU redisInfoCPU);
    public int saveMemory(RedisInfoMemory redisInfoMemory);
}
