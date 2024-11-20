package aquariux.codingtest.trade.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(TradeDataId.class)
@Table(name = "user_trade")
public class TradeData {
    private @Id long userId;
    private @Id Date tradeDate;
    private String ccyPair;
    private String direction;
    private float price;
//    private int quantity;
    private float amount;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
