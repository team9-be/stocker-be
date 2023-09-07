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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


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



    @Autowired
    private TradePublisher tradePublisher;

    //sell order publish
    public TradeCreateResponseDto sellOrders(
            TradeCreateRequestDto ordersCreateRequestDto,
            HttpServletRequest request,
            Long userId
    ) {
        String token = jwtUtil.getJwtFromRequest(request);
        ordersCreateRequestDto.setToken(token);
        tradePublisher.publishSellOrders(ordersCreateRequestDto);
        Account account = accountRepository.findByUserIdAndStockCompany(
                userId,
                ordersCreateRequestDto.getStock()).orElseThrow(
                        () -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다.")
        );
        if(ordersCreateRequestDto.getQuantity() > account.getQuantity()){
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
    public TradeCreateResponseDto buyOrders(TradeCreateRequestDto ordersCreateRequestDto, HttpServletRequest request) {
        String token = jwtUtil.getJwtFromRequest(request);
        ordersCreateRequestDto.setToken(token);
        tradePublisher.publishBuyOrders(ordersCreateRequestDto);
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
    @Async
    @Transactional
    public void matchOrders() {
        System.out.println("Current Thread : " + Thread.currentThread().getName());
        List<Orders> allOrders = ordersRepository.findAll();
        allOrders.stream()
                .filter(order -> order.getBuyer() != null)
                .forEach(buyOrder -> {
                    allOrders.stream()
                            .filter(order -> order.getSeller() != null)
                            .filter(sellOrder -> isMatchingOrder(buyOrder, sellOrder))
                            .findFirst()
                            .ifPresent(sellOrder -> processMatchingOrders(buyOrder, sellOrder));
                });
    }

    private boolean isMatchingOrder(Orders buyOrder, Orders sellOrder) {
        return buyOrder.getStock().equals(sellOrder.getStock()) &&
                buyOrder.getPrice().equals(sellOrder.getPrice()) &&
                buyOrder.getQuantity().equals(sellOrder.getQuantity());
    }

    private void processMatchingOrders(Orders buyOrder, Orders sellOrder) {
        Trade trade = createTrade(buyOrder, sellOrder);
        updateTradeStatus(trade);
        updateAccount(buyOrder.getBuyer(), trade.getStock().getCompany(), trade.getQuantity(), trade);
        updateAccount(sellOrder.getSeller(), trade.getStock().getCompany(), -trade.getQuantity(), trade);
        deleteOrders(buyOrder, sellOrder);
    }

    private Trade createTrade(Orders buyOrder, Orders sellOrder) {
        return new Trade.Builder(buyOrder.getQuantity(), buyOrder.getPrice(), buyOrder.getStock())
                .buyer(buyOrder.getBuyer())
                .seller(sellOrder.getSeller())
                .build();
    }


    private void updateTradeStatus(Trade trade) {
        trade.setStatus("confirm");
        tradeRepository.save(trade);
    }

    private void updateAccount(User user, String stockCompany, Long quantity, Trade trade) {
        Optional<Account> accountOptional = accountRepository.findByUserIdAndStockCompany(user.getId(), stockCompany);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.changeQuantity(quantity);
        } else {
            Account account = new Account(user, trade);
            accountRepository.save(account);
        }
    }

    private void deleteOrders(Orders buyOrder, Orders sellOrder) {
        ordersRepository.delete(buyOrder);
        ordersRepository.delete(sellOrder);
    }

    public TradeCreateResponseDto testBuy() {
        String token = jwtUtil.createToken("ht@gmail.com", UserRoleEnum.USER);
        TradeCreateRequestDto tradeCreateRequestDto = new TradeCreateRequestDto();
        tradeCreateRequestDto.setPrice(70000L);
        tradeCreateRequestDto.setToken(token.substring(7));
        tradeCreateRequestDto.setQuantity(10L);
        tradeCreateRequestDto.setStock("삼성전자");
        tradePublisher.publishBuyOrders(tradeCreateRequestDto);
        return new TradeCreateResponseDto(200, "매수 주문 처리 중");

    }
    public TradeCreateResponseDto testBuy2() {
        String token = jwtUtil.createToken("ht@gmail.com", UserRoleEnum.USER);
        TradeCreateRequestDto tradeCreateRequestDto = new TradeCreateRequestDto();
        tradeCreateRequestDto.setPrice(100000L);
        tradeCreateRequestDto.setToken(token.substring(7));
        tradeCreateRequestDto.setQuantity(10L);
        tradeCreateRequestDto.setStock("SK하이닉스");
        tradePublisher.publishBuyOrders(tradeCreateRequestDto);
        return new TradeCreateResponseDto(200, "매수 주문 처리 중");

    }
    public TradeCreateResponseDto testSell(){
        String token = jwtUtil.createToken("ht2@gmail.com", UserRoleEnum.USER);
        System.out.println("token = " + token);
        TradeCreateRequestDto tradeCreateRequestDto = new TradeCreateRequestDto();
        tradeCreateRequestDto.setPrice(70000L);
        tradeCreateRequestDto.setToken(token.substring(7));
        tradeCreateRequestDto.setQuantity(10L);
        tradeCreateRequestDto.setStock("삼성전자");
        tradePublisher.publishSellOrders(tradeCreateRequestDto);
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매도 주문 처리 중");
    }
    public TradeCreateResponseDto testSell2(){
        String token = jwtUtil.createToken("ht2@gmail.com", UserRoleEnum.USER);
        TradeCreateRequestDto tradeCreateRequestDto = new TradeCreateRequestDto();
        tradeCreateRequestDto.setPrice(100000L);
        tradeCreateRequestDto.setToken(token.substring(7));
        tradeCreateRequestDto.setQuantity(10L);
        tradeCreateRequestDto.setStock("SK하이닉스");
        tradePublisher.publishSellOrders(tradeCreateRequestDto);
        return new TradeCreateResponseDto(HttpStatus.OK.value(), "매도 주문 처리 중");
    }
}


