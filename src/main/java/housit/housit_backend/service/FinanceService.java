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
    public Long createTxn(Long roomId, Long accountId, AccountTxnSaveDto accountTxnSaveDto) {
        TxnType txnType = accountTxnSaveDto.getTxnType();
        if(txnType == TxnType.TRANSFER) {
            Account fromAccount = financeRepository.findAccountById(accountTxnSaveDto.getFromAccountId());
            Account toAccount = financeRepository.findAccountById(accountTxnSaveDto.getToAccountId());
            AccountTxn fromTxn = AccountTxn.create(accountTxnSaveDto, fromAccount);
            AccountTxn toTxn = AccountTxn.create(accountTxnSaveDto, toAccount);
            financeRepository.saveTxn(fromTxn);
            financeRepository.saveTxn(toTxn);
            fromTxn.setFromTxn(toTxn.getAccountTxnId());
            toTxn.setToTxn(fromTxn.getAccountTxnId());
            return fromTxn.getAccountTxnId();
        }
        Account account = financeRepository.findAccountById(accountId);
        AccountTxn accountTxn = AccountTxn.create(accountTxnSaveDto, account);
        financeRepository.saveTxn(accountTxn);
        return accountTxn.getAccountTxnId();
    }

    @Transactional
    public void updateTxn(Long roomId, Long accountId, Long accountTxnId, AccountTxnSaveDto accountTxnSaveDto) {


    }

    public List<AccountTxn> getTxn(Long roomId, Long accountId) {
        Account account = financeRepository.findAccountById(accountId);
        return financeRepository.findAllTxns(account);
    }



}
