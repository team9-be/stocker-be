package com.project.stocker.service;

import com.project.stocker.dto.request.TradeCreateRequestDto;
import com.project.stocker.dto.request.TradeDeleteRequestDto;
import com.project.stocker.dto.request.TradeUpdateRequestDto;
import com.project.stocker.dto.response.TradeCreateResponseDto;
import com.project.stocker.dto.response.TradeDeleteResponseDto;
import com.project.stocker.dto.response.TradeUpdateResponseDto;
import com.project.stocker.entity.*;
import com.project.stocker.jwt.JwtUtil;
import com.project.stocker.repository.*;
import jakarta.servlet.http.HttpServletRequest;
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
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;
    private final AccountService accountService;

    @Autowired
    private TradePublisher tradePublisher;

    //sell order publish
    public TradeCreateResponseDto sellOrders(TradeCreateRequestDto ordersCreatRequestDto, HttpServletRequest request,
                                             Long userId) {
        String token = jwtUtil.getJwtFromRequest(request);
        ordersCreatRequestDto.setToken(token);
        tradePublisher.publishSellOrders(ordersCreatRequestDto);
        ordersCreatRequestDto.getStock();
        if(accountRepository.findByUserIdAndStockCompany(userId, ordersCreatRequestDto.getStock()).isEmpty()){
            Account account = new Account(userRepository.findById(userId).get(), ordersCreatRequestDto);
            accountRepository.save(account);
        }
        Account account = accountRepository.findByUserIdAndStockCompany(userId, ordersCreatRequestDto.getStock()).get();
        if(ordersCreatRequestDto.getQuantity() > account.getQuantity()){
            throw new IllegalArgumentException("보유중인 주식이 부족합니다.");
        }
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매도 주문 처리 중");
    }


    //sell order subscriber
    public TradeCreateResponseDto subSellOrders(TradeCreateRequestDto ordersCreateRequestDto) {
        String stockName = ordersCreateRequestDto.getStock();
        Long quantity = ordersCreateRequestDto.getQuantity();
        Long buyPrice = ordersCreateRequestDto.getPrice();
        String token = ordersCreateRequestDto.getToken();

        String email = jwtUtil.getUserEmailFromToken(token);

        User user1 = userRepository.findByEmail(email).orElseThrow(() ->
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
    public TradeUpdateResponseDto sellUpdate(TradeUpdateRequestDto sellUpdateDto, HttpServletRequest request) {

        Long tradeId = sellUpdateDto.getTrade_id();
        Long quantity = sellUpdateDto.getQuantity();
        Long buyPrice = sellUpdateDto.getPrice();

        String token = jwtUtil.getJwtFromRequest(request);
        String email = jwtUtil.getUserEmailFromToken(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("email이 없습니다"));

        // tradeId validation
        Orders orders = (Orders) ordersRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매도 신청이 존재하지 않습니다."));

        if (!orders.getBuyer().equals(user) && !orders.getSeller().equals(user)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // trade status validation
        orders.setQuantity(quantity);
        orders.setPrice(buyPrice);
        orders.setStatus("updated");
        ordersRepository.save(orders);

        return new TradeUpdateResponseDto(HttpStatus.OK.value(), "매도 신청 수정 성공.");
    }

    //sell orders delete
    public TradeDeleteResponseDto sellDelete(TradeDeleteRequestDto sellDeleteDto, HttpServletRequest request) {

        Long tradeId = sellDeleteDto.getTrade_id();

        String token = jwtUtil.getJwtFromRequest(request);
        String email = jwtUtil.getUserEmailFromToken(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("email이 없습니다"));

        // trade Id validation
        Orders orders = (Orders) ordersRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        if (!orders.getBuyer().equals(user) && !orders.getSeller().equals(user)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        //user validation
        ordersRepository.delete(orders);

        return new TradeDeleteResponseDto(HttpStatus.OK.value(), "매도 취소 성공.");
    }

    //buy orders publish
    public TradeCreateResponseDto buyOrders(TradeCreateRequestDto ordersCreatRequestDto, HttpServletRequest request) {
        String token = jwtUtil.getJwtFromRequest(request);
        ordersCreatRequestDto.setToken(token);
        tradePublisher.publishBuyOrders(ordersCreatRequestDto);
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매수 주문 처리 중");
    }

    //buy orders subscriber
    public TradeCreateResponseDto subBuyOrders(TradeCreateRequestDto ordersCreateRequestDto) {
        String stockName = ordersCreateRequestDto.getStock();
        Long quantity = ordersCreateRequestDto.getQuantity();
        Long buyPrice = ordersCreateRequestDto.getPrice();
        String token = ordersCreateRequestDto.getToken();

        String email = jwtUtil.getUserEmailFromToken(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("유저가 존재하지 않습니다."));

        Stock stock = (Stock) stockRepository.findByCompany(stockName).orElseThrow(() ->
                new IllegalArgumentException("해당 종목이 존재하지 않습니다."));

        Orders trade = new Orders.Builder(quantity, buyPrice, stock)
                .buyer(user)
                .build();

        ordersRepository.save(trade);
        matchOrders();
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매수 신청 성공.");
    }

    //buy orders update
    public TradeUpdateResponseDto buyUpdate(TradeUpdateRequestDto buyUpdateDto, HttpServletRequest request) {

        Long tradeId = buyUpdateDto.getTrade_id();
        Long quantity = buyUpdateDto.getQuantity();
        Long buyPrice = buyUpdateDto.getPrice();

        String token = jwtUtil.getJwtFromRequest(request);
        String email = jwtUtil.getUserEmailFromToken(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("email이 없습니다"));

        // tradeId validation
        Orders orders = (Orders) ordersRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        if (!orders.getBuyer().equals(user) && !orders.getSeller().equals(user)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // trade status validation
        orders.setQuantity(quantity);
        orders.setPrice(buyPrice);
        orders.setStatus("updated");
        ordersRepository.save(orders);

        return new TradeUpdateResponseDto(HttpStatus.OK.value(), "매수 신청 수정 성공.");
    }

    //buy orders delete
    public TradeDeleteResponseDto buyDelete(TradeDeleteRequestDto buyDeleteDto, HttpServletRequest request) {

        Long tradeId = buyDeleteDto.getTrade_id();

        String token = jwtUtil.getJwtFromRequest(request);
        String email = jwtUtil.getUserEmailFromToken(token);

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("email이 없습니다"));

        // trade Id validation
        Orders orders = (Orders) ordersRepository.findById(tradeId).orElseThrow(() ->
                new IllegalArgumentException("해당 매수 신청이 존재하지 않습니다."));

        if (!orders.getBuyer().equals(user) && !orders.getSeller().equals(user)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        //user validation
        ordersRepository.delete(orders);

        return new TradeDeleteResponseDto(HttpStatus.OK.value(), "매수 취소 성공.");
    }

    //Matching function
    //Matching 시점에 my account insert
    @Transactional
    public void matchOrders() {
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
                    // myAccount
                    if (accountRepository.findByUserIdAndStockCompany(buyOrder.getBuyer().getId(), trade.getStock()
                            .getCompany()).isPresent()) {
                        Account account = accountRepository.findByUserIdAndStockCompany(buyOrder.getBuyer().getId(),
                                trade.getStock().getCompany()).get();
                        account.changeQuantity(trade.getQuantity());
                    } else {
                        Account account = new Account(
                                userRepository.findById(buyOrder.getBuyer().getId()).get(), trade);
                        accountRepository.save(account);
                    }
                    if (accountRepository.findByUserIdAndStockCompany(trade.getSeller().getId(), trade.getStock()
                            .getCompany()).isPresent()) {
                        Account account2 = accountRepository.findByUserIdAndStockCompany(trade.getSeller().getId(),
                                trade.getStock().getCompany()).get();
                        account2.changeQuantity(-trade.getQuantity());
                    }else {
                        Account account = new Account(
                                userRepository.findById(trade.getSeller().getId()).get(), trade);
                        accountRepository.save(account);
                    }
                    ordersRepository.delete(buyOrder);
                    ordersRepository.delete(sellOrder);
                    return;
                }
            }
        }
    }
}


