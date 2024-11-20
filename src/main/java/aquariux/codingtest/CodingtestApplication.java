package aquariux.codingtest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import aquariux.codingtest.consumer.ApiConsumerService;

@SpringBootApplication
public class CodingtestApplication {

    public static void main(String[] args) throws InterruptedException {
//        while (true) {
//            SpringApplication.run(CodingtestApplication.class, args);
//            Thread.sleep(10000);
//        }
    }

//    @Bean
//    public CommandLineRunner run(ApiConsumerService apiConsumerService) {
//        return args -> {
//            apiConsumerService.consumeApi();
//        };
//    }
}
