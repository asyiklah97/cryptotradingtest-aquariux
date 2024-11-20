package aquariux.codingtest.trade.model;

public class TradeRequest {
    private long userId;
    private String ccyPair;
    private float amount;
    private String type; // BUY or SELL

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccypair) {
        this.ccyPair = ccypair;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
