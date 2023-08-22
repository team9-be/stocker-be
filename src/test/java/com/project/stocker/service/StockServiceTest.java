package com.project.stocker.service;

import com.project.stocker.entity.Stock;
import com.project.stocker.entity.User;

import com.project.stocker.repository.StockRepository;
import com.project.stocker.repository.TradeRepository;
import com.project.stocker.repository.UserRepository;
import com.project.stocker.util.JsoupCrawling;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {
    @Mock
    StockRepository stockRepository;
    @Mock
    JsoupCrawling jsoupCrawling;
    @Mock
    TradeRepository tradeRepository;
    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("saveStockList 성공 테스트")
    void saveStockListSuccessTest() {
        // given
        List<Stock> stocks = new ArrayList<>();
        StockService stockService = new StockService(stockRepository, jsoupCrawling, tradeRepository, userRepository);
        given(jsoupCrawling.getStocks()).willReturn(stocks);

        // when
        stockService.saveStockList();
    }

    @Test
    @DisplayName("saveTradeList 성공 테스트")
    void saveTradeListSuccessTest() {
        // given
        StockService stockService = new StockService(stockRepository, jsoupCrawling, tradeRepository, userRepository);
        User user = new User();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        // when
        stockService.saveTradeList();
    }

    @Test
    @DisplayName("saveTradeList NoUser 실패 테스트")
    void saveTradeListNoUserTest() {
        // given
        StockService stockService = new StockService(stockRepository, jsoupCrawling, tradeRepository, userRepository);

        // when
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, stockService::saveTradeList);

        // then
        assertEquals(illegalArgumentException.getMessage(), "id가 1인 유저가 존재하지 않습니다.");
    }

}