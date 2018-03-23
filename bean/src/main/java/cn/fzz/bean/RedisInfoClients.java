package cn.fzz.bean;

/**
 * Created by Administrator on 2018/3/21.
 * Desc:
 */
public class RedisInfoClients {
    private String task_name;
    private int connected_clients;
    private int blocked_clients;
    private int client_longest_input_buf;
    private int client_longest_output_list;

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public int getConnected_clients() {
        return connected_clients;
    }

    public void setConnected_clients(int connected_clients) {
        this.connected_clients = connected_clients;
    }

    public int getBlocked_clients() {
        return blocked_clients;
    }

    public void setBlocked_clients(int blocked_clients) {
        this.blocked_clients = blocked_clients;
    }

    public int getClient_longest_input_buf() {
        return client_longest_input_buf;
    }

    public void setClient_longest_input_buf(int client_longest_input_buf) {
        this.client_longest_input_buf = client_longest_input_buf;
    }

    public int getClient_longest_output_list() {
        return client_longest_output_list;
    }

    public void setClient_longest_output_list(int client_longest_output_list) {
        this.client_longest_output_list = client_longest_output_list;
    }
}
