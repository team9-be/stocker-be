package com.project.stocker.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.stocker.dto.request.TradeCreateRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class TradeSubscriber implements MessageListener {

    @Autowired
    private TradeService tradeService;

    // TradeCreateRequestDto Serializer
    private Jackson2JsonRedisSerializer<TradeCreateRequestDto> tradeCreateRequestDtoSerializer;

    // Serializer와 ObjectMapper 초기화
    public TradeSubscriber() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        this.tradeCreateRequestDtoSerializer = new Jackson2JsonRedisSerializer<>(TradeCreateRequestDto.class);
    }


    //Message를 받으면 TradeService실행
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String topicName = new String(pattern);

        switch (topicName) {
            case "BuyOrder":
                TradeCreateRequestDto buyOrderDto = deserializeToTradeCreateRequestDto(message.getBody());
                tradeService.subBuyOrders(buyOrderDto);
                break;
            case "SellOrder":
                TradeCreateRequestDto sellOrderDto = deserializeToTradeCreateRequestDto(message.getBody());
                tradeService.subSellOrders(sellOrderDto);
                break;

            default:
                System.out.println("Received message from unknown topic: " + topicName);
                break;
        }
    }

    //TradeCreateRequestDto 객체로 deserialize
    private TradeCreateRequestDto deserializeToTradeCreateRequestDto(byte[] body) {
        return tradeCreateRequestDtoSerializer.deserialize(body);
    }
}
