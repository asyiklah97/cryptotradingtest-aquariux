package aquariux.codingtest.price.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import aquariux.codingtest.price.entity.AggregatedTickerPrice;
import aquariux.codingtest.price.model.HuobiResponse;
import aquariux.codingtest.price.model.TickerPrice;
import aquariux.codingtest.price.repository.PriceRepository;

@Service
public class PriceService {
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    private PriceRepository repository;

    private static final String binanceURL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private static final String houbiURL = "https://api.huobi.pro/market/tickers";

    private TickerPrice getBinanceTickerPrice(String symbol) {
        return restTemplate.getForObject(binanceURL + "?symbol=" + symbol, TickerPrice.class);
    }
    
    private Optional<HuobiResponse.TickerData> getHoubiTickerData(String symbol) {

        // Fetch the response and map to HuobiResponse
        HuobiResponse response = restTemplate.getForObject(houbiURL, HuobiResponse.class);

        if (response != null && "ok".equalsIgnoreCase(response.getStatus())) {
            // Filter the list to find the desired ticker
            return response.getData().stream()
                    .filter(ticker -> symbol.equalsIgnoreCase(ticker.getSymbol()))
                    .findFirst();
        }

        return Optional.empty();
    }

    public Optional<AggregatedTickerPrice> getPriceByPair(String ccypair) {
        return repository.findByCcyPair(ccypair);
    }

    @Scheduled(fixedRate = 10000)
    public void retrieveAndSaveBestPrices() {
        AggregatedTickerPrice bestPriceEthUsdt = retrieveBestPrices("ETHUSDT");
        AggregatedTickerPrice bestPriceBtcUsdt = retrieveBestPrices("BTCUSDT");
        
        saveBestPrice(bestPriceEthUsdt.getCcyPair(), bestPriceEthUsdt.getAskPrice(), bestPriceEthUsdt.getBidPrice());
        saveBestPrice(bestPriceBtcUsdt.getCcyPair(), bestPriceBtcUsdt.getAskPrice(), bestPriceBtcUsdt.getBidPrice());
    }
    
//    public void retrieveAndSaveBestPrices(String symbol) {
//        AggregatedTickerPrice bestPrice = retrieveBestPrices(symbol);
//        saveBestPrice(bestPrice.getCcyPair(), bestPrice.getBidPrice(), bestPrice.getAskPrice()); }
    
    private AggregatedTickerPrice retrieveBestPrices(String symbol) {
        AggregatedTickerPrice bestPrice = new AggregatedTickerPrice();
        
        // no need to make API call, as we dont need to decpuple this API with price-retrieving API 
//        String baseUrl = "http://localhost:8080";
//        String binanceApiEndpoint = "/ticker/binance/" + symbol;
//        String houbiApiEndpoint = "/ticker/houbi/" + symbol;
//        ResponseEntity<TickerPrice> responseBinance = restTemplate.getForEntity(binanceApiEndpoint, TickerPrice.class);
//        ResponseEntity<HuobiResponse.TickerData> responseHoubi = restTemplate.getForEntity(houbiApiEndpoint, HuobiResponse.TickerData.class);
        
//        if("204".equals(responseHoubi.getStatusCodeValue())) {
//            return Float.parseFloat(getBinanceTickerPrice(symbol).getAskPrice());
//        }

        TickerPrice binanceBestPrice = getBinanceTickerPrice(symbol);
        Optional<HuobiResponse.TickerData> houbiBestPrice = getHoubiTickerData(symbol);
        
//        float binanceAskPrice = Float.parseFloat(responseBinance.getBody().getAskPrice()); //getBinanceTickerPrice(symbol).getAskPrice());
//        float houbiAskPrice = responseHoubi.getBody().getAsk(); //getHoubiTickerData(symbol).isEmpty()? .getAskPrice());
        bestPrice.setAskPrice(Math.min(Float.parseFloat(binanceBestPrice.getAskPrice()), houbiBestPrice.get().getAsk()));

//        float binanceBidPrice = Float.parseFloat(responseBinance.getBody().getBidPrice()); //getBinanceTickerPrice(symbol).getBidPrice());
//        float houbiBidPrice = responseHoubi.getBody().getBid(); //getHoubiTickerData(symbol).getBidPrice());
        bestPrice.setBidPrice(Math.max(Float.parseFloat(binanceBestPrice.getBidPrice()), houbiBestPrice.get().getBid()));
        
        return bestPrice;
    }
     
    public void saveBestPrice(String ccyPair, float bestAskPrice, float bestBidPrice) {
        AggregatedTickerPrice bestPrice = repository.findByCcyPair(ccyPair).orElse(new AggregatedTickerPrice());
        bestPrice.setDate(new Date(System.currentTimeMillis()));
        repository.save(bestPrice); 
    }
}
