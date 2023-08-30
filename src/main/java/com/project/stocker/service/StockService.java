package com.project.stocker.service;

import com.project.stocker.dto.request.TradeDto;
import com.project.stocker.dto.response.StockResponseDto;
import com.project.stocker.entity.Stock;
import com.project.stocker.entity.Trade;
import com.project.stocker.entity.User;
import com.project.stocker.repository.StockRepository;
import com.project.stocker.repository.TradeRepository;
import com.project.stocker.repository.UserRepository;
import com.project.stocker.util.JsoupCrawling;
import com.project.stocker.util.YesterdayPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final JsoupCrawling jsoupCrawling;
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final YesterdayPrice yesterdayPrice;

    @Transactional
    public void saveStockList() {
        List<Stock> stocks = jsoupCrawling.getStocks();
        stockRepository.saveAll(stocks);
    }

    @Transactional
    public void saveTradeList() {
        User user1 = userRepository.findById(1L).orElseThrow(() ->
                new IllegalArgumentException("id가 1인 유저가 존재하지 않습니다."));
        User user2 = userRepository.findById(2L).orElseThrow(() ->
                new IllegalArgumentException("id가 2인 유저가 존재하지 않습니다."));
        List<Stock> stocks = stockRepository.findAll();
        List<TradeDto> trades = jsoupCrawling.getTrades(stocks);
        List<Trade> TradeList = trades.parallelStream().map(tradeDto -> new Trade.Builder(tradeDto.getQuantity(), tradeDto.getPrice(), tradeDto.getStock())
                .buyer(user1)
                .seller(user2)
                .build()).toList();
        tradeRepository.saveAll(TradeList);
    }

    @Transactional(readOnly = true)
    public StockResponseDto getStock(Long stockId) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(() ->
                new IllegalArgumentException("해당 id의 주식이 존재하지 않습니다."));
        Trade tradeByStock = tradeRepository.findTop1ByStock_IdAndBuyerIsNotNullAndSellerIsNotNullOrderByCreatedAtDesc(stock.getId());
        if (tradeByStock == null)
            throw new IllegalArgumentException("해당 id의 주식의 거래내역이 존재하지 않습니다.");
        return new StockResponseDto(stockId, stock.getCompany(), tradeByStock.getPrice(), yesterdayPrice.getYesterdayLastPrice(stockId));
    }

}
