package aquariux.codingtest.price.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class AggregatedTickerPriceId implements Serializable {

    private static final long serialVersionUID = 7887566426214065569L;

    private String ccyPair;
    private Date date;

    public AggregatedTickerPriceId() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(ccyPair, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AggregatedTickerPriceId other = (AggregatedTickerPriceId) obj;
        return Objects.equals(ccyPair, other.ccyPair) && Objects.equals(date, other.date);
    }
}
