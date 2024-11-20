package aquariux.codingtest.trade.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import aquariux.codingtest.price.entity.AggregatedTickerPrice;
import aquariux.codingtest.price.repository.PriceRepository;
import aquariux.codingtest.trade.model.TradeRequest;
import aquariux.codingtest.trade.repository.TradeRepository;
import aquariux.codingtest.userwallet.entity.UserWallet;
import aquariux.codingtest.userwallet.repository.UserWalletRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserWalletRepository walletRepository;

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private TradeRepository transactionRepository;

    @BeforeEach
    void setup() {
        walletRepository.deleteAll();
        priceRepository.deleteAll();
        transactionRepository.deleteAll();

        // Add sample data
        walletRepository.save(new UserWallet(1L, "USDT", 50000));
        walletRepository.save(new UserWallet(1L, "BTC", 0));
        walletRepository.save(new UserWallet(1L, "ETH", 0));

        priceRepository
                .save(new AggregatedTickerPrice("ETHUSDT", (1500f), (1510f), new Date(System.currentTimeMillis())));
    }

    @Test
    void testGetPrices() throws Exception {
        mockMvc.perform(get("/price/bestprice/" + "ETHUSDT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4)) // 4: ccypair, date, askPrice, bidPrice
                .andExpect(jsonPath("$[0].ccyPair").value("ETHUSDT"))
                .andExpect(jsonPath("$[0].askPrice").value(1510));
    }

    @Test
    void testTrade_BuySuccess() throws Exception {
        TradeRequest request = new TradeRequest();
        request.setUserId(1L);
        request.setCcyPair("ETHUSDT");
        request.setType("BUY");
        request.setAmount(2f);

        mockMvc.perform(post("/trade/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Trade executed successfully"));

        UserWallet usdtWallet = walletRepository.findByUserIdAndCurrency(1L, "USDT").get();
        UserWallet ethWallet = walletRepository.findByUserIdAndCurrency(1L, "ETH").get();

        assertEquals(47000, usdtWallet.getBalance());
        assertEquals(2, ethWallet.getBalance());
    }
}
