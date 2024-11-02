package housit.housit_backend.service;

import housit.housit_backend.domain.finance.*;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.ExpenseDto;
import housit.housit_backend.dto.reponse.FinanceDto;
import housit.housit_backend.dto.reponse.IncomeDto;
import housit.housit_backend.dto.request.AccountSaveDto;
import housit.housit_backend.dto.request.AccountTxnSaveDto;
import housit.housit_backend.dto.request.FinancePlanSaveDto;
import housit.housit_backend.repository.FinanceRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FinanceService {
    private final FinanceRepository financeRepository;
    private final RoomRepository roomRepository;

    public FinanceDto getFinanceInfo(Long roomId, Integer year, Integer month) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        List<PredictedIncome> predictedIncomes = financeRepository.findAllPredictedIncomesByDate(room, year, month);
        List<PredictedExpense> predictedExpenses = financeRepository.findAllPredictedExpensesByDate(room, year, month);
        List<SavingGoal> savingGoals = financeRepository.findAllSavingGoalsByDate(room, year, month);

        List<Account> allAccounts = financeRepository.findAllAccounts(room);
        Long income = 0L;
        Long expense = 0L;
        income += financeRepository.findTotalSumByDate(allAccounts, room, year, month, TxnType.DEPOSIT);
        expense = financeRepository.findTotalSumByDate(allAccounts, room, year, month, TxnType.WITHDRAWAL);
        return new FinanceDto(allAccounts, income, expense,
                predictedIncomes, savingGoals, predictedExpenses);
    }

    @Transactional
    public Long createAccount(Long roomId, AccountSaveDto accountSaveDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Account account = Account.createAccount(accountSaveDto, room);
        financeRepository.saveAccount(account);
        return account.getAccountId();
    }

    @Transactional
    public Long updateAccount(Long accountId, AccountSaveDto accountSaveDto) {
        Account account = financeRepository.findAccountById(accountId);
        account.update(accountSaveDto);
        return account.getAccountId();
    }

    @Transactional
    public void deleteAccount(Long accountId) {
        financeRepository.deleteAccount(financeRepository.findAccountById(accountId));
    }

    public List<AccountTxn> getTxns(Long roomId, Long accountId, Integer year, Integer month) {
        Account account = financeRepository.findAccountById(accountId);
        return financeRepository.findAllTxnsByYearMonth(account, year, month);
    }

    @Transactional
    public Long createTxn(Long roomId, Long accountId, AccountTxnSaveDto accountTxnSaveDto) {
        TxnType txnType = accountTxnSaveDto.getTxnType(); // TRANSFER 타입 체크
        if(txnType == TxnType.TRANSFER)
            throw new IllegalArgumentException("Transaction type must be DEPOSIT or WITHDRAWAL");

        Account account = financeRepository.findAccountById(accountId);
        AccountTxn accountTxn = AccountTxn.create(accountTxnSaveDto, account);
        financeRepository.saveTxn(accountTxn);
        return accountTxn.getAccountTxnId();
    }

    @Transactional
    public void updateTxn(Long roomId, Long accountTxnId, AccountTxnSaveDto accountTxnSaveDto) {
        AccountTxn accountTxn = financeRepository.findTxnById(accountTxnId);
        accountTxn.update(accountTxnSaveDto);
    }

    @Transactional
    public void deleteTxn(Long accountTxnId) {
        AccountTxn txn = financeRepository.findTxnById(accountTxnId);
        Account account = txn.getAccount();
        txn.delete(account);
        financeRepository.deleteTxn(txn);
    }

    @Transactional
    public Long createTransferTxn(Long roomId, AccountTxnSaveDto accountTxnSaveDto,
                                  Long fromAccountId, Long toAccountId) {
        TxnType txnType = accountTxnSaveDto.getTxnType(); // TRANSFER 타입 체크
        if(txnType != TxnType.TRANSFER)
            throw new IllegalArgumentException("Transaction type must be DEPOSIT or WITHDRAWAL");

        Account fromAccount = financeRepository.findAccountById(fromAccountId);
        Account toAccount = financeRepository.findAccountById(toAccountId);
        AccountTxn fromTxn = AccountTxn.create(accountTxnSaveDto, fromAccount);
        AccountTxn toTxn = AccountTxn.create(accountTxnSaveDto, toAccount);
        financeRepository.saveTxn(fromTxn); // fromTxn 저장
        financeRepository.saveTxn(toTxn); // toTxn 저장
        fromTxn.setFromTxn(toTxn.getAccountTxnId()); // fromTxn 에 toTxnId 저장
        toTxn.setToTxn(fromTxn.getAccountTxnId()); // toTxn 에 fromTxnId 저장
        return fromTxn.getAccountTxnId();
    }

    @Transactional
    public Long updateTransferTxn(Long roomId, Long accountTxnId, AccountTxnSaveDto accountTxnSaveDto,
                                  Long fromAccountId, Long toAccountId) {
        AccountTxn oldTxn = financeRepository.findTxnById(accountTxnId);

        if(oldTxn.getTxnType() != TxnType.TRANSFER)
            throw new IllegalArgumentException("Transaction type must be TRANSFER");

        if(oldTxn.getFromTxnId() != null) {
            AccountTxn fromTxn = financeRepository.findTxnById(oldTxn.getFromTxnId());
            fromTxn.delete(fromTxn.getAccount());
            financeRepository.deleteTxn(fromTxn);
        }

        if(oldTxn.getToTxnId() != null) {
            AccountTxn toTxn = financeRepository.findTxnById(oldTxn.getToTxnId());
            toTxn.delete(toTxn.getAccount());
            financeRepository.deleteTxn(toTxn);
        }

        oldTxn.delete(oldTxn.getAccount());
        financeRepository.deleteTxn(oldTxn);

        return createTransferTxn(roomId, accountTxnSaveDto, fromAccountId, toAccountId);
    }

    public IncomeDto getIncomeTxns(Long roomId, Integer year, Integer month) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        List<Account> allAccounts = financeRepository.findAllAccounts(room);
        List<AccountTxn> allTxns = new ArrayList<>();
        for (Account allAccount : allAccounts) {
            allTxns.addAll(financeRepository.findAllTxnsByYearMonthWithType(allAccount, year, month, TxnType.DEPOSIT));
        }
        return new IncomeDto(allTxns);
    }

    public ExpenseDto getExpenseTxns(Long roomId, Integer year, Integer month) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        List<Account> allAccounts = financeRepository.findAllAccounts(room);
        List<AccountTxn> allTxns = new ArrayList<>();
        for (Account allAccount : allAccounts) {
            allTxns.addAll(financeRepository.findAllTxnsByYearMonthWithType(allAccount, year, month, TxnType.WITHDRAWAL));
        }
        return new ExpenseDto(allTxns);
    }

    @Transactional
    public Long createPredictedIncome(Long roomId, FinancePlanSaveDto financePlanSaveDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        PredictedIncome predictedIncome = PredictedIncome.createPredictedIncome(financePlanSaveDto, room);
        financeRepository.saveFinancePlan(predictedIncome);
        return predictedIncome.getIncomeId();
    }

    @Transactional
    public Long createPredictedExpense(Long roomId, FinancePlanSaveDto financePlanSaveDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        PredictedExpense predictedExpense = PredictedExpense.createPredictedExpense(financePlanSaveDto, room);
        financeRepository.saveFinancePlan(predictedExpense);
        return predictedExpense.getExpenseId();
    }

    @Transactional
    public Long createSavingGoal(Long roomId, FinancePlanSaveDto financePlanSaveDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        SavingGoal savingGoal = SavingGoal.createSavingGoal(financePlanSaveDto, room);
        financeRepository.saveFinancePlan(savingGoal);
        return savingGoal.getSavingGoalId();
    }

    @Transactional
    public Long updatePredictedIncome(Long financePlanId, FinancePlanSaveDto financePlanSaveDto) {
        PredictedIncome predictedIncome = financeRepository.findPredictedIncomeById(financePlanId);
        predictedIncome.update(financePlanSaveDto);
        return predictedIncome.getIncomeId();
    }

    @Transactional
    public void deletePredictedIncome(Long financePlanId) {
        PredictedIncome predictedIncome = financeRepository.findPredictedIncomeById(financePlanId);
        financeRepository.deleteFinancePlan(predictedIncome);
    }
}
