package cn.fzz.bean;

/**
 * Created by fanzezhen on 2017/12/19.
 * Desc:
 */
public class RedisConfigBean {
    private String bind;
    private String protected_mode;
    private String loglevel;
    private String logfile;
    private String stop_writes_on_bgsave_error;
    private String rdbcompression;
    private String dbfilename;
    private String dir;
    private String slave_serve_stale_data;
    private String slave_read_only;
    private String repl_diskless_sync;
    private String repl_disable_tcp_nodelay;
    private String appendonly;
    private String appendfilename;
    private String appendfsync;
    private String auto_aof_rewrite_min_size;
    private String aof_load_truncated;
    private String notify_keyspace_events;
    private String activerehashing;
    private String aof_rewrite_incremental_fsync;
    private String softwarePath;
    private String configPath;
    private String[][] saveArr;
    private String[][] client_output_buffer_limitArr;
    private Integer port;
    private Integer tcp_backlog;
    private Integer timeout;
    private Integer tcp_keepalive;
    private Integer databases;
    private Integer repl_diskless_sync_delay;
    private Integer slave_priority;
    private Integer auto_aof_rewrite_percentage;
    private Integer lua_time_limit;
    private Integer slowlog_log_slower_than;
    private Integer slowlog_max_len;
    private Integer latency_monitor_threshold;
    private Integer hash_max_ziplist_entries;
    private Integer hash_max_ziplist_value;
    private Integer list_max_ziplist_size;
    private Integer list_compress_depth;
    private Integer zset_max_ziplist_entries;
    private Integer zset_max_ziplist_value;
    private Integer hll_sparse_max_bytes;
    private Integer hz;

    public RedisConfigBean() {
        port = 6379;
        bind = "127.0.0.1";
        protected_mode = "yes";
        tcp_backlog = 511;
        timeout = 0;
        tcp_keepalive = 0;
        loglevel = "notice";
        logfile = "";
        databases = 16;
        saveArr = new String[][]{{"save", "900", "1"}, {"save", "300", "10"}, {"save", "60", "10000"}};
        client_output_buffer_limitArr = new String[][]{{"client-output-buffer-limit", "normal", "0", "0", "0"},
                {"client-output-buffer-limit", "slave", "256mb", "64mb", "60"},
                {"client-output-buffer-limit", "pubsub", "32mb", "8mb", "60"}};
        stop_writes_on_bgsave_error = "yes";
        rdbcompression = "yes";
        dbfilename = "dump.rdb";
        dir  = "./";
        slave_serve_stale_data = "yes";
        slave_read_only = "yes";
        repl_diskless_sync = "no";
        repl_diskless_sync_delay = 5;
        slave_priority = 100;
        repl_disable_tcp_nodelay = "no";
        appendonly = "no";
        appendfilename = "appendonly.aof";
        appendfsync = "everysec";
        auto_aof_rewrite_percentage  = 100;
        auto_aof_rewrite_min_size = "64mb";
        aof_load_truncated = "yes";
        lua_time_limit = 5000;
        slowlog_log_slower_than = 10000;
        slowlog_max_len = 128;
        latency_monitor_threshold = 0;
        notify_keyspace_events = "";
        hash_max_ziplist_entries = 512;
        hash_max_ziplist_value = 64;
        list_max_ziplist_size = -2;
        list_compress_depth = 0;
        zset_max_ziplist_entries = 128;
        zset_max_ziplist_value = 64;
        hll_sparse_max_bytes = 3000;
        activerehashing = "yes";
        hz = 10;
        aof_rewrite_incremental_fsync = "yes";
    }

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public String getProtected_mode() {
        return protected_mode;
    }

    public void setProtected_mode(String protected_mode) {
        this.protected_mode = protected_mode;
    }

    public String getLoglevel() {
        return loglevel;
    }

    public void setLoglevel(String loglevel) {
        this.loglevel = loglevel;
    }

    public String getLogfile() {
        return logfile;
    }

    public void setLogfile(String logfile) {
        this.logfile = logfile;
    }

    public String getStop_writes_on_bgsave_error() {
        return stop_writes_on_bgsave_error;
    }

    public void setStop_writes_on_bgsave_error(String stop_writes_on_bgsave_error) {
        this.stop_writes_on_bgsave_error = stop_writes_on_bgsave_error;
    }

    public String getRdbcompression() {
        return rdbcompression;
    }

    public void setRdbcompression(String rdbcompression) {
        this.rdbcompression = rdbcompression;
    }

    public String getDbfilename() {
        return dbfilename;
    }

    public void setDbfilename(String dbfilename) {
        this.dbfilename = dbfilename;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSlave_serve_stale_data() {
        return slave_serve_stale_data;
    }

    public void setSlave_serve_stale_data(String slave_serve_stale_data) {
        this.slave_serve_stale_data = slave_serve_stale_data;
    }

    public String getSlave_read_only() {
        return slave_read_only;
    }

    public void setSlave_read_only(String slave_read_only) {
        this.slave_read_only = slave_read_only;
    }

    public String getRepl_diskless_sync() {
        return repl_diskless_sync;
    }

    public void setRepl_diskless_sync(String repl_diskless_sync) {
        this.repl_diskless_sync = repl_diskless_sync;
    }

    public String getRepl_disable_tcp_nodelay() {
        return repl_disable_tcp_nodelay;
    }

    public void setRepl_disable_tcp_nodelay(String repl_disable_tcp_nodelay) {
        this.repl_disable_tcp_nodelay = repl_disable_tcp_nodelay;
    }

    public String getAppendonly() {
        return appendonly;
    }

    public void setAppendonly(String appendonly) {
        this.appendonly = appendonly;
    }

    public String getAppendfilename() {
        return appendfilename;
    }

    public void setAppendfilename(String appendfilename) {
        this.appendfilename = appendfilename;
    }

    public String getAppendfsync() {
        return appendfsync;
    }

    public void setAppendfsync(String appendfsync) {
        this.appendfsync = appendfsync;
    }

    public String getAuto_aof_rewrite_min_size() {
        return auto_aof_rewrite_min_size;
    }

    public void setAuto_aof_rewrite_min_size(String auto_aof_rewrite_min_size) {
        this.auto_aof_rewrite_min_size = auto_aof_rewrite_min_size;
    }

    public String getAof_load_truncated() {
        return aof_load_truncated;
    }

    public void setAof_load_truncated(String aof_load_truncated) {
        this.aof_load_truncated = aof_load_truncated;
    }

    public String getNotify_keyspace_events() {
        return notify_keyspace_events;
    }

    public void setNotify_keyspace_events(String notify_keyspace_events) {
        this.notify_keyspace_events = notify_keyspace_events;
    }

    public String getActiverehashing() {
        return activerehashing;
    }

    public void setActiverehashing(String activerehashing) {
        this.activerehashing = activerehashing;
    }

    public String getAof_rewrite_incremental_fsync() {
        return aof_rewrite_incremental_fsync;
    }

    public void setAof_rewrite_incremental_fsync(String aof_rewrite_incremental_fsync) {
        this.aof_rewrite_incremental_fsync = aof_rewrite_incremental_fsync;
    }

    public String getSoftwarePath() {
        return softwarePath;
    }

    public void setSoftwarePath(String softwarePath) {
        this.softwarePath = softwarePath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String[][] getSaveArr() {
        return saveArr;
    }

    public void setSaveArr(String[][] saveArr) {
        this.saveArr = saveArr;
    }

    public void setSaveArr(String str){
        String[][] saveArr = new String[3][3];
        String[] rows = str.split(",");
        for (int i = 0; i < saveArr.length; i++){
            saveArr[i] = rows[i].split(" ");
        }
        this.saveArr = saveArr;
    }

    public String[][] getClient_output_buffer_limitArr() {
        return client_output_buffer_limitArr;
    }

    public void setClient_output_buffer_limitArr(String[][] client_output_buffer_limitArr) {
        this.client_output_buffer_limitArr = client_output_buffer_limitArr;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTcp_backlog() {
        return tcp_backlog;
    }

    public void setTcp_backlog(Integer tcp_backlog) {
        this.tcp_backlog = tcp_backlog;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getTcp_keepalive() {
        return tcp_keepalive;
    }

    public void setTcp_keepalive(Integer tcp_keepalive) {
        this.tcp_keepalive = tcp_keepalive;
    }

    public Integer getDatabases() {
        return databases;
    }

    public void setDatabases(Integer databases) {
        this.databases = databases;
    }

    public Integer getRepl_diskless_sync_delay() {
        return repl_diskless_sync_delay;
    }

    public void setRepl_diskless_sync_delay(Integer repl_diskless_sync_delay) {
        this.repl_diskless_sync_delay = repl_diskless_sync_delay;
    }

    public Integer getSlave_priority() {
        return slave_priority;
    }

    public void setSlave_priority(Integer slave_priority) {
        this.slave_priority = slave_priority;
    }

    public Integer getAuto_aof_rewrite_percentage() {
        return auto_aof_rewrite_percentage;
    }

    public void setAuto_aof_rewrite_percentage(Integer auto_aof_rewrite_percentage) {
        this.auto_aof_rewrite_percentage = auto_aof_rewrite_percentage;
    }

    public Integer getLua_time_limit() {
        return lua_time_limit;
    }

    public void setLua_time_limit(Integer lua_time_limit) {
        this.lua_time_limit = lua_time_limit;
    }

    public Integer getSlowlog_log_slower_than() {
        return slowlog_log_slower_than;
    }

    public void setSlowlog_log_slower_than(Integer slowlog_log_slower_than) {
        this.slowlog_log_slower_than = slowlog_log_slower_than;
    }

    public Integer getSlowlog_max_len() {
        return slowlog_max_len;
    }

    public void setSlowlog_max_len(Integer slowlog_max_len) {
        this.slowlog_max_len = slowlog_max_len;
    }

    public Integer getLatency_monitor_threshold() {
        return latency_monitor_threshold;
    }

    public void setLatency_monitor_threshold(Integer latency_monitor_threshold) {
        this.latency_monitor_threshold = latency_monitor_threshold;
    }

    public Integer getHash_max_ziplist_entries() {
        return hash_max_ziplist_entries;
    }

    public void setHash_max_ziplist_entries(Integer hash_max_ziplist_entries) {
        this.hash_max_ziplist_entries = hash_max_ziplist_entries;
    }

    public Integer getHash_max_ziplist_value() {
        return hash_max_ziplist_value;
    }

    public void setHash_max_ziplist_value(Integer hash_max_ziplist_value) {
        this.hash_max_ziplist_value = hash_max_ziplist_value;
    }

    public Integer getList_max_ziplist_size() {
        return list_max_ziplist_size;
    }

    public void setList_max_ziplist_size(Integer list_max_ziplist_size) {
        this.list_max_ziplist_size = list_max_ziplist_size;
    }

    public Integer getList_compress_depth() {
        return list_compress_depth;
    }

    public void setList_compress_depth(Integer list_compress_depth) {
        this.list_compress_depth = list_compress_depth;
    }

    public Integer getZset_max_ziplist_entries() {
        return zset_max_ziplist_entries;
    }

    public void setZset_max_ziplist_entries(Integer zset_max_ziplist_entries) {
        this.zset_max_ziplist_entries = zset_max_ziplist_entries;
    }

    public Integer getZset_max_ziplist_value() {
        return zset_max_ziplist_value;
    }

    public void setZset_max_ziplist_value(Integer zset_max_ziplist_value) {
        this.zset_max_ziplist_value = zset_max_ziplist_value;
    }

    public Integer getHll_sparse_max_bytes() {
        return hll_sparse_max_bytes;
    }

    public void setHll_sparse_max_bytes(Integer hll_sparse_max_bytes) {
        this.hll_sparse_max_bytes = hll_sparse_max_bytes;
    }

    public Integer getHz() {
        return hz;
    }

    public void setHz(Integer hz) {
        this.hz = hz;
    }
}
