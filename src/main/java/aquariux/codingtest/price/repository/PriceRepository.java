package aquariux.codingtest.price.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aquariux.codingtest.price.entity.AggregatedTickerPrice;

public interface PriceRepository extends JpaRepository<AggregatedTickerPrice, String> {
    Optional<AggregatedTickerPrice> findByCcyPair(String ccyPair);
}
