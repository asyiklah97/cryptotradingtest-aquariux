package aquariux.codingtest.consumer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import aquariux.codingtest.price.entity.AggregatedTickerPrice;

public class ApiConsumerService {
    private final RestTemplate restTemplate;

    public ApiConsumerService() {
        this.restTemplate = new RestTemplate();
    }

    public void consumeApi() {
        String retrieveBestPriceApiUrlEthUsdt = "http://localhost:8080/price/bestprice/ETHUSDT";
        String retrieveBestPriceApiUrlBtcUsdt = "http://localhost:8080/price/bestprice/BTCUSDT";
        String saveBestPriceApiUrlEthUsdt = "http://localhost:8080/price/save/ETHUSDT";
        String saveBestPriceApiUrlBtcUsdt = "http://localhost:8080/price/save/BTCUSDT";

        // Send GET request
        ResponseEntity<AggregatedTickerPrice> responseRetrieveEthUsdt = restTemplate
                .getForEntity(retrieveBestPriceApiUrlEthUsdt, AggregatedTickerPrice.class);
        ResponseEntity<AggregatedTickerPrice> responseRetrieveBtcUsdt = restTemplate
                .getForEntity(retrieveBestPriceApiUrlBtcUsdt, AggregatedTickerPrice.class);

        // Send POST request
        ResponseEntity<AggregatedTickerPrice> responseSaveEthUsdt = restTemplate.postForEntity(
                saveBestPriceApiUrlEthUsdt, responseRetrieveEthUsdt.getBody(), AggregatedTickerPrice.class);
        ResponseEntity<AggregatedTickerPrice> responseSaveBtcUsdt = restTemplate.postForEntity(
                saveBestPriceApiUrlBtcUsdt, responseRetrieveBtcUsdt.getBody(), AggregatedTickerPrice.class);

        // Print HTTP status and body
//        System.out.println("HTTP Status: " + response.getStatusCode());
//        System.out.println("Response Body: " + response.getBody());
    }

}
