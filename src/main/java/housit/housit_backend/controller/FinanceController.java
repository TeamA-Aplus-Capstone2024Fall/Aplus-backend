package housit.housit_backend.controller;

import housit.housit_backend.domain.finance.AccountTxn;
import housit.housit_backend.dto.reponse.FinanceDto;
import housit.housit_backend.dto.request.AccountSaveDto;
import housit.housit_backend.dto.request.AccountTxnSaveDto;
import housit.housit_backend.service.FinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FinanceController {
    public final FinanceService financeService;

    @GetMapping("/room/{roomId}/finance")
    public FinanceDto getFinance(@PathVariable Long roomId,
                                 @RequestParam Integer year,
                                 @RequestParam Integer month) {
        return financeService.getFinanceInfo(roomId, year, month);
    }

    // 계좌 생성
    @PostMapping("/room/{roomId}/finance/account")
    public Long createAccount(@PathVariable Long roomId,
                              @RequestBody AccountSaveDto accountSaveDto) {
        return financeService.createAccount(roomId, accountSaveDto);
    }

    // 계좌 rename
    @PutMapping("/room/{roomId}/finance/{accountId}")
    public Long updateAccount(@PathVariable Long roomId,
                              @PathVariable Long accountId,
                              @RequestBody AccountSaveDto accountSaveDto) {
        return financeService.updateAccount(accountId, accountSaveDto);
    }

    // 계좌 로그 불러오기
    @GetMapping("/room/{roomId}/finance/{accountId}")
    public List<AccountTxn> getTxns(@PathVariable Long roomId, @PathVariable Long accountId) {
        return financeService.getTxn(roomId, accountId);
    }

    // Txn 생성
    @PostMapping("/room/{roomId}/finance/{accountId}/accountTxn")
    public Long createTxn(@PathVariable Long roomId,
                          @PathVariable Long accountId,
                          @RequestBody AccountTxnSaveDto accountTxnSaveDto) {
        return financeService.createTxn(roomId, accountId, accountTxnSaveDto);
    }

    // Txn 수정
    @PutMapping("/room/{roomId}/finance/accountTxn/{accountTxnId}")
    public void updateTxn(@PathVariable Long roomId,
                          @PathVariable Long accountTxnId,
                          @RequestBody AccountTxnSaveDto accountTxnSaveDto) {
        financeService.updateTxn(roomId, accountTxnId, accountTxnSaveDto);
    }

    // TransferTxn 생성
    @PostMapping("/room/{roomId}/finance/accountTransferTxn")
    public Long createTransferTxn(@PathVariable Long roomId,
                                  @RequestBody AccountTxnSaveDto accountTxnSaveDto,
                                  @RequestParam Long fromAccountId,
                                  @RequestParam Long toAccountId) {
        return financeService.createTransferTxn(roomId, accountTxnSaveDto, fromAccountId, toAccountId);
    }

    // TransferTxn 생성
    @PutMapping("/room/{roomId}/finance/accountTransferTxn/{accountTxnId}")
    public Long updateTransferTxn(@PathVariable Long roomId,
                                  @RequestBody AccountTxnSaveDto accountTxnSaveDto,
                                  @RequestParam Long fromAccountId,
                                  @RequestParam Long toAccountId) {
        return financeService.createTransferTxn(roomId, accountTxnSaveDto, fromAccountId, toAccountId);
    }


}
