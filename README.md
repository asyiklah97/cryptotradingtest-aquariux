# cryptotradingtest-aquariux
1. This project uses Maven and Spring boot.
2. The architecture is monolith, but clear separation of Controller-Service-Repository layers are provided for future migration to microservices architecture.
3. Unit tests and integration tests are provided (using JUnit, Mockito). Run mvn test. 

Known error:
1. Integration Test in TradeControllerTest.testGetPrices() fails on line 64: No value at JSON path "$[0].ccyPair". I could not manage to finish debugging this, although it seems to me that there's no mistake in the code. Apologies for this. This should be fixed in the future. 

Further improvements:
1. I didn't do it in microservice architecture, but in monolith, as microservice architecture is more complicated and I didn't have enough time. But the monolith is sufficient to show the trading application and its corresponding logic. 
2. More robust Exception handling. 
