package cn.fzz.dao.mapper;

import cn.fzz.bean.RedisInfoCPU;
import cn.fzz.bean.RedisInfoMemory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/3/18.
 * Desc:
 */
@Mapper
@Component
public interface RedisMapper {
    @Insert("INSERT INTO redis_cpu(task_name, used_cpu_sys, used_cpu_user, used_cpu_sys_children, used_cpu_user_children) " +
            "VALUES(#{task_name}, #{used_cpu_sys}, #{used_cpu_user}, #{used_cpu_sys_children}, #{used_cpu_user_children})")
    public int saveCPU(RedisInfoCPU redisInfoCPU);
    @Insert("INSERT INTO redis_memory(task_name, used_memory, used_memory_rss, used_memory_peak, used_memory_lua, " +
            "used_memory_human, used_memory_peak_human, used_memory_lua_human, mem_fragmentation_ratio, " +
            "mem_allocator) " +
            "VALUES(#{task_name}, #{used_memory}, #{used_memory_rss}, #{used_memory_peak}, #{used_memory_lua}, " +
            "#{used_memory_human}, #{used_memory_peak_human}, #{used_memory_lua_human}, #{mem_fragmentation_ratio}, " +
            "#{mem_allocator})")
    public int saveMemory(RedisInfoMemory redisInfoMemory);
}
