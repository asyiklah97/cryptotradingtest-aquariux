package aquariux.codingtest.price.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

//@Data
@Entity
//@IdClass(AggregatedTickerPriceId.class)
@Table(name = "best_price")
public class AggregatedTickerPrice {
    private @Id String ccyPair;
    private Date date;
    private float askPrice;
    private float bidPrice;

    public AggregatedTickerPrice() {
    }

    public AggregatedTickerPrice(String ccyPair, float askPrice, float bidPrice, Date date) {
        super();
        this.ccyPair = ccyPair;
        this.date = date;
        this.askPrice = askPrice;
        this.bidPrice = bidPrice;
    }

    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    public float getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(float askPrice) {
        this.askPrice = askPrice;
    }

    public float getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(float bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date priceDate) {
        this.date = priceDate;
    }

}
