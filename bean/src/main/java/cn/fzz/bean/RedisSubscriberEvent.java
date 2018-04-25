package cn.fzz.bean;

import java.util.Date;

public class RedisSubscriberEvent {
    private String event_id;
    private String event_name;
    private String server;
    private String event_type;
    private String notice_type;
    private String key_pattern;
    private String pattern;
    private String channel;
    private String data_type;
    private String redis_key;
    private String redis_value;
    private Date create_time;
    private Date resolving_time;
    private Boolean is_resolved = false;

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

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

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getNotice_type() {
        return notice_type;
    }

    public void setNotice_type(String notice_type) {
        this.notice_type = notice_type;
    }

    public String getKey_pattern() {
        return key_pattern;
    }

    public void setKey_pattern(String key_pattern) {
        this.key_pattern = key_pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channe) {
        this.channel = channe;
    }
    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getRedis_key() {
        return redis_key;
    }

    public void setRedis_key(String redis_key) {
        this.redis_key = redis_key;
    }

    public String getRedis_value() {
        return redis_value;
    }

    public void setRedis_value(String redis_value) {
        this.redis_value = redis_value;
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
