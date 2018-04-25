package cn.fzz.bean;

import java.util.Date;

/**
 * Created by Administrator on 2018/3/22.
 * Desc:
 */
public class RedisDangerousEvent {
    private String event_id;
    private String server;
    private String event_name;
    private String type;
    private String message;
    private Boolean is_resolved = false;
    private Date resolving_time;
    private Date date;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public Boolean getIs_resolved() {
        return is_resolved;
    }

    public void setIs_resolved(Boolean is_resolved) {
        this.is_resolved = is_resolved;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Date getResolving_time() {
        return resolving_time;
    }

    public void setResolving_time(Date resolving_time) {
        this.resolving_time = resolving_time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
