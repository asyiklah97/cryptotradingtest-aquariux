package aquariux.codingtest.trade.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class TradeDataId implements Serializable {
    private long userId;
    private Date tradeDate;

    public TradeDataId() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeDate, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TradeDataId other = (TradeDataId) obj;
        return Objects.equals(tradeDate, other.tradeDate) && userId == other.userId;
    }
}
