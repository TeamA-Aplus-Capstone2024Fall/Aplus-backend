package housit.housit_backend.repository;

import housit.housit_backend.domain.finance.*;
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
    List<AccountTxn> findAllTxnsByYearMonth(Account account, Integer year, Integer month);
    List<AccountTxn> findAllTxnsByYearMonthWithType(Account account, Integer year, Integer month, TxnType type);

    void saveFinancePlan(FinancePlan financePlan);

    List<PredictedIncome> findAllPredictedIncomesByDate(Room room, Integer year, Integer month);

    List<PredictedExpense> findAllPredictedExpensesByDate(Room room, Integer year, Integer month);

    List<SavingGoal> findAllSavingGoalsByDate(Room room, Integer year, Integer month);

    Long findTotalSumByDate(List<Account> allAccounts, Room room, Integer year, Integer month, TxnType txnType);

    FinancePlan findFinancePlanById(Long financePlanId);
}
