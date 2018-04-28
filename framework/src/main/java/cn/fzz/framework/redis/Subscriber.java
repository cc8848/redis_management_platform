package cn.fzz.framework.redis;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 * Desc:
 */
public class Subscriber extends Thread{
    private RedisConnection redisConnection = new RedisConnection(6379);
    private String serverName = "localhost";

    public Subscriber(String serverName, RedisConnection redisConnection) {
        this.serverName = serverName;
        this.redisConnection = redisConnection;
    }

    @Override
    public void run(){
        String noticeType = RedisCommon.getStringByKey("notice_type");
        if (StringUtils.isEmpty(noticeType)){
            noticeType = "key*";
        }
        List<String> keyPatterns = RedisCommon.getListFromRedis("key_patterns");
        String db = RedisCommon.getStringByKey("db");

        List<String> patternsList = new ArrayList<>();

        if (keyPatterns.size() > 0){
            for (String keyPattern: keyPatterns){
                patternsList.add("__" + noticeType + "@0__:" + keyPattern);
            }
        } else {
            patternsList.add("__" + noticeType + "@0__:" + "*");
        }
        String[] patterns = new String[patternsList.size()];
        patternsList.toArray(patterns);
        redisConnection.psubscribe(new KeyExpiredListener(serverName), patterns);
    }
}
