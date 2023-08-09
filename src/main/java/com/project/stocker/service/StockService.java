package com.project.stocker.service;

import com.project.stocker.entity.Stock;
import com.project.stocker.repository.StockRepository;
import com.project.stocker.util.JsoupCrawling;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final JsoupCrawling jsoupCrawling;

    public void saveStockList() {
        List<Stock> stocks = jsoupCrawling.getStocks().stream().map(Stock::new).toList();
        stockRepository.saveAll(stocks);
    }

}
