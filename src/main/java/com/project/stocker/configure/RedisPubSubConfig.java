package com.project.stocker.configure;

import com.project.stocker.service.TradeSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPubSubConfig {

    // 토픽 이름 정의
    private String buyOrderTopicName = "BuyOrder";
    private String sellOrderTopicName = "SellOrder";

    // 매수 order 토픽 bean 설정
    @Bean
    public ChannelTopic buyOrderTopic() {
        return new ChannelTopic(buyOrderTopicName);
    }

    // 매도 order 토픽 bean 설정
    @Bean
    public ChannelTopic sellOrderTopic() {
        return new ChannelTopic(sellOrderTopicName);
    }

    //MessageListenerAdapter
    @Bean
    public MessageListenerAdapter listenerAdapter(TradeSubscriber tradeSubscriber) {
        return new MessageListenerAdapter(tradeSubscriber, "onMessage"); // onMessage를 참조
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory,
                                                        MessageListenerAdapter listenerAdapter,
                                                        ChannelTopic buyOrderTopic,
                                                        ChannelTopic sellOrderTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, buyOrderTopic);
        container.addMessageListener(listenerAdapter, sellOrderTopic);
        return container;
    }
}