package aquariux.codingtest.price.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aquariux.codingtest.price.entity.AggregatedTickerPrice;
import aquariux.codingtest.price.model.HuobiResponse;
import aquariux.codingtest.price.model.TickerPrice;
import aquariux.codingtest.price.service.PriceService;

@RestController
@RequestMapping("/price")
public class PriceController {

    @Autowired
    private PriceService service;

//    @GetMapping("/ticker/binance/{symbol}")
//    public TickerPrice getBinanceTickerPrice(@PathVariable String symbol) {
//        return service.getBinanceTickerPrice(symbol); }

//    @GetMapping("/ticker/houbi/{symbol}")
//    public Optional<HuobiResponse.TickerData> getHoubiTickerData(@PathVariable String symbol) {
//        return service.getHoubiTickerData(symbol); }

//    @PostMapping("/bestprice/save")
//    public void retrieveAndSaveBestPrices(@RequestBody AggregatedTickerPrice price) {
//        service.retrieveAndSaveBestPrices(price.getCcyPair()); }

    @GetMapping("/bestprice/{ccypair}")
    public ResponseEntity<Object> getBestPriceByCcyPair(@PathVariable String ccypair) {
        try {
            Optional<AggregatedTickerPrice> pricing = service.getPriceByPair(ccypair.toUpperCase());
            return pricing
                    .<ResponseEntity<Object>>map(ResponseEntity::ok) // HTTP 200
                    .orElseGet(() -> ResponseEntity
                                        .status(HttpStatus.NOT_FOUND) // HTTP 404
                                        .body(Map.of(
                                                "status", HttpStatus.NOT_FOUND.value(),
                                                "error", "Not Found",
                                                "message", "No pricing found for pair: " + ccypair
                    )));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // HTTP 500
                    .body(Map.of(
                            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error", "Internal Server Error",
                            "message", "An error occurred while fetching pricing for pair: " + ccypair
            ));
        }
    }

    @PostMapping("/save") // TODO not necessary
    public ResponseEntity<?> saveBestPrice(@RequestBody AggregatedTickerPrice bestPrice) {
        try {
            service.saveBestPrice(bestPrice.getCcyPair(), bestPrice.getAskPrice(), bestPrice.getBidPrice());

            // HTTP 200
            return ResponseEntity.ok("Price updates successfully for ccy pair: " + bestPrice.getCcyPair());

        } catch (Exception e) {
            // HTTP 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating price: " + e.getMessage());
        }
    }
}
