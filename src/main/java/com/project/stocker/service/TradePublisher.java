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

    @Autowired
    private ChannelTopic buyTopic;

    @Autowired
    private ChannelTopic sellTopic;

    public void publishBuy(TradeCreateRequestDto buyCreateDto) {
        redisTemplate.convertAndSend(buyTopic.getTopic(), buyCreateDto);
    }

    public void publishSell(TradeCreateRequestDto sellCreateDto) {
        redisTemplate.convertAndSend(sellTopic.getTopic(), sellCreateDto);
    }


}