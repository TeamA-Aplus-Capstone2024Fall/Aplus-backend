package housit.housit_backend.repository;

import housit.housit_backend.domain.finance.Account;
import housit.housit_backend.domain.finance.AccountTxn;
import housit.housit_backend.domain.room.Room;

import java.util.List;

public interface FinanceRepository {
    void saveAccount(Account account);
    void saveTxn(AccountTxn accountTxn);

    Account findAccountById(Long accountId);
    AccountTxn findTxnById(Long txnId);

    void deleteAccount(Account account);
    void deleteTxn(AccountTxn accountTxn);

    List<Account> findAllAccounts(Room room);
    List<AccountTxn> findAllTxns(Account account);
}
