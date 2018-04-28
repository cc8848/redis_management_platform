package cn.fzz.framework.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/3/9.
 * Desc:
 */

public class RedisConnection {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //Redis服务器IP
    private String addr = "127.0.0.1";

    //Redis的端口号
    private int port = 6379;

    //访问密码
//    private String auth = "admin";

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private int max_active = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private int max_idle = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private int max_wait = 100;

    private int timeout = 100;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private boolean test_on_borrow = true;

    private JedisPool jedisPool = null;

    public RedisConnection() {
        initJedisPool();
    }

    public RedisConnection(int port) {
        this.port = port;
        initJedisPool();
    }

    public RedisConnection(String addr, int port, int max_active, int max_idle, int max_wait, int timeout) {
        this.addr = addr;
        this.port = port;
        this.max_active = max_active;
        this.max_idle = max_idle;
        this.max_wait = max_wait;
        this.timeout = timeout;
        initJedisPool();
    }

    private void initJedisPool() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(max_active);
            config.setMaxIdle(max_idle);
            config.setMaxWaitMillis(max_wait);
            config.setTestOnBorrow(test_on_borrow);
//            jedisPool = new JedisPool(config, addr, port, timeout, auth);
            jedisPool = new JedisPool(config, addr, port, timeout);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    private synchronized Jedis getJedis() {
        try {
            return jedisPool != null ? jedisPool.getResource() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public String ping() {
        Jedis jedis = getJedis();
        if (jedis != null) {
            String ping = jedis.ping();
            jedis.close();
            return ping;
        }
        return "fail";
    }

    public Set<String> getKeys(){
        Jedis jedis = getJedis();
        if (jedis != null) {
            Set<String> keys = jedis.keys("*");
            jedis.close();
            return keys;
        }
        return null;
    }

    public String getStringByKey(String key){
        Jedis jedis = getJedis();
        if (jedis != null){
            String string = jedis.get(key);
            jedis.close();
            return string;
        }
        return null;
    }

    public String getHashFeildValue(String key, String childKey){
        Jedis jedis = getJedis();
        if (jedis != null){
            String value = jedis.hget(key, childKey);
            jedis.close();
            return value;
        }
        return null;
    }

    public String getTypeByKey(String key){
        Jedis jedis = getJedis();
        if (jedis != null) {
            String type = jedis.type(key);
            jedis.close();
            return type;
        }
        return null;
    }

    public String getEncodingByKey(String key){
        Jedis jedis = getJedis();
        if (jedis != null) {
            String encoding = jedis.objectEncoding(key);
            jedis.close();
            return encoding;
        }
        return null;
    }

    public Long getCountByKey(String key){
        Jedis jedis = getJedis();
        if (jedis != null) {
            Long refCount = jedis.objectRefcount(key);
            jedis.close();
            return refCount;
        }
        return null;
    }

    public Long getIdleTimeByKey(String key){
        Jedis jedis = getJedis();
        if (jedis != null) {
            Long idleTime = jedis.objectIdletime(key);
            jedis.close();
            return idleTime;
        }
        return null;
    }

    public Long getValidTimeByKey(String key){
        Jedis jedis = getJedis();
        if (jedis != null) {
            Long validTime = jedis.ttl(key);
            jedis.close();
            return validTime;
        }
        return null;
    }

    public String getInfo(){
        Jedis jedis = getJedis();
        if (jedis != null) {
            String info = jedis.info();
            jedis.close();
            return info;
        }
        return null;
    }

    public Boolean checkByport(int port) {
        port = port;
        Jedis jedis = getJedis();
        if (jedis != null) {
            String ping = jedis.ping();
            jedis.close();
            return ping.equals("PONG");
        }
        return false;
    }

    public Boolean saveRedisString(String key, String value) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            jedis.set(key, value);
            jedis.close();
            return true;
        }
        return false;
    }

    public Boolean saveRedisHash(String key, Map<String, String> map) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            jedis.hmset(key, map);
            jedis.close();
            return true;
        }
        return false;
    }

    public Boolean saveRedisList(String listName, String value) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            jedis.lpush(listName, value);
            jedis.close();
            return true;
        }
        return false;
    }

    public Map<String, String> getRedisHashAll(String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            Map<String, String> map = jedis.hgetAll(key);
            jedis.close();
            return map;
        }
        return null;
    }

    public String getRedisHashField(String key, String fieldName) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            String fieldValue = jedis.hget(key, fieldName);
            jedis.close();
            return fieldValue;
        }
        return null;
    }

    public Boolean updateRedisHash(String key, Map hashMap) {
        return false;
    }

    public Boolean updateRedisHashByUser(String username, Map hashMap) {
        return false;
    }

    public List<String> getListByKey(String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            List<String> list = jedis.lrange(key, 0, -1);
            jedis.close();
            return list;
        }
        return null;
    }

    public Boolean setExpire(String key, int seconds){
        Jedis jedis = getJedis();
        if (jedis != null){
            if (seconds > 0){
                jedis.expire(key, seconds);
            }else {
                jedis.persist(key);
            }
            jedis.close();
            return true;
        }
        return false;
    }

    public Boolean deleteByKey(String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            jedis.del(key);
            jedis.close();
            return true;
        }
        return false;
    }

    public Boolean delListByValue(String key, String value) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            jedis.lrem(key, 0, value);
            jedis.close();
            return true;
        }
        return false;
    }

    public Boolean delHashByField(String key, String field){
        Jedis jedis = getJedis();
        if (jedis != null){
            jedis.hdel(key, field);
            jedis.close();
            return true;
        }
        return false;
    }

    public Long getListLen(String key){
        Jedis jedis = getJedis();
        if (jedis != null){
            Long length = jedis.llen(key);
            jedis.close();
            return length;
        }
        return null;
    }

    public Long getHashCount(String key){
        Jedis jedis = getJedis();
        if (jedis != null){
            Long count = jedis.hlen(key);
            jedis.close();
            return count;
        }
        return null;
    }

    public void psubscribe(JedisPubSub jedisPubSub, String... patterns){
        Jedis jedis = getJedis();
        if (jedis != null){
            jedis.psubscribe(jedisPubSub, patterns);
            jedis.close();
        }
    }
}
