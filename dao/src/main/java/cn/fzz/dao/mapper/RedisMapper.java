package cn.fzz.dao.mapper;

import cn.fzz.bean.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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

    @Insert("INSERT INTO redis_dangerous_event(event_name, server, is_resolved, type, message) " +
            "VALUES(#{event_name}, #{server}, #{is_resolved}, #{type}, #{message})")
    public int saveDangerousEvent(RedisDangerousEvent redisDangerousEvent);

    @Select("<script>select *, id as event_id from redis_dangerous_event where 1 = 1 " +
            "<if test=\"id !=null \"> and id = #{id} </if>" +
            "<if test=\"message !=null \"> and message like #{message} </if>" +
            "<if test=\"server !=null \"> and server like #{server} </if>" +
            "<if test=\"type !=null \"> and type in (#{type}) </if>" +
            "<if test=\"is_resolved !=null \"> and is_resolved = (#{is_resolved}) </if>" +
            "<if test=\"start_date !=null \"> and date <![CDATA[>]]> #{start_date} </if>" +
            "<if test=\"end_date !=null \"> and date <![CDATA[<]]> #{end_date} </if>" +
            "order by date desc;" +
            "</script>")
    public List<RedisDangerousEvent> getDangerousEventsByMap(Map map);

    @Insert("INSERT INTO redis_subscriber_event(event_name, server, event_type, notice_type, key_pattern, pattern, " +
            "channel, data_type, redis_key, redis_value, create_time, resolving_time, is_resolved) " +
            "VALUES(#{event_name}, #{server}, #{event_type}, #{notice_type}, #{key_pattern}, #{pattern}, #{channel}, " +
            "#{data_type}, #{redis_key}, #{redis_value}, #{create_time}, #{resolving_time}, #{is_resolved})")
    public int saveSubscriberEvent(RedisSubscriberEvent redisExpireEvent);

    @Update("update redis_subscriber_event set is_resolved = true where id = #{event_id};")
    public int solveSubscriberEvent(String event_id);

    @Update("update redis_dangerous_event set is_resolved = true where id = #{event_id};")
    public int solveDangerousEvent(String event_id);

    @Select("select *, id as event_id from redis_subscriber_event order by create_time desc;")
    public List<RedisSubscriberEvent> getSubscriberEvents();

    @Select("<script>select *, id as event_id from redis_subscriber_event where 1 = 1 " +
            "<if test=\"id !=null \"> and id = #{id} </if>" +
            "<if test=\"key !=null \"> and redis_key like #{key} </if>" +
            "<if test=\"server !=null \"> and server like #{server} </if>" +
            "<if test=\"event_type !=null \"> and event_type in (#{event_type}) </if>" +
            "<if test=\"notice_type !=null \"> and notice_type in (#{notice_type}) </if>" +
            "<if test=\"start_date !=null \"> and create_time <![CDATA[>]]> #{start_date} </if>" +
            "<if test=\"end_date !=null \"> and create_time <![CDATA[<]]> #{end_date} </if>" +
            "order by create_time desc;" +
            "</script>")
    public List<RedisSubscriberEvent> getSubscriberEventsByMap(Map map);

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
