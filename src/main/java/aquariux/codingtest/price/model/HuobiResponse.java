package aquariux.codingtest.price.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HuobiResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("ts")
    private long timestamp;

    @JsonProperty("data")
    private List<TickerData> data;

    public static class TickerData {
        private String symbol;
        private float bid;
        private float ask;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public float getBid() {
            return bid;
        }

        public void setBid(float bid) {
            this.bid = bid;
        }

        public float getAsk() {
            return ask;
        }

        public void setAsk(float ask) {
            this.ask = ask;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<TickerData> getData() {
        return data;
    }

    public void setData(List<TickerData> data) {
        this.data = data;
    }
}