package housit.housit_backend.service;

import housit.housit_backend.domain.finance.Account;
import housit.housit_backend.domain.finance.AccountTxn;
import housit.housit_backend.domain.finance.TxnType;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.FinanceDto;
import housit.housit_backend.dto.request.AccountSaveDto;
import housit.housit_backend.dto.request.AccountTxnSaveDto;
import housit.housit_backend.repository.FinanceRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<Account> allAccounts = financeRepository.findAllAccounts(room);
        FinanceDto financeDto = new FinanceDto();
        financeDto.setAccounts(allAccounts);
        return financeDto;
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

    public List<AccountTxn> getTxns(Long roomId, Long accountId) {
        Account account = financeRepository.findAccountById(accountId);
        return financeRepository.findAllTxns(account);
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
}
