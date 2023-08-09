package com.project.stocker.util;

import com.project.stocker.dto.request.StockDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsoupCrawling {

    private final String STOCK_URL_BASE = "https://finance.naver.com/sise/sise_market_sum.nhn?&page=";

    // 크롤링을 통해 주식명, 종목코드 가져와 Stock list 생성
    public List<StockDto> getStocks() {
        List<StockDto> stocks = new ArrayList<>();

        for (int i = 1; i < 41; i++) {
            Connection conn = Jsoup.connect(STOCK_URL_BASE + i);

            try {
                Document document = conn.get();
                Elements stockTableBody = document.select("table.type_2 tbody tr");
                for (Element element : stockTableBody) {
                    if (element.attr("onmouseover").isEmpty()) {
                        continue;
                    }
                    String company = element.select("td").get(1).text();
                    String code = element.select("td").get(12).select(".center a").attr("href").split("code=")[1];
                    stocks.add(new StockDto(company, code));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return stocks;
    }

}
