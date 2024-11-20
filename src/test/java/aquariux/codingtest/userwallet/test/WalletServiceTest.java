package aquariux.codingtest.userwallet.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import aquariux.codingtest.userwallet.entity.UserWallet;
import aquariux.codingtest.userwallet.repository.UserWalletRepository;
import aquariux.codingtest.userwallet.service.UserWalletService;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    private UserWalletRepository walletRepository;

    @InjectMocks
    private UserWalletService walletService;

    @Test
    void testGetWalletBalances() {
        // Arrange
        List<UserWallet> wallets = List.of(
                new UserWallet(1L, "USDT", 50000),
                new UserWallet(1L, "BTC", 0)
        );
        when(walletRepository.findByUserId(1L)).thenReturn(wallets);

        // Act
        List<UserWallet> result = walletService.getWalletBalances(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals("USDT", result.get(0).getCurrency());
        assertEquals(50000, result.get(0).getBalance());
    }
}
