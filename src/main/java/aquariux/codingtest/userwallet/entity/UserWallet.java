package aquariux.codingtest.userwallet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@IdClass(UserWalletId.class)
@Table(name = "user_wallet")
public class UserWallet {
    private @Id long userId;
    private @Id String currency;
    private float balance;

    public UserWallet() {
        super();
    }
    
    public UserWallet(long userId, String currency, float balance) {
        super();
        this.userId = userId;
        this.currency = currency;
        this.balance = balance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
