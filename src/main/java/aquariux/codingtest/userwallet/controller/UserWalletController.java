package aquariux.codingtest.userwallet.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aquariux.codingtest.userwallet.entity.UserWallet;
import aquariux.codingtest.userwallet.service.UserWalletService;

@RestController
@RequestMapping("/userwallet")
public class UserWalletController {

    @Autowired
    private UserWalletService service;

//    @GetMapping("/{currency}/{userId}")
//    public UserWallet getUserWalletBalance(long userId, String currency) {
//        return service.getUserWalletBalance(userId, currency); }

//    @GetMapping("/{userId}")
//    public ResponseEntity<Map<String, Float>> getWalletBalances(@PathVariable long userId) {
//        Map<String, Float> walletBalance = service.getWalletBalances(userId);
//        return ResponseEntity.ok(walletBalance); }

    @GetMapping("/wallet/{userId}")
    public ResponseEntity<List<UserWallet>> getWallet(@PathVariable long userId) {
        List<UserWallet> wallets = service.getWalletBalances(userId);
        return ResponseEntity.ok(wallets);
    }
    
    @PutMapping("/update")
    public void updateBalance(@RequestBody UserWallet wallet) {
//        service.updateWalletBalance(wallet.getUserId(), wallet.getCurrency(), wallet.getBalance());
        service.updateWalletBalance(wallet);
    }
}
