package cn.fzz.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.*;

public final class RedisUtil {
    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    //Redis服务器IP
    private static String ADDR = "127.0.0.1";

    //Redis的端口号
    private static int PORT = 6379;

    //访问密码
//    private static String AUTH = "admin";

    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 1000;

    private static int TIMEOUT = 100000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        initJedisPool();
    }

    private static void initJedisPool(){
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(MAX_IDLE);
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT); //jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
            flushAll();
            initLocalhost();
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
    public synchronized static Jedis getJedis() {
        try {
            return jedisPool != null?jedisPool.getResource(): null;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static Set<String> getKeys(){
        Jedis jedis = getJedis();
        if (jedis != null){
            Set<String> keys = jedis.keys("*");
            jedis.close();
            return keys;
        }
        return null;
    }

    public static String getStringByKey(String key){
        Jedis jedis = getJedis();
        if (jedis != null){
            String string = jedis.get(key);
            jedis.close();
            return string;
        }
        return null;
    }

    public static String pingByPort(int port){
        PORT = port;
        initJedisPool();
        Jedis jedis = getJedis();
        if (jedis != null){
            String ping = jedis.ping();
            jedis.close();
            return ping.equals("PONG")?"success":"fail";
        }
        return "fail";
    }

    public static Boolean checkByPort(int port){
        PORT = port;
        Jedis jedis = getJedis();
        if (jedis != null){
            String ping = jedis.ping();
            jedis.close();
            return ping.equals("PONG");
        }
        return false;
    }

    public static Boolean saveRedisString(String key, String value){
        Jedis jedis = getJedis();
        if (jedis != null){
            jedis.set(key, value);
            jedis.close();
            return true;
        }
        return false;
    }

    public static Boolean saveRedisHash(String key, Map<String, String> map){
        Jedis jedis = getJedis();
        if (jedis != null){
            jedis.hmset(key, map);
            jedis.close();
            return true;
        }
        return false;
    }

    public static Boolean saveRedisList(String listName, String value){
        Jedis jedis = getJedis();
        if (jedis != null){
            jedis.lpush(listName, value);
            jedis.close();
            return true;
        }
        return false;
    }

    public static Map<String, String> getRedisHashAll(String key){
        Jedis jedis = getJedis();
        if (jedis != null){
            Map<String, String> map = jedis.hgetAll(key);
            jedis.close();
            return map;
        }
        return null;
    }

    public static String getHashFeildValue(String key, String childKey){
        Jedis jedis = getJedis();
        if (jedis != null){
            String value = jedis.hget(key, childKey);
            jedis.close();
            return value;
        }
        return null;
    }

    public static String getRedisHashField(String key, String fieldName){
        Jedis jedis = getJedis();
        if (jedis != null){
            String fieldValue = jedis.hget(key, fieldName);
            jedis.close();
            return fieldValue;
        }
        return null;
    }

    public static Boolean updateRedisHash(String key, Map hashMap){
        return false;
    }

    public static Boolean updateRedisHashByUser(String username, Map hashMap){
        return false;
    }

    public static List<String> getListByKey(String key){
        Jedis jedis = getJedis();
        if (jedis != null){
            List<String> list =jedis.lrange(key, 0, -1);
            jedis.close();
            return list;
        }
        return null;
    }

    public static Boolean deleteByKey(String key){
        Jedis jedis = getJedis();
        if (jedis != null){
            jedis.del(key);
            jedis.close();
            return true;
        }
        return false;
    }

    public static Boolean delListByValue(String key, String value){
        Jedis jedis = getJedis();
        if (jedis != null){
            jedis.lrem(key, 0, value);
            jedis.close();
            return true;
        }
        return false;
    }

    public static Boolean delHashByFeild(String key, String field){
        Jedis jedis = getJedis();
        if (jedis != null){
            jedis.hdel(key, field);
            jedis.close();
            return true;
        }
        return false;
    }

    public static Boolean setExpire(String key, int seconds){
        Jedis jedis = getJedis();
        if (jedis != null){
            if (seconds == -1){
                jedis.persist(key);
            }else {
                jedis.expire(key, seconds);
            }
            jedis.close();
            return true;
        }
        return false;
    }

    public static Boolean hExists(String key, String field){
        Jedis jedis = getJedis();
        if (jedis != null){
            Boolean isExists = jedis.hexists(key, field);
            jedis.close();
            return isExists;
        }
        return false;
    }

    public static Long hDel(String key, String field){
        Jedis jedis = getJedis();
        if (jedis != null){
            Long result = jedis.hdel(key, field);
            jedis.close();
            return result;
        }
        return 0L;
    }

    public static String hGet(String key, String field){
        Jedis jedis = getJedis();
        if (jedis != null){
            String result = jedis.hget(key, field);
            jedis.close();
            return result;
        }
        return null;
    }

    private static void flushAll(){
        Jedis jedis = getJedis();
        if (jedis != null){
            jedis.flushAll();
            jedis.close();
        }
    }

    private static void initLocalhost(){
        Map<String, String> localhost = new HashMap<>();
        localhost.put("port", "6379");
        localhost.put("version", "v3.2.100");
        localhost.put("time", "1521002602005");
        localhost.put("redisSoftwarePath", "../redis/redis-server.exe");
        localhost.put("redisConfigPath", "web/src/main/resources/configs/redis6379.conf");
        saveRedisHash("localhost", localhost);
        saveRedisHash("localhostState", localhost);
        saveRedisList("machineList", "machine1");
        saveRedisList("alreadyList", "localhost");
    }
}