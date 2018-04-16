package cn.fzz.bean;

import java.util.Date;

public class RedisExpireEvent {
    private String event_name;
    private String server;
    private String data_type;
    private String key;
    private String value;
    private Date create_time;
    private Date resolving_time;
    private Boolean is_resolved;

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getResolving_time() {
        return resolving_time;
    }

    public void setResolving_time(Date resolving_time) {
        this.resolving_time = resolving_time;
    }

    public Boolean getIs_resolved() {
        return is_resolved;
    }

    public void setIs_resolved(Boolean is_resolved) {
        this.is_resolved = is_resolved;
    }
}
