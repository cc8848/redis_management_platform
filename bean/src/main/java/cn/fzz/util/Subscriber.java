package cn.fzz.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Administrator on 2018/3/22.
 * Desc:
 */
public class Subscriber {
    public static void main(String[] args) {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

        Jedis jedis = pool.getResource();
        jedis.psubscribe(new KeyExpiredListener(), "__key*__:*");

    }
}
