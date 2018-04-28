package cn.fzz.dao.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;

/**
 * Created by fanzezhen on 2017/12/15.
 * Desc:
 */
public class Main {
    private static int count = 0;
    private static JedisPool jedisPool = null;
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Jedis jedis1 = new Jedis("127.0.0.1", 6380);
        new Main().setup(jedis1);
        Jedis jedis2 = new Jedis("127.0.0.1", 6380);
        new Main().setup(jedis2);
        try {
//            jedis1.close();
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(Arrays.toString(e.getStackTrace()));
        }
    }

    private void setup(Jedis jedis) {
        jedis.set("name" + count, "andy" + count);//向key-->name中放入了value-->andy
        logger.info(jedis.get("name" + count));//执行结果：andy
        count++;
    }
}
