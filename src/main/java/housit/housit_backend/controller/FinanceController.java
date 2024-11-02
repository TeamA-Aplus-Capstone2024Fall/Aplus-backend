package housit.housit_backend.controller;

import housit.housit_backend.domain.finance.AccountTxn;
import housit.housit_backend.dto.reponse.ExpenseDto;
import housit.housit_backend.dto.reponse.FinanceDto;
import housit.housit_backend.dto.reponse.IncomeDto;
import housit.housit_backend.dto.request.AccountSaveDto;
import housit.housit_backend.dto.request.AccountTxnSaveDto;
import housit.housit_backend.dto.request.FinancePlanSaveDto;
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

    // 전쳬 재정 정보 불러오기
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

    // 계좌 수정
    @PutMapping("/room/{roomId}/finance/{accountId}")
    public Long updateAccount(@PathVariable Long roomId,
                              @PathVariable Long accountId,
                              @RequestBody AccountSaveDto accountSaveDto) {
        return financeService.updateAccount(accountId, accountSaveDto);
    }

    // 계좌 삭제
    @DeleteMapping("/room/{roomId}/finance/{accountId}")
    public void deleteAccount(@PathVariable Long roomId,
                              @PathVariable Long accountId) {
        financeService.deleteAccount(accountId);
    }

    // 계좌 로그 불러오기
    @GetMapping("/room/{roomId}/finance/{accountId}")
    public List<AccountTxn> getTxns(@PathVariable Long roomId, @PathVariable Long accountId,
                                    @RequestParam Integer year, @RequestParam Integer month) {
        return financeService.getTxns(roomId, accountId, year, month);
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

    // Txn 삭제
    @DeleteMapping("/room/{roomId}/finance/accountTxn/{accountTxnId}")
    public void deleteTxn(@PathVariable Long roomId,
                          @PathVariable Long accountTxnId) {
        financeService.deleteTxn(accountTxnId);
    }

    // TransferTxn 생성
    @PostMapping("/room/{roomId}/finance/accountTransferTxn")
    public Long createTransferTxn(@PathVariable Long roomId,
                                  @RequestBody AccountTxnSaveDto accountTxnSaveDto,
                                  @RequestParam Long fromAccountId,
                                  @RequestParam Long toAccountId) {
        return financeService.createTransferTxn(roomId, accountTxnSaveDto, fromAccountId, toAccountId);
    }

    // TransferTxn 수정
    @PutMapping("/room/{roomId}/finance/accountTransferTxn/{accountTxnId}")
    public Long updateTransferTxn(@PathVariable Long roomId,
                                  @PathVariable Long accountTxnId,
                                  @RequestBody AccountTxnSaveDto accountTxnSaveDto,
                                  @RequestParam Long fromAccountId,
                                  @RequestParam Long toAccountId) {
        return financeService.updateTransferTxn(roomId, accountTxnId, accountTxnSaveDto, fromAccountId, toAccountId);
    }

    // TransferTxn 삭제
//    @DeleteMapping("/room/{roomId}/finance/accountTransferTxn/{accountTxnId}")
//    public void deleteTransferTxn(@PathVariable Long roomId,
//                                  @PathVariable Long accountTxnId) {
//        financeService.deleteTransferTxn(accountTxnId);
//    }

    // Income 로그 불러오기
    @GetMapping("/room/{roomId}/finance/income")
    public IncomeDto getIncomeTxns(@PathVariable Long roomId,
                                   @RequestParam Integer year,
                                   @RequestParam Integer month) {
        return financeService.getIncomeTxns(roomId, year, month);
    }

    // Expense 로그 불러오기
    @GetMapping("/room/{roomId}/finance/expense")
    public ExpenseDto getExpenseTxns(@PathVariable Long roomId,
                                     @RequestParam Integer year,
                                     @RequestParam Integer month) {
        return financeService.getExpenseTxns(roomId, year, month);
    }

    // Predicted Income 생성
    @PostMapping("/room/{roomId}/finance/predictedIncome")
    public Long createPredictedIncome(@PathVariable Long roomId,
                                      @RequestBody FinancePlanSaveDto financePlanSaveDto) {
        return financeService.createPredictedIncome(roomId, financePlanSaveDto);
    }

    // Predicted Expense 수정
    @PutMapping("/room/{roomId}/finance/predictedExpense/{financePlanId}")
    public Long updatePredictedExpense(@PathVariable Long roomId,
                                       @PathVariable Long financePlanId,
                                       @RequestBody FinancePlanSaveDto financePlanSaveDto) {
        return financeService.updatePredictedExpense(roomId, financePlanId, financePlanSaveDto);
    }

    // Predicted Expense 생성
    @PostMapping("/room/{roomId}/finance/predictedExpense")
    public Long createPredictedExpense(@PathVariable Long roomId,
                                       @RequestBody FinancePlanSaveDto financePlanSaveDto) {
        return financeService.createPredictedExpense(roomId, financePlanSaveDto);
    }

    // Saving Goal 생성
    @PostMapping("/room/{roomId}/finance/savingGoal")
    public Long createSavingGoal(@PathVariable Long roomId,
                                 @RequestBody FinancePlanSaveDto financePlanSaveDto) {
        return financeService.createSavingGoal(roomId, financePlanSaveDto);
    }
}
