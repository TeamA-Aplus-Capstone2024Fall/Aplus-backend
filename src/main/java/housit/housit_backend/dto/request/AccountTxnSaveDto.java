package housit.housit_backend.dto.request;

import housit.housit_backend.domain.finance.Account;
import housit.housit_backend.domain.finance.AccountTxn;
import housit.housit_backend.domain.finance.TxnType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class AccountTxnSaveDto {
    private Long amount;  // 금액
    private TxnType txnType;  // 거래 유형 (입금, 출금, 교환)
    private LocalDate txnDate;  // 거래 날짜
    private String description;  // 거래 내역
}
