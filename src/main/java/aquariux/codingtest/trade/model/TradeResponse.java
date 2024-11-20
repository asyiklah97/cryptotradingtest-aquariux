package aquariux.codingtest.trade.model;

public class TradeResponse {
    private String message;
    private double executedPrice;
    private double totalCost;

    public TradeResponse(String message, double executedPrice, double totalCost) {
        super();
        this.message = message;
        this.executedPrice = executedPrice;
        this.totalCost = totalCost;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(double executedPrice) {
        this.executedPrice = executedPrice;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

}
