package com.project.stocker.util;

import com.project.stocker.entity.Stock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class JsoupCrawlingTest {

    @Test
    @DisplayName("getStocks 성공 테스트")
    void getStocksSuccessTest() {
        // given
        JsoupCrawling jsoupCrawling = new JsoupCrawling();

        // when
        List<Stock> stocks = jsoupCrawling.getStocks();

        // then
        Assertions.assertEquals(stocks.size(), 2000);
    }
}