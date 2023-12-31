package com.project.stocker.service;

import com.project.stocker.dto.request.TradeCreateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class TradePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    //buyOrderTopic 채널
    @Autowired
    private ChannelTopic buyOrderTopic;

    //sellOrderTopic 채널
    @Autowired
    private ChannelTopic sellOrderTopic;

    //buy order topic publish
    public void publishBuyOrders(TradeCreateRequestDto buyOrderRequestDto) {
        redisTemplate.convertAndSend(buyOrderTopic.getTopic(), buyOrderRequestDto);
    }

    //sell order topic publish
    public void publishSellOrders(TradeCreateRequestDto sellOrderRequestDto) {
        redisTemplate.convertAndSend(sellOrderTopic.getTopic(), sellOrderRequestDto);
    }
}