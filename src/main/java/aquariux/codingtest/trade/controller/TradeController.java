package aquariux.codingtest.trade.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aquariux.codingtest.trade.entity.TradeData;
import aquariux.codingtest.trade.exception.CurrencyWalletNotFoundException;
import aquariux.codingtest.trade.exception.InsufficientBalanceException;
import aquariux.codingtest.trade.exception.InvalidTradeException;
import aquariux.codingtest.trade.model.TradeRequest;
import aquariux.codingtest.trade.model.TradeResponse;
import aquariux.codingtest.trade.service.TradeService;

@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private TradeService service;

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<TradeData>> retrieveTradeHistory(@PathVariable long userId) {
        List<TradeData> tradeData = service.retrieveTradeData(userId);
        return ResponseEntity.ok(tradeData);
    }

    @PostMapping("/execute")
    public ResponseEntity<Object> saveTrade(@RequestBody TradeRequest tradeReq) {
        try {
            TradeResponse traderesp = service.executeTrade(tradeReq);
//            URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{userid}")
//                .buildAndExpand(tradeReq.getUserId())
//                .toUri();
//            return ResponseEntity.created(location).build(); // HTTP 201
            return ResponseEntity.ok(traderesp.getMessage()); // HTTP 200
        } catch (InsufficientBalanceException e) {
            e.printStackTrace();
//            return ResponseEntity.notFound().build(); // HTTP 404 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", HttpStatus.BAD_REQUEST.value(), "error", "Insufficient Balance", "message",
                            e.getMessage())); // HTTP 400
        } catch (InvalidTradeException e) {
            e.printStackTrace();
//            return ResponseEntity.notFound().build(); // HTTP 404
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", HttpStatus.BAD_REQUEST.value(), "error", "Invalid Trade", "message",
                            e.getMessage())); // HTTP 400
        } catch (CurrencyWalletNotFoundException e) {
            e.printStackTrace();
//            return ResponseEntity.notFound().build(); // HTTP 404 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", HttpStatus.BAD_REQUEST.value(), "error", "Currency wallet not found",
                            "message", e.getMessage())); // HTTP 400
        }
    }
}
