package com.project.stocker.service;

import com.project.stocker.dto.request.ConfirmSellRequestDto;
import com.project.stocker.dto.request.TradeCreateRequestDto;
import com.project.stocker.dto.request.TradeDeleteRequestDto;
import com.project.stocker.dto.request.TradeUpdateRequestDto;
import com.project.stocker.dto.response.ConfirmSellResponseDto;
import com.project.stocker.dto.response.TradeCreateResponseDto;
import com.project.stocker.dto.response.TradeDeleteResponseDto;
import com.project.stocker.dto.response.TradeUpdateResponseDto;
import com.project.stocker.entity.Stock;
import com.project.stocker.entity.Trade;
import com.project.stocker.entity.User;
import com.project.stocker.repository.TradeRepository;
import com.project.stocker.repository.StockRepository;
import com.project.stocker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private TradePublisher tradePublisher;


    public TradeCreateResponseDto sellCreate(TradeCreateRequestDto sellCreateDto) {
        tradePublisher.publishSell(sellCreateDto);
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매도 처리 중");
    }

    // SEll
    public TradeCreateResponseDto subSellCreate(TradeCreateRequestDto sellCreateDto) {

        String stockName = sellCreateDto.getStock();
        Long quantity = sellCreateDto.getQuantity();
        Long buyPrice = sellCreateDto.getPrice();

        User user1 = userRepository.findById(1L).orElseThrow(() ->
                new IllegalArgumentException("id가 1인 유저가 존재하지 않습니다."));

        Stock stock = (Stock) stockRepository.findByCompany(stockName).orElseThrow(() ->
                new IllegalArgumentException("해당 종목이 존재하지 않습니다."));

        Trade trade = new Trade.Builder(quantity, buyPrice, stock)
                .seller(user1)
                .build();
        tradeRepository.save(trade);
        log.info("매도 신청 성공");
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매도 신청 성공.");
    }

    // 기존 메서드
    public TradeCreateResponseDto sellCreate1(TradeCreateRequestDto sellCreateDto) {

        String stockName = sellCreateDto.getStock();
        Long quantity = sellCreateDto.getQuantity();
        Long buyPrice = sellCreateDto.getPrice();

        Stock stock = (Stock) stockRepository.findByCompany(stockName).orElseThrow(() ->
                new IllegalArgumentException("해당 종목이 존재하지 않습니다."));

        Trade trade = new Trade.Builder(quantity, buyPrice, stock)
                .build();
        tradeRepository.save(trade);

        return new TradeCreateResponseDto(HttpStatus.OK.value(), "sellCreate1 매도 신청 성공.");
    }

    public TradeUpdateResponseDto sellUpdate(TradeUpdateRequestDto sellUpdateDto) {

        Long tradeId = sellUpdateDto.getTrade_id();
        Long quantity = sellUpdateDto.getQuantity();
        Long buyPrice = sellUpdateDto.getPrice();

        // tradeId validation
        Trade trade = (Trade) tradeRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매도 신청이 존재하지 않습니다."));

        // trade status validation
        trade.setQuantity(quantity);
        trade.setPrice(buyPrice);
        trade.setStatus("updated");
        tradeRepository.save(trade);
        // quantity & buyPrice validation -> wallet checking

        return new TradeUpdateResponseDto(HttpStatus.OK.value(), "매도 신청 수정 성공.");
    }

    public TradeDeleteResponseDto sellDelete(TradeDeleteRequestDto sellDeleteDto) {

        Long tradeId = sellDeleteDto.getTrade_id();

        // trade Id validation
        Trade trade = (Trade) tradeRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        //user validation
        tradeRepository.delete(trade);

        return new TradeDeleteResponseDto(HttpStatus.OK.value(), "매도 취소 성공.");
    }

    public ConfirmSellResponseDto confirmSell(ConfirmSellRequestDto sellUpdateDto) {
        Long tradeId = sellUpdateDto.getTrade_id();

        Trade trade = (Trade) tradeRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        trade.setStatus("confirm");
        return new ConfirmSellResponseDto(HttpStatus.OK.value(), "매도 확정 성공.");
    }


    // BUY
    public TradeCreateResponseDto buyCreate(TradeCreateRequestDto buyCreateDto) {
        tradePublisher.publishBuy(buyCreateDto);
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매수 처리 중");
    }

    public TradeCreateResponseDto subBuyCreate(TradeCreateRequestDto buyCreateDto) {

        String stockName = buyCreateDto.getStock();
        Long quantity = buyCreateDto.getQuantity();
        Long buyPrice = buyCreateDto.getPrice();

        List<Trade> matchingSells = tradeRepository.findByStockCompanyAndPriceAndQuantityAndBuyerIsNullOrderByCreatedAtAsc(stockName, buyPrice, quantity);

        if (!matchingSells.isEmpty()) {
            // 여기서는 가장 먼저 등록된 매도 주문을 선택합니다. 복잡한 로직(예: 최적 주문 선택)을 적용할 수도 있습니다.
            Trade matchedSell = matchingSells.get(0);

            User buyer = userRepository.findById(2L).orElseThrow(() ->
                    new IllegalArgumentException("id가 2인 유저가 존재하지 않습니다."));

            matchedSell.setBuyer(buyer);
            tradeRepository.save(matchedSell);

            log.info("매수 주문과 매칭됨.");
            return new TradeCreateResponseDto(HttpStatus.OK.value(), "매수 주문과 매칭됨.");
        }
        log.info("현재 매칭되는 매수 주문 없음.");
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "현재 매칭되는 매수 주문 없음.");
    }
}
//    public TradeUpdateResponseDto sellUpdate(TradeUpdateRequestDto sellUpdateDto){
//
//        Long tradeId = sellUpdateDto.getTrade_id();
//        Long quantity = sellUpdateDto.getQuantity();
//        Long buyPrice = sellUpdateDto.getPrice();
//
//        // tradeId validation
//        Trade trade = (Trade) tradeRepository.findById(tradeId).orElseThrow(()->
//                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));
//
//        // trade status validation
//        trade.setQuantity(quantity);
//        trade.setPrice(buyPrice);
//        trade.setStatus("updated");
//        tradeRepository.save(trade);
//        // quantity & buyPrice validation -> wallet checking
//
//        return new TradeUpdateResponseDto(HttpStatus.OK.value(), "매수 신청 수정 성공.");
//    }
//
//    public TradeDeleteResponseDto sellDelete(TradeDeleteRequestDto sellDeleteDto){
//
//        Long tradeId = sellDeleteDto.getTrade_id();
//
//        // trade Id validation
//        Trade trade = (Trade) tradeRepository.findById(tradeId).orElseThrow(()->
//                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));
//
//
//        // user validation
//
//        tradeRepository.delete(trade);
//
//        return new TradeDeleteResponseDto(HttpStatus.OK.value(), "매수 취소 성공.");
//    }
//



