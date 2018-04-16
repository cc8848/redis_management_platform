package cn.fzz.dao.mapper;

import cn.fzz.bean.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Insert("INSERT INTO redis_expire_event(event_name, server, data_type, key, value, create_time, " +
            "resolving_time, is_resolved) VALUES(#{event_name}, #{server}, #{data_type}, #{key}, #{value}, " +
            "#{create_time}, #{resolving_time}, #{is_resolved})")
    public int saveExpireEvent(RedisExpireEvent redisExpireEvent);

    @Select("select * from redis_expire_event order by create_time desc;")
    public List<RedisExpireEvent> getExpireEvents();

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

    @Select("select * from redis_cpu where task_name = #{0} and date >= #{1} order by date limit 0, 1;")
    public RedisInfoCPU getRedisCPUByDate(String taskName, Date date);

    @Select("select * from redis_cpu where task_name = #{0} and date >= #{1} and date < #{2} order by date limit 0, 1;")
    public RedisInfoCPU getRedisCPUByDate1(String taskName, Date date1, Date date2);

    @Select("select * from redis_clients where task_name = #{0} and date >= #{1} and date < #{2} order by date limit 0, 1;")
    public RedisInfoClients getRedisClientsByDate1(String taskName, Date date1, Date date2);

    @Select("select * from redis_memory where task_name = #{0} and date >= #{1} and date < #{2} order by date limit 0, 1;")
    public RedisInfoMemory getRedisMemoryByDate1(String taskName, Date date1, Date date2);
}
