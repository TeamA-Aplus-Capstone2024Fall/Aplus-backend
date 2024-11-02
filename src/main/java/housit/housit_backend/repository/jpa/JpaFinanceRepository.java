package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.finance.*;
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
    public List<AccountTxn> findAllTxnsByYearMonth(Account account, Integer year, Integer month) {
        return em.createQuery("select at from AccountTxn at where at.account =: account " +
                        "and year(at.txnDate) =: year and month(at.txnDate) =: month " +
                        "order by at.txnDate desc", AccountTxn.class)
                .setParameter("account", account)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    @Override
    public List<AccountTxn> findAllTxnsByYearMonthWithType(Account account, Integer year, Integer month, TxnType type) {
        return em.createQuery("select at from AccountTxn at where at.account =: account " +
                        "and year(at.txnDate) =: year and month(at.txnDate) =: month " +
                        "and at.txnType =: type " +
                        "order by at.txnDate desc", AccountTxn.class)
                .setParameter("account", account)
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("type", type)
                .getResultList();
    }

    @Override
    public void saveFinancePlan(FinancePlan financePlan) {
        em.persist(financePlan);
    }

    @Override
    public List<PredictedIncome> findAllPredictedIncomesByDate(Room room, Integer year, Integer month) {
        return em.createQuery("select pi from PredictedIncome pi where pi.room =: room " +
                        "and year(pi.enrolledDate) =: year and month(pi.enrolledDate) =: month", PredictedIncome.class)
                .setParameter("room", room)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    @Override
    public List<PredictedExpense> findAllPredictedExpensesByDate(Room room, Integer year, Integer month) {
        return em.createQuery("select pe from PredictedExpense pe where pe.room =: room " +
                        "and year(pe.enrolledDate) =: year and month(pe.enrolledDate) =: month", PredictedExpense.class)
                .setParameter("room", room)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    @Override
    public List<SavingGoal> findAllSavingGoalsByDate(Room room, Integer year, Integer month) {
        return em.createQuery("select sg from SavingGoal sg where sg.room =: room " +
                        "and year(sg.enrolledDate) =: year and month(sg.enrolledDate) =: month", SavingGoal.class)
                .setParameter("room", room)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
    }

    @Override
    public Long findTotalSumByDate(List<Account> allAccounts, Room room, Integer year, Integer month, TxnType txnType) {
        Long amount = em.createQuery("select sum(at.amount) from AccountTxn at where at.account in :accounts " +
                        "and year(at.txnDate) =: year and month(at.txnDate) =: month " +
                        "and at.txnType =: type", Long.class)
                .setParameter("accounts", allAccounts)
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("type", txnType)
                .getSingleResult();
        if(amount == null) amount = 0L;
        return amount;
    }

    @Override
    public PredictedIncome findPredictedIncomeById(Long financePlanId) {
        return em.find(PredictedIncome.class, financePlanId);
    }

    @Override
    public void deleteFinancePlan(FinancePlan financePlan) {
        em.remove(financePlan);
    }
}
