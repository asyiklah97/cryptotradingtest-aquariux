package aquariux.codingtest.userwallet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import aquariux.codingtest.userwallet.entity.UserWallet;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {
    
//    @Query("select * from USER_WALLET where user_id=?1 and currency=?2")
    public Optional<UserWallet> findByUserIdAndCurrency(long userId, String currency);

    public List<UserWallet> findByUserId(long userId);

//    @Query("update USER_WALLET set balance=?3 where userId=?1 and currency=?2")
//    public void updateBalance(long userId, String currency, float balance);
}
