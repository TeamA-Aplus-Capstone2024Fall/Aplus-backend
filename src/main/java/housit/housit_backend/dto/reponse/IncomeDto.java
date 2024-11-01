package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.finance.AccountTxn;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class IncomeDto {
    private List<AccountTxn> txns = new ArrayList<>();
    private Long totalIncome;

    public IncomeDto(List<AccountTxn> txns) {
        this.txns = txns;
        totalIncome = 0L;
        for (AccountTxn txn : txns) {
            totalIncome += txn.getAmount();
        }
    }
}
