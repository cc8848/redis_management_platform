package cn.fzz.thread;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2018/3/22.
 * Desc:
 */
public class Subscriber {
    public static void main(String[] args) {
        String host = "localhost";
        JedisPool pool = new JedisPool(new JedisPoolConfig(), host);

        Jedis jedis = pool.getResource();
        jedis.psubscribe(new KeyExpiredListener(host), "__key*__:*");

    }
}
