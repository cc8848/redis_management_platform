package cn.fzz.thread;

import cn.fzz.bean.RedisExpireEvent;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPubSub;
import cn.fzz.service.RedisLogService;

import javax.annotation.PostConstruct;


/**
 * Created by Administrator on 2018/3/22.
 * Desc:
 */
public class KeyExpiredListener extends JedisPubSub {
    private String server = "localhost";
    private static RedisLogService redisLogService;

    private KeyExpiredListener keyExpiredListener;

    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        keyExpiredListener = this;
        keyExpiredListener.redisLogService = this.redisLogService;
        // 初使化时将已静态化的testService实例化
    }
    @Autowired
    public KeyExpiredListener(RedisLogService redisLogService) {
        this.redisLogService = redisLogService;
    }

    public KeyExpiredListener() {
    }

    public KeyExpiredListener(String server) {
        this.server = server;
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        System.out.println("onPSubscribe "
                + pattern + " " + subscribedChannels);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        RedisExpireEvent redisExpireEvent = new RedisExpireEvent();
        redisExpireEvent.setEvent_name(channel + message);
        redisExpireEvent.setServer(server);
        redisExpireEvent.setKey(channel);
        redisExpireEvent.setValue(message);
        redisExpireEvent.setData_type("");
        redisLogService.saveExpireEvent(redisExpireEvent);

        System.out.println("onPMessage pattern "
                + pattern + " " + channel + " " + message);
    }
}