package aquariux.codingtest.userwallet.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aquariux.codingtest.trade.exception.CurrencyWalletNotFoundException;
import aquariux.codingtest.userwallet.entity.UserWallet;
import aquariux.codingtest.userwallet.repository.UserWalletRepository;

@Service
public class UserWalletService {

    @Autowired
    private UserWalletRepository repository;

//    public UserWallet getUserWalletBalance(long userId, String currency) {
//        return repository.findByUserIdAndCurrency(userId, currency); }

//    public Map<String, Float> getWalletBalances(long userId) {
//        List<UserWallet> wallets = repository.findByUserId(userId);
//        return wallets
//                .stream()
//                .collect(Collectors.toMap(UserWallet::getCurrency, UserWallet::getBalance)); }
    
    public List<UserWallet> getWalletBalances(long userId) {
        return repository.findByUserId(userId);
    }
    
    public UserWallet getWallet(long userId, String currency) throws CurrencyWalletNotFoundException {
        return repository.findByUserIdAndCurrency(userId, currency)
                .orElseThrow(() -> new CurrencyWalletNotFoundException("Wallet not found for currency: " + currency));
    }

    public void updateWalletBalance(UserWallet wallet) {
        repository.save(wallet);
    }
    
//    public void updateWalletBalance(long userId, String ccyPair, String direction, float amount, double totalCost)
//            throws InsufficientBalanceException {
//        // Parse base currency and quote currency
//        String baseCurrency = ccyPair.substring(0, 3); // e.g., "ETH" from "ETHUSDT"
//        String quoteCurrency = ccyPair.substring(3);   // e.g., "USDT" from "ETHUSDT"
//
//        List<UserWallet> wallets = repository.findByUserId(userId);
//        
//        // Find the relevant wallets for the base and quote currencies
//        UserWallet baseWallet = wallets.stream()
//                .filter(w -> w.getCurrency().equalsIgnoreCase(baseCurrency))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("No wallet found for currency: " + baseCurrency));
//        UserWallet quoteWallet = wallets.stream()
//                .filter(w -> w.getCurrency().equalsIgnoreCase(quoteCurrency))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("No wallet found for currency: " + quoteCurrency));
//
//        
//        if(direction.equalsIgnoreCase("BUY")) {
//            if (quoteWallet.getBalance() < totalCost) {
//                throw new InsufficientBalanceException("Insufficient " + quoteCurrency + " balance");
//            }
//            // Deduct from quote wallet and add to base wallet
//            quoteWallet.setBalance((float) (quoteWallet.getBalance() - totalCost));
//            baseWallet.setBalance(baseWallet.getBalance() + amount);
//        } else if (direction.equalsIgnoreCase("SELL")) {
//            if (baseWallet.getBalance() < amount) {
//                throw new InsufficientBalanceException("Insufficient " + baseCurrency + " balance");
//            }
//            // Deduct from base wallet and add to quote wallet
//            baseWallet.setBalance(baseWallet.getBalance() - amount);
//            quoteWallet.setBalance(quoteWallet.getBalance() + totalCost);
//        }
//
//        // Save the updated wallets
//        repository.save(baseWallet);
//        repository.save(quoteWallet);
//
//        repository.updateBalance(userId, currency, amount);
//    }
}
