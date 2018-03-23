package cn.fzz.dao.mapper;

import cn.fzz.bean.RedisDangerousEvent;
import cn.fzz.bean.RedisInfoCPU;
import cn.fzz.bean.RedisInfoClients;
import cn.fzz.bean.RedisInfoMemory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

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
            "used_memory_human, used_memory_rss_human, used_memory_peak_human, used_memory_lua_human, " +
            "mem_fragmentation_ratio, mem_allocator) " +
            "VALUES(#{task_name}, #{used_memory}, #{used_memory_rss}, #{used_memory_peak}, #{used_memory_lua}, " +
            "#{used_memory_human}, #{used_memory_rss_human}, #{used_memory_peak_human}, #{used_memory_lua_human}, " +
            "#{mem_fragmentation_ratio}, #{mem_allocator})")
    public int saveMemory(RedisInfoMemory redisInfoMemory);

    @Insert("INSERT INTO redis_clients(task_name, connected_clients, blocked_clients, client_longest_input_buf, " +
            "client_longest_output_list) " +
            "VALUES(#{task_name}, #{connected_clients}, #{blocked_clients}, #{client_longest_input_buf}, " +
            "#{client_longest_output_list})")
    public int saveClients(RedisInfoClients redisInfoClients);

    @Insert("INSERT INTO redis_dangerous_event(event_name, type, message) VALUES(#{event_name}, #{type}, #{message})")
    public int saveEvent(RedisDangerousEvent redisDangerousEvent);

    @Select("select * from redis_memory order by date desc limit 0, 7;")
    public List<RedisInfoMemory> getSevenMemory();

    @Select("select * from redis_cpu order by date desc limit 0, 7;")
    public List<RedisInfoCPU> getSevenCPU();

    @Select("select * from redis_memory where task_name = #{taskName} order by date desc limit 0, 7;")
    public List<RedisInfoMemory> getSevenMemoryByName(String taskName);

    @Select("select * from redis_cpu where task_name = #{taskName} order by date desc limit 0, 7;")
    public List<RedisInfoCPU> getSevenCPUByName(String taskName);

    @Select("select * from redis_clients where task_name = #{taskName} order by date desc limit 0, 7;")
    public List<RedisInfoClients> getSevenClientsByName(String taskName);
}
