package com.project.stocker.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.stocker.dto.request.TradeCreateRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

@Service
public class TradeSubscriber implements MessageListener {

    @Autowired
    private TradeService tradeService;

    private final Jackson2JsonRedisSerializer<TradeCreateRequestDto> serializer;

    public TradeSubscriber() {
        this.serializer = new Jackson2JsonRedisSerializer<>(TradeCreateRequestDto.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }



    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topicName = new String(pattern);
        TradeCreateRequestDto tradeCreateDto = serializer.deserialize(message.getBody());

        if ("Buy".equals(topicName)) {
            tradeService.subBuyCreate(tradeCreateDto);
        } else if ("Sell".equals(topicName)) {
            tradeService.subSellCreate(tradeCreateDto);
        } else {
            System.out.println("Received message from unknown topic: " + topicName);
        }
    }
}
