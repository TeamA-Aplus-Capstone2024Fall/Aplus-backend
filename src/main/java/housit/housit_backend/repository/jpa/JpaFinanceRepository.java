package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.finance.Account;
import housit.housit_backend.domain.finance.AccountTxn;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.repository.FinanceRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaFinanceRepository implements FinanceRepository {
    private final EntityManager em;

    @Override
    public void saveAccount(Account account) {
        em.persist(account);
    }

    @Override
    public void saveTxn(AccountTxn accountTxn) {
        em.persist(accountTxn);
    }

    @Override
    public Account findAccountById(Long accountId) {
        return em.find(Account.class, accountId);
    }

    @Override
    public AccountTxn findTxnById(Long txnId) {
        return em.find(AccountTxn.class, txnId);
    }

    @Override
    public void deleteAccount(Account account) {
        em.remove(account);
    }

    @Override
    public void deleteTxn(AccountTxn accountTxn) {
        em.remove(accountTxn);
    }

    @Override
    public List<Account> findAllAccounts(Room room) {
        return em.createQuery("select a from Account a where a.room =: room", Account.class)
                .setParameter("room", room)
                .getResultList();
    }

    @Override
    public List<AccountTxn> findAllTxns(Account account) {
        return em.createQuery("select at from AccountTxn at where at.account =: account " +
                        "order by at.txnDate", AccountTxn.class)
                .setParameter("account", account)
                .getResultList();
    }
}
