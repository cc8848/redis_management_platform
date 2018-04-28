package cn.fzz.bean.filter;

import java.util.Date;
import java.util.List;

public class DangerousEventFilter {
    private String id;
    private String server;
    private String event_name;
    private String type;
    private List<String> type_list;
    private String message;
    private Boolean is_resolved;
    private String resolving_time_start;
    private String resolving_time_end;
    private String date_start;
    private String date_end;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
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

    public List<String> getType_list() {
        return type_list;
    }

    public void setType_list(List<String> type_list) {
        this.type_list = type_list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIs_resolved() {
        return is_resolved;
    }

    public void setIs_resolved(Boolean is_resolved) {
        this.is_resolved = is_resolved;
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

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }
}
