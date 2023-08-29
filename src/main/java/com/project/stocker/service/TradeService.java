package com.project.stocker.service;

import com.project.stocker.dto.request.ConfirmTradeRequestDto;
import com.project.stocker.dto.request.TradeCreateRequestDto;
import com.project.stocker.dto.request.TradeDeleteRequestDto;
import com.project.stocker.dto.request.TradeUpdateRequestDto;
import com.project.stocker.dto.response.ConfirmTradeResponseDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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

    //sell confirm publish
    public ConfirmTradeResponseDto sellConfirm(ConfirmTradeRequestDto sellCreateDto) {
        tradePublisher.publishSell(sellCreateDto);
        return new ConfirmTradeResponseDto(HttpStatus.OK.value(), "매도 처리 중");
    }

    //sell confirm subscriber
    public ConfirmTradeResponseDto subSellConfirm(ConfirmTradeRequestDto sellRequestDto) {
        Long orderId = sellRequestDto.getTrade_id();

        Orders orders = ordersRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        Trade trade = new Trade.Builder(orders.getQuantity(), orders.getPrice(), orders.getStock())
                .seller(orders.getSeller())
                .build();
        trade.setStatus("confirm");
        tradeRepository.save(trade);
        ordersRepository.delete(orders);
        return new ConfirmTradeResponseDto(HttpStatus.OK.value(), "매도 확정 성공.");
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
                new IllegalArgumentException("id가 1인 유저가 존재하지 않습니다."));

        Stock stock = (Stock) stockRepository.findByCompany(stockName).orElseThrow(() ->
                new IllegalArgumentException("해당 종목이 존재하지 않습니다."));

        Orders trade = new Orders.Builder(quantity, buyPrice, stock)
                .buyer(user2)
                .build();

        ordersRepository.save(trade);
        log.info("매수 신청 성공");

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

    //buy confirm publish
    public ConfirmTradeResponseDto buyConfirm(ConfirmTradeRequestDto buyCreateDto) {
        tradePublisher.publishBuy(buyCreateDto);
        return new ConfirmTradeResponseDto(HttpStatus.OK.value(), "매수 처리 중");
    }

    //buy confirm subscriber
    public ConfirmTradeResponseDto subBuyConfirm(ConfirmTradeRequestDto buyRequestDto) {
        Long orderId = buyRequestDto.getTrade_id();
        String stockName = buyRequestDto.getStock();
        Long quantity = buyRequestDto.getQuantity();
        Long buyPrice = buyRequestDto.getPrice();

        List<Trade> matchingSells = tradeRepository
                .findByStockCompanyAndPriceAndQuantityAndBuyerIsNullOrderByCreatedAtAsc
                        (stockName, buyPrice, quantity);

        Orders orders = ordersRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        if (!matchingSells.isEmpty()) {
            Trade matchingTrade = matchingSells.get(0);

            matchingTrade.setBuyer(orders.getBuyer());
            matchingTrade.setStatus("confirm");
            tradeRepository.save(matchingTrade);

            ordersRepository.delete(orders);
            return new ConfirmTradeResponseDto(HttpStatus.OK.value(), "매수 확정 성공.");
        } else {
            throw new IllegalStateException("매칭되는 주문이 없습니다.");
        }
    }
}


