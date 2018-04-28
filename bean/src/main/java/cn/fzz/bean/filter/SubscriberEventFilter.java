package cn.fzz.bean.filter;

import java.util.List;

public class SubscriberEventFilter {
    private String id;
    private String event_name;
    private String server;
    private String event_type;
    private List<String> event_type_list;
    private String notice_type;
    private List<String> notice_type_list;
    private String key_pattern;
    private String pattern;
    private String channel;
    private String data_type;
    private String redis_key;
    private String redis_value;
    private String create_time_start;
    private String create_time_end;
    private String resolving_time_start;
    private String resolving_time_end;
    private Boolean is_resolved;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getEvent_type_list() {
        return event_type_list;
    }

    public void setEvent_type_list(List<String> event_type_list) {
        this.event_type_list = event_type_list;
    }

    public String getNotice_type() {
        return notice_type;
    }

    public void setNotice_type(String notice_type) {
        this.notice_type = notice_type;
    }

    public List<String> getNotice_type_list() {
        return notice_type_list;
    }

    public void setNotice_type_list(List<String> notice_type_list) {
        this.notice_type_list = notice_type_list;
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

    public void setChannel(String channel) {
        this.channel = channel;
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

    public String getCreate_time_start() {
        return create_time_start;
    }

    public void setCreate_time_start(String create_time_start) {
        this.create_time_start = create_time_start;
    }

    public String getCreate_time_end() {
        return create_time_end;
    }

    public void setCreate_time_end(String create_time_end) {
        this.create_time_end = create_time_end;
    }

    public String getResolving_time_start() {
        return resolving_time_start;
    }

    public void setResolving_time_start(String resolving_time_start) {
        this.resolving_time_start = resolving_time_start;
    }

    public String getResolving_time_end() {
        return resolving_time_end;
    }

    public void setResolving_time_end(String resolving_time_end) {
        this.resolving_time_end = resolving_time_end;
    }

    public Boolean getIs_resolved() {
        return is_resolved;
    }

    public void setIs_resolved(Boolean is_resolved) {
        this.is_resolved = is_resolved;
    }
}
