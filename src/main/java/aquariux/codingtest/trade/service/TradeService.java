package aquariux.codingtest.trade.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aquariux.codingtest.price.entity.AggregatedTickerPrice;
import aquariux.codingtest.price.service.PriceService;
import aquariux.codingtest.trade.entity.TradeData;
import aquariux.codingtest.trade.exception.CurrencyWalletNotFoundException;
import aquariux.codingtest.trade.exception.InsufficientBalanceException;
import aquariux.codingtest.trade.exception.InvalidTradeException;
import aquariux.codingtest.trade.model.TradeRequest;
import aquariux.codingtest.trade.model.TradeResponse;
import aquariux.codingtest.trade.repository.TradeRepository;
import aquariux.codingtest.userwallet.entity.UserWallet;
import aquariux.codingtest.userwallet.service.UserWalletService;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private PriceService pricingService;
    
    @Autowired
    private UserWalletService walletService;
    
    public List<TradeData> retrieveTradeData(long userId) {
        return tradeRepository.findByUserId(userId);
    }
    
//    public void trade(TradeRequest tradeRequest) throws InsufficientBalanceException {
//        
//        // retrieve user wallet from DB 
//        String url = "http://localhost:8080/userwallet/" + tradeRequest.getUserId() + "/"
//                + (tradeRequest.isBuySell() ? tradeRequest.getCcyPair().substring(3)
//                        : tradeRequest.getCcyPair().substring(0, 3));
//        ResponseEntity<UserWallet> responseUserWallet = restTemplate.getForEntity(url, UserWallet.class);
//        UserWallet wallet = responseUserWallet.getBody();
//        
//        if (wallet.getBalance() < tradeRequest.getAmount()) {
//            // return HTTP 204 ? 
//            throw new InsufficientBalanceException(); 
//        }
//        wallet.setBalance(wallet.getBalance() - tradeRequest.getAmount());
//        
//        url = "http://localhost:8080/userwallet/update";
//        restTemplate.put(url, wallet);
//
////        repository.save(new TradeData(userId, ccyPair, amount));
//        tradeRepository.save(tradeRequest);
//    }

    public TradeResponse executeTrade(TradeRequest tradeRequest)
            throws InvalidTradeException, InsufficientBalanceException, CurrencyWalletNotFoundException {
        String ccyPair = tradeRequest.getCcyPair().toUpperCase();
        String baseCurrency = ccyPair.substring(0, 3); // e.g., "ETH" from "ETHUSDT"
        String quoteCurrency = ccyPair.substring(3);   // e.g., "USDT" from "ETHUSDT"

        AggregatedTickerPrice latestPrice = pricingService.getPriceByPair(ccyPair)
                .orElseThrow(() -> new InvalidTradeException("Invalid or unsupported trading pair: " + ccyPair));

        float price = tradeRequest.getType().equalsIgnoreCase("BUY") ? 
                        latestPrice.getAskPrice() : 
                        latestPrice.getBidPrice();
        double totalCost = tradeRequest.getAmount() * price;
        
        if (tradeRequest.getType().equalsIgnoreCase("BUY")) {
            UserWallet quoteWallet = walletService.getWallet(tradeRequest.getUserId(), quoteCurrency);
//                    .orElseThrow(() -> new RuntimeException("USDT wallet not found"));
            if (quoteWallet.getBalance() < totalCost)
                throw new InsufficientBalanceException("Insufficient balance of currency " + quoteCurrency);
            quoteWallet.setBalance((float) (quoteWallet.getBalance() - totalCost));
            walletService.updateWalletBalance(quoteWallet);
            
            // Credit crypto to wallet
            UserWallet cryptoWallet = walletService.getWallet(tradeRequest.getUserId(), baseCurrency);
            cryptoWallet.setBalance(cryptoWallet.getBalance() + tradeRequest.getAmount());
            walletService.updateWalletBalance(cryptoWallet);

        } else {
            UserWallet cryptoWallet = walletService.getWallet(tradeRequest.getUserId(), baseCurrency);
//                    .orElseTh?row(() -> new RuntimeException("Crypto wallet not found"));
            if (cryptoWallet.getBalance() < tradeRequest.getAmount())
                throw new InsufficientBalanceException("Insufficient crypto balance");
            cryptoWallet.setBalance(cryptoWallet.getBalance() - tradeRequest.getAmount());
            walletService.updateWalletBalance(cryptoWallet);
            
            // Credit quoteCurrency to wallet
            UserWallet quoteWallet = walletService.getWallet(tradeRequest.getUserId(), quoteCurrency);
            quoteWallet.setBalance((float) (quoteWallet.getBalance() + totalCost));
            walletService.updateWalletBalance(quoteWallet);
        }
        
//        walletService.updateWalletBalance(tradeRequest.getUserId(), ccyPair, tradeRequest.getDirection(), tradeRequest.getAmount(), totalCost);

        // Log the trade
        TradeData trade = new TradeData();
        trade.setUserId(tradeRequest.getUserId());
        trade.setCcyPair(ccyPair);
        trade.setAmount(tradeRequest.getAmount());
        trade.setDirection(tradeRequest.getType());
        trade.setPrice(price);
        trade.setTradeDate(new Date(System.currentTimeMillis()));
        tradeRepository.save(trade);

        return new TradeResponse("Trade executed successfully", price, totalCost);
    }
}
