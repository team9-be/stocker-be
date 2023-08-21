package com.project.stocker.util;

import com.project.stocker.dto.request.TradeDto;
import com.project.stocker.entity.Stock;
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
public class JsoupCrawling {

    private final String STOCK_URL_BASE = "https://finance.naver.com/sise/sise_market_sum.nhn?";
    private final String TRADE_URL_BASE = "https://finance.naver.com/item/sise_time.naver?";

    // 크롤링을 통해 주식명, 종목코드 가져와 Stock list 생성
    public List<Stock> getStocks() {
        List<Stock> stocks = new ArrayList<>();

        for (int i = 1; i < 41; i++) {
            Connection conn = Jsoup.connect(STOCK_URL_BASE + "&page=" + i);

            try {
                Document document = conn.get();
                Elements elements = document.select("table.type_2 tbody tr");
                for (Element element : elements) {
                    if (element.attr("onmouseover").isEmpty()) {
                        continue;
                    }
                    String company = element.select("td").get(1).text();
                    String code = element.select("td").get(12).select(".center a").attr("href").split("code=")[1];
                    stocks.add(new Stock(company, code));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return stocks;
    }

    public List<TradeDto> getTrades(List<Stock> stocks) {
        List<TradeDto> trades = new ArrayList<>();
        for (Stock stock : stocks) {
//            if(stock.getId()>10){
//                break;
//            }

            for (int i = 1; i < 41; i++) {
                Connection conn = Jsoup.connect(TRADE_URL_BASE + "code=" + stock.getCode() + "&thistime=20230818182200&page=" + i);

                try {
                    Document document = conn.get();
                    Elements elements = document.select("table.type2 tbody tr");

                    for (Element element : elements) {
                        if (element.attr("onmouseover").isEmpty()) {
                            continue;
                        }
                        String price = element.select("td").get(1).text().replaceAll(",", "");
                        String quantity = element.select("td").get(6).text().replaceAll(",", "");

                        if (!price.equals("") && !quantity.equals("")) {
                            trades.add(new TradeDto(Long.parseLong(quantity), Long.parseLong(price), stock));
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return trades;
    }

}
