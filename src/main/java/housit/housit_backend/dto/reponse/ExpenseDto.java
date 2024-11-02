package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.finance.AccountTxn;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExpenseDto {
    private List<AccountTxn> txns = new ArrayList<>();
    private Long totalExpense;

    public ExpenseDto(List<AccountTxn> txns) {
        this.txns = txns;
        totalExpense = 0L;
        for (AccountTxn txn : txns) {
            totalExpense += txn.getAmount();
        }
    }
}
