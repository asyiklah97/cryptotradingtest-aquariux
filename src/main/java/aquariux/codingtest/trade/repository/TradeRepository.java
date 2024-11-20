package aquariux.codingtest.trade.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import aquariux.codingtest.trade.entity.TradeData;

public interface TradeRepository extends JpaRepository<TradeData, Long> {

    public List<TradeData> findByUserId(long userId);
}
