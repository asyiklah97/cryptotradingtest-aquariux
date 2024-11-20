package aquariux.codingtest.userwallet.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserWalletId implements Serializable {
    private static final long serialVersionUID = 5935101095621517057L;
    
    private long userId;
    private String currency;

    public UserWalletId() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, userId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserWalletId other = (UserWalletId) obj;
        return Objects.equals(currency, other.currency) && userId == other.userId;
    }
}
