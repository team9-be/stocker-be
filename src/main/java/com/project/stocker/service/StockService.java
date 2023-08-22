package com.project.stocker.service;

import com.project.stocker.dto.request.TradeDto;
import com.project.stocker.entity.*;
import com.project.stocker.repository.TradeRepository;
import com.project.stocker.repository.StockRepository;
import com.project.stocker.repository.UserRepository;
import com.project.stocker.util.JsoupCrawling;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final JsoupCrawling jsoupCrawling;
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;

    public void saveStockList() {
        List<Stock> stocks = jsoupCrawling.getStocks();
        stockRepository.saveAll(stocks);
    }

    public void saveTradeList() {
        User user = userRepository.findById(1L).orElseThrow(()->
                new IllegalArgumentException("id가 1인 유저가 존재하지 않습니다."));
        List<Stock> stocks = stockRepository.findAll();
        List<TradeDto> trades = jsoupCrawling.getTrades(stocks);
        List<Trade> buys = trades.parallelStream().map((
                tradeDto -> new Trade(
                        tradeDto.getQuantity(),
                        tradeDto.getPrice(),
                        tradeDto.getStock(),
                        user
                )
        )).toList();
        List<Trade> sells = trades.parallelStream().map((
                tradeDto -> new Trade(
                        tradeDto.getQuantity(),
                        tradeDto.getPrice(),
                        tradeDto.getStock(), user)
        )).toList();
        tradeRepository.saveAll(buys);
        tradeRepository.saveAll(sells);
    }
}
