package com.project.stocker.configure;

import com.project.stocker.service.TradeSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisPubSubConfig {

    private String buyTopicName = "Buy";
    private String sellTopicName = "Sell";

    @Bean
    public ChannelTopic buyTopic() {
        return new ChannelTopic(buyTopicName);
    }

    @Bean
    public ChannelTopic sellTopic() {
        return new ChannelTopic(sellTopicName);
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(TradeSubscriber tradeSubscriber) {
        return new MessageListenerAdapter(tradeSubscriber, "onMessage"); // onMessage를 참조
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter listenerAdapter, ChannelTopic buyTopic, ChannelTopic sellTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, buyTopic);
        container.addMessageListener(listenerAdapter, sellTopic);
        return container;
    }
}