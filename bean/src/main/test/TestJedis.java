import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Collections;
import java.util.Scanner;

/**
 * Created by Administrator on 2018/3/22.
 * Desc:
 */
public class TestJedis {
    public static void main(String[] args) {
//        JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
//
//        Jedis jedis = pool.getResource();
//        jedis.set("key", "新浪微博：小叶子一点也不逗");
//        jedis.expire("key", 5);
//        while (true) {
//            Scanner sc = new Scanner(System.in);
//            int n = sc.nextInt();
//            if (n == 0){
//                break;
//            }
//            int count = 1;
//            Boolean flag = true;
//            while (flag) {
//                int error = 0;
//                for (int i = 0; i < Math.pow(2, count); i++) {
//                    int money = 0;
//                    StringBuilder s = new StringBuilder(Integer.toBinaryString(i));
//                    int range = count - s.length();
//                    for (int j = 0; j < range; j++) {
//                        s.insert(0, "0");
//                    }
//                    char[] charArray = s.toString().toCharArray();
//                    for (char c : charArray) {
//                        int intNum = c - '0';
//                        money = money * 2 + 1 + intNum;
//                    }
//                    if (money == n) {
//                        for (char c : charArray) {
//                            System.out.print(c - '0' + 1);
//                        }
//                        flag = false;
//                        break;
//                    }
//                }
//                count++;
//            }
//        }
    }
}
