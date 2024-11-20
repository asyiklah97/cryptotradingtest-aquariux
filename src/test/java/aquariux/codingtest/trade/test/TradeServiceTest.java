package aquariux.codingtest.trade.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import aquariux.codingtest.price.entity.AggregatedTickerPrice;
import aquariux.codingtest.price.service.PriceService;
import aquariux.codingtest.trade.entity.TradeData;
import aquariux.codingtest.trade.exception.CurrencyWalletNotFoundException;
import aquariux.codingtest.trade.exception.InsufficientBalanceException;
import aquariux.codingtest.trade.exception.InvalidTradeException;
import aquariux.codingtest.trade.model.TradeRequest;
import aquariux.codingtest.trade.repository.TradeRepository;
import aquariux.codingtest.trade.service.TradeService;
import aquariux.codingtest.userwallet.entity.UserWallet;
import aquariux.codingtest.userwallet.service.UserWalletService;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {
    @Mock
    private UserWalletService walletService;

    @Mock
    private PriceService priceService;

    @Mock
    private TradeRepository transactionRepository;

    @InjectMocks
    private TradeService tradeService;

    @Captor
    private ArgumentCaptor<UserWallet> walletCaptor;

    @Captor
    private ArgumentCaptor<TradeData> transactionCaptor;

    @Test
    void testExecuteTrade_BuySuccess()
            throws CurrencyWalletNotFoundException, InvalidTradeException, InsufficientBalanceException {
        // Arrange
        TradeRequest request = new TradeRequest();
        request.setUserId(1L);
        request.setCcyPair("ETHUSDT");
        request.setType("BUY");
        request.setAmount(2);

        AggregatedTickerPrice aggregatedPrice = new AggregatedTickerPrice();
        aggregatedPrice.setCcyPair("ETHUSDT");
        aggregatedPrice.setAskPrice(1500);

        UserWallet usdtWallet = new UserWallet();
        usdtWallet.setUserId(1L);
        usdtWallet.setCurrency("USDT");
        usdtWallet.setBalance(5000);

        UserWallet ethWallet = new UserWallet();
        ethWallet.setUserId(1L);
        ethWallet.setCurrency("ETH");
        ethWallet.setBalance(0);

        when(priceService.getPriceByPair("ETHUSDT")).thenReturn(Optional.of(aggregatedPrice));
        when(walletService.getWallet(1L, "USDT")).thenReturn(usdtWallet);
        when(walletService.getWallet(1L, "ETH")).thenReturn(ethWallet);

        // Act
        tradeService.executeTrade(request);

        // Assert : check wallet updates
        verify(walletService, times(2)).updateWalletBalance(walletCaptor.capture());
        List<UserWallet> updatedWallets = walletCaptor.getAllValues();

        UserWallet updatedUSDTWallet = updatedWallets.stream()
                .filter(wallet -> "USDT".equals(wallet.getCurrency()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("USDT wallet not updated"));

        UserWallet updatedETHWallet = updatedWallets.stream()
                .filter(wallet -> "ETH".equals(wallet.getCurrency()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("ETH wallet not updated"));

        assertEquals(2000f, usdtWallet.getBalance());
        assertEquals(2f, ethWallet.getBalance());

        // Assert: Check transaction saving
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());
        TradeData savedTransaction = transactionCaptor.getValue();

        assertEquals(1L, savedTransaction.getUserId());
        assertEquals("ETHUSDT", savedTransaction.getCcyPair());
        assertEquals("BUY", savedTransaction.getDirection());
        assertEquals(1500f, savedTransaction.getPrice());
        assertEquals(2f, savedTransaction.getAmount());
    }

    @Test
    void testExecuteTrade_InsufficientBalance() throws CurrencyWalletNotFoundException {
        // Arrange
        TradeRequest request = new TradeRequest();
        request.setUserId(1L);
        request.setCcyPair("BTCUSDT");
        request.setType("BUY");
        request.setAmount(1f);

        AggregatedTickerPrice aggregatedPrice = new AggregatedTickerPrice();
        aggregatedPrice.setCcyPair("BTCUSDT");
        aggregatedPrice.setAskPrice(30000);

        UserWallet usdtWallet = new UserWallet();
        usdtWallet.setUserId(1L);
        usdtWallet.setCurrency("USDT");
        usdtWallet.setBalance(1000);

        when(priceService.getPriceByPair("BTCUSDT")).thenReturn(Optional.of(aggregatedPrice));
        when(walletService.getWallet(1L, "USDT")).thenReturn(usdtWallet);

        // Act & Assert
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            tradeService.executeTrade(request);
        });
        assertEquals("Insufficient balance of currency USDT", exception.getMessage());

        verify(walletService, times(0)).updateWalletBalance(Mockito.<UserWallet>any());
        verify(transactionRepository, times(0)).save(Mockito.<TradeData>any());
    }

    @Test
    void testExecuteTrade_PriceNotAvailable() {
        // Arrange
        TradeRequest request = new TradeRequest();
        request.setUserId(1L);
        request.setCcyPair("ETHUSDT");
        request.setType("BUY");
        request.setAmount(1f);

        when(priceService.getPriceByPair("ETHUSDT")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(InvalidTradeException.class, () -> tradeService.executeTrade(request));
        assertEquals("Invalid or unsupported trading pair: ETHUSDT", exception.getMessage());

        verify(walletService, times(0)).updateWalletBalance(Mockito.<UserWallet>any());
        verify(transactionRepository, times(0)).save(Mockito.<TradeData>any());
    }
}
