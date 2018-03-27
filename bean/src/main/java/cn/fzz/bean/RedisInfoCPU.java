package cn.fzz.bean;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/18.
 * Desc:
 */
public class RedisInfoCPU {
    private String task_name;
    private float used_cpu_sys;
    private float used_cpu_user;
    private float used_cpu_sys_children;
    private float used_cpu_user_children;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RedisInfoCPU() {
    }

    public RedisInfoCPU(float used_cpu_sys, float used_cpu_user, float used_cpu_sys_children,
                        float used_cpu_user_children) {
        this.used_cpu_sys = used_cpu_sys;
        this.used_cpu_user = used_cpu_user;
        this.used_cpu_sys_children = used_cpu_sys_children;
        this.used_cpu_user_children = used_cpu_user_children;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public float getUsed_cpu_sys() {
        return used_cpu_sys;
    }

    public void setUsed_cpu_sys(float used_cpu_sys) {
        this.used_cpu_sys = used_cpu_sys;
    }

    public float getUsed_cpu_user() {
        return used_cpu_user;
    }

    public void setUsed_cpu_user(float used_cpu_user) {
        this.used_cpu_user = used_cpu_user;
    }

    public float getUsed_cpu_sys_children() {
        return used_cpu_sys_children;
    }

    public void setUsed_cpu_sys_children(float used_cpu_sys_children) {
        this.used_cpu_sys_children = used_cpu_sys_children;
    }

    public float getUsed_cpu_user_children() {
        return used_cpu_user_children;
    }

    public void setUsed_cpu_user_children(float used_cpu_user_children) {
        this.used_cpu_user_children = used_cpu_user_children;
    }
}
