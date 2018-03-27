package cn.fzz.bean;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/18.
 * Desc:
 */
public class RedisInfoMemory {
    private String task_name;
    private int used_memory;
    private int used_memory_rss;
    private int used_memory_peak;
    private int used_memory_lua;
    private float used_memory_human;
    private float used_memory_rss_human;
    private float used_memory_peak_human;
    private float used_memory_lua_human;
    private float mem_fragmentation_ratio;
    private String mem_allocator;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RedisInfoMemory() {
    }

    public RedisInfoMemory(String task_name, int used_memory, int used_memory_rss, int used_memory_peak, int used_memory_lua,
                           float used_memory_human, float used_memory_peak_human, float used_memory_lua_human,
                           float mem_fragmentation_ratio, String mem_allocator) {
        this.task_name = task_name;
        this.used_memory = used_memory;
        this.used_memory_rss = used_memory_rss;
        this.used_memory_peak = used_memory_peak;
        this.used_memory_lua = used_memory_lua;
        this.used_memory_human = used_memory_human;
        this.used_memory_peak_human = used_memory_peak_human;
        this.used_memory_lua_human = used_memory_lua_human;
        this.mem_fragmentation_ratio = mem_fragmentation_ratio;
        this.mem_allocator = mem_allocator;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public int getUsed_memory() {
        return used_memory;
    }

    public void setUsed_memory(int used_memory) {
        this.used_memory = used_memory;
    }

    public int getUsed_memory_rss() {
        return used_memory_rss;
    }

    public void setUsed_memory_rss(int used_memory_rss) {
        this.used_memory_rss = used_memory_rss;
    }

    public int getUsed_memory_peak() {
        return used_memory_peak;
    }

    public void setUsed_memory_peak(int used_memory_peak) {
        this.used_memory_peak = used_memory_peak;
    }

    public int getUsed_memory_lua() {
        return used_memory_lua;
    }

    public void setUsed_memory_lua(int used_memory_lua) {
        this.used_memory_lua = used_memory_lua;
    }

    public float getUsed_memory_human() {
        return used_memory_human;
    }

    public void setUsed_memory_human(float used_memory_human) {
        this.used_memory_human = used_memory_human;
    }

    public float getUsed_memory_rss_human() {
        return used_memory_rss_human;
    }

    public void setUsed_memory_rss_human(float used_memory_rss_human) {
        this.used_memory_rss_human = used_memory_rss_human;
    }

    public float getUsed_memory_peak_human() {
        return used_memory_peak_human;
    }

    public void setUsed_memory_peak_human(float used_memory_peak_human) {
        this.used_memory_peak_human = used_memory_peak_human;
    }

    public float getUsed_memory_lua_human() {
        return used_memory_lua_human;
    }

    public void setUsed_memory_lua_human(float used_memory_lua_human) {
        this.used_memory_lua_human = used_memory_lua_human;
    }

    public float getMem_fragmentation_ratio() {
        return mem_fragmentation_ratio;
    }

    public void setMem_fragmentation_ratio(float mem_fragmentation_ratio) {
        this.mem_fragmentation_ratio = mem_fragmentation_ratio;
    }

    public String getMem_allocator() {
        return mem_allocator;
    }

    public void setMem_allocator(String mem_allocator) {
        this.mem_allocator = mem_allocator;
    }
}
