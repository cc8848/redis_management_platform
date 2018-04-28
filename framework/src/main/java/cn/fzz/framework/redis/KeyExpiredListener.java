package cn.fzz.framework.redis;

import cn.fzz.bean.RedisSubscriberEvent;
import cn.fzz.util.SpringContextUtils;
import redis.clients.jedis.JedisPubSub;
import cn.fzz.service.RedisLogService;


/**
 * Created by Administrator on 2018/3/22.
 * Desc:
 */
public class KeyExpiredListener extends JedisPubSub {
    private String serverName = "localhost";
    private static RedisLogService redisLogService = (RedisLogService) SpringContextUtils.getBean(RedisLogService.class);

    public KeyExpiredListener() {
    }

    public KeyExpiredListener(String serverName) {
        this.serverName = serverName;
    }

    @Override
    // 取得订阅的消息后的处理
    public void onMessage(String channel, String message) {
        //TODO:接收订阅频道消息后，业务处理逻辑
        System.out.println(channel + "=" + message);
    }

    @Override
    // 初始化订阅时候的处理
    public void onSubscribe(String channel, int subscribedChannels) {
        // System.out.println(channel + "=" + subscribedChannels);
    }

    @Override
// 取消订阅时候的处理
    public void onUnsubscribe(String channel, int subscribedChannels) {
        // System.out.println(channel + "=" + subscribedChannels);
    }

    @Override
    // 初始化按表达式的方式订阅时候的处理
    public void onPSubscribe(String pattern, int subscribedChannels) {
        // System.out.println(pattern + "=" + subscribedChannels);
    }

    @Override
    // 取消按表达式的方式订阅时候的处理
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        // System.out.println(pattern + "=" + subscribedChannels);
    }

    @Override
    // 取得按表达式的方式订阅的消息后的处理
    public void onPMessage(String pattern, String channel, String message) {
        RedisSubscriberEvent redisExpireEvent = new RedisSubscriberEvent();
        redisExpireEvent.setEvent_name(channel + " " + message);
        redisExpireEvent.setServer(serverName);
        redisExpireEvent.setKey_pattern(pattern.substring(pattern.indexOf(":")+1));
        String noticeType = channel.substring(2, channel.indexOf("@"));
        redisExpireEvent.setNotice_type(noticeType);
        redisExpireEvent.setEvent_type(noticeType.equals("keyspace")?message:channel.substring(channel.indexOf(":")+1));
        redisExpireEvent.setRedis_key(noticeType.equals("keyevent")?message:channel.substring(channel.indexOf(":")+1));
        redisExpireEvent.setRedis_value("");
        redisExpireEvent.setPattern(pattern);
        redisExpireEvent.setChannel(channel);
        redisExpireEvent.setData_type("");
        redisLogService.saveSubscriberEvent(redisExpireEvent);

        System.out.println("onPMessage pattern "
                + pattern + " " + channel + " " + message);
    }
}