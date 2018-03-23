import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2018/3/22.
 * Desc:
 */
public class TestJedis {
    public static void main(String[] args) {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

        Jedis jedis = pool.getResource();
        jedis.set("key", "新浪微博：小叶子一点也不逗");
        jedis.expire("key", 5);
    }
}
