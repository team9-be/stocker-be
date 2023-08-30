package com.project.stocker.service;

import com.project.stocker.dto.request.TradeCreateRequestDto;
import com.project.stocker.dto.request.TradeDeleteRequestDto;
import com.project.stocker.dto.request.TradeUpdateRequestDto;
import com.project.stocker.dto.response.TradeCreateResponseDto;
import com.project.stocker.dto.response.TradeDeleteResponseDto;
import com.project.stocker.dto.response.TradeUpdateResponseDto;
import com.project.stocker.entity.Orders;
import com.project.stocker.entity.Stock;
import com.project.stocker.entity.Trade;
import com.project.stocker.entity.User;
import com.project.stocker.repository.OrdersRepository;
import com.project.stocker.repository.StockRepository;
import com.project.stocker.repository.TradeRepository;
import com.project.stocker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class TradeService {
    private final TradeRepository tradeRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;
    private final OrdersRepository ordersRepository;

    @Autowired
    private TradePublisher tradePublisher;

    //sell order publish
    public TradeCreateResponseDto sellOrders(TradeCreateRequestDto ordersCreatRequestDto) {
        tradePublisher.publishSellOrders(ordersCreatRequestDto);
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매도 주문 처리 중");
    }

    //sell order subscriber
    public TradeCreateResponseDto subSellOrders(TradeCreateRequestDto ordersCreateRequestDto) {
        String stockName = ordersCreateRequestDto.getStock();
        Long quantity = ordersCreateRequestDto.getQuantity();
        Long buyPrice = ordersCreateRequestDto.getPrice();

        User user1 = userRepository.findById(1L).orElseThrow(() ->
                new IllegalArgumentException("id가 1인 유저가 존재하지 않습니다."));

        Stock stock = (Stock) stockRepository.findByCompany(stockName).orElseThrow(() ->
                new IllegalArgumentException("해당 종목이 존재하지 않습니다."));

        Orders trade = new Orders.Builder(quantity, buyPrice, stock)
                .seller(user1)
                .build();

        ordersRepository.save(trade);
        matchOrders();
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매도 신청 성공.");
    }

    //sell orders update
    public TradeUpdateResponseDto sellUpdate(TradeUpdateRequestDto sellUpdateDto) {

        Long tradeId = sellUpdateDto.getTrade_id();
        Long quantity = sellUpdateDto.getQuantity();
        Long buyPrice = sellUpdateDto.getPrice();

        // tradeId validation
        Orders orders = (Orders) ordersRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매도 신청이 존재하지 않습니다."));

        // trade status validation
        orders.setQuantity(quantity);
        orders.setPrice(buyPrice);
        orders.setStatus("updated");
        ordersRepository.save(orders);

        return new TradeUpdateResponseDto(HttpStatus.OK.value(), "매도 신청 수정 성공.");
    }

    //sell orders delete
    public TradeDeleteResponseDto sellDelete(TradeDeleteRequestDto sellDeleteDto) {

        Long tradeId = sellDeleteDto.getTrade_id();

        // trade Id validation
        Orders orders = (Orders) ordersRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        //user validation
        ordersRepository.delete(orders);

        return new TradeDeleteResponseDto(HttpStatus.OK.value(), "매도 취소 성공.");
    }

    //buy orders publish
    public TradeCreateResponseDto buyOrders(TradeCreateRequestDto ordersCreatRequestDto) {
        tradePublisher.publishBuyOrders(ordersCreatRequestDto);
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매수 주문 처리 중");
    }

    //buy orders subscriber
    public TradeCreateResponseDto subBuyOrders(TradeCreateRequestDto ordersCreateRequestDto) {
        String stockName = ordersCreateRequestDto.getStock();
        Long quantity = ordersCreateRequestDto.getQuantity();
        Long buyPrice = ordersCreateRequestDto.getPrice();

        User user2 = userRepository.findById(2L).orElseThrow(() ->
                new IllegalArgumentException("id가 2인 유저가 존재하지 않습니다."));

        Stock stock = (Stock) stockRepository.findByCompany(stockName).orElseThrow(() ->
                new IllegalArgumentException("해당 종목이 존재하지 않습니다."));

        Orders trade = new Orders.Builder(quantity, buyPrice, stock)
                .buyer(user2)
                .build();

        ordersRepository.save(trade);
        matchOrders();
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매수 신청 성공.");
    }

    //buy orders update
    public TradeUpdateResponseDto buyUpdate(TradeUpdateRequestDto buyUpdateDto) {

        Long tradeId = buyUpdateDto.getTrade_id();
        Long quantity = buyUpdateDto.getQuantity();
        Long buyPrice = buyUpdateDto.getPrice();

        // tradeId validation
        Orders orders = (Orders) ordersRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        // trade status validation
        orders.setQuantity(quantity);
        orders.setPrice(buyPrice);
        orders.setStatus("updated");
        ordersRepository.save(orders);

        return new TradeUpdateResponseDto(HttpStatus.OK.value(), "매수 신청 수정 성공.");
    }

    //buy orders delete
    public TradeDeleteResponseDto buyDelete(TradeDeleteRequestDto buyDeleteDto) {

        Long tradeId = buyDeleteDto.getTrade_id();

        // trade Id validation
        Orders orders = (Orders) ordersRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        //user validation
        ordersRepository.delete(orders);

        return new TradeDeleteResponseDto(HttpStatus.OK.value(), "매수 취소 성공.");
    }

    //Matching function
    private void matchOrders() {
        List<Orders> allOrders = ordersRepository.findAll();

        for (Orders buyOrder : allOrders) {
            if (buyOrder.getBuyer() == null) continue;

            for (Orders sellOrder : allOrders) {
                if (sellOrder.getSeller() == null) continue;

                if (buyOrder.getStock().equals(sellOrder.getStock()) &&
                        buyOrder.getPrice().equals(sellOrder.getPrice()) &&
                        buyOrder.getQuantity().equals(sellOrder.getQuantity())) {

                    Trade trade = new Trade.Builder(buyOrder.getQuantity(), buyOrder.getPrice(), buyOrder.getStock())
                            .buyer(buyOrder.getBuyer())
                            .seller(sellOrder.getSeller())
                            .build();
                    trade.setStatus("confirm");
                    tradeRepository.save(trade);

                    ordersRepository.delete(buyOrder);
                    ordersRepository.delete(sellOrder);
                    return;
                }
            }
        }
    }
}


