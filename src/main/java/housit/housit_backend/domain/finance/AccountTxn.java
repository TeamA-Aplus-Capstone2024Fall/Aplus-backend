package housit.housit_backend.domain.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import housit.housit_backend.dto.request.AccountTxnSaveDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;


@Entity @Getter
@ToString
public class AccountTxn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountTxnId;

    @Column(nullable = false)
    private Long amount;  // 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TxnType txnType;  // 거래 유형 (입금, 출금, 교환)

    @Column(nullable = false)
    private LocalDate txnDate;  // 거래 날짜
    private String description;  // 거래 내역

    private Long fromTxnId; // 채워져있으면 해당 AccountTxn 에서 transfer
    private Long toTxnId; // 채워져있으면 해당 AccountTxn 로 transfer

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    // Enum 타입 추가: 거래 유형

    public static AccountTxn create(AccountTxnSaveDto accountTxnSaveDto, Account account) {
        AccountTxn accountTxn = new AccountTxn();
        accountTxn.amount = accountTxnSaveDto.getAmount();
        accountTxn.txnType = accountTxnSaveDto.getTxnType();
        accountTxn.txnDate = accountTxnSaveDto.getTxnDate();
        accountTxn.description = accountTxnSaveDto.getDescription();
        accountTxn.account = account;
        if(accountTxn.txnType == TxnType.DEPOSIT) account.deposit(accountTxn.amount);
        if(accountTxn.txnType == TxnType.WITHDRAWAL) account.withdraw(accountTxn.amount);
        return accountTxn;
    }

    public void setFromTxn(Long toTxnId) {
        this.toTxnId = toTxnId;
        this.account.withdraw(this.amount);
    }

    public void setToTxn(Long fromTxnId) {
        this.fromTxnId = fromTxnId;
        this.account.deposit(this.amount);
    }

    public void update(AccountTxnSaveDto accountTxnSaveDto) {
        Long diff = accountTxnSaveDto.getAmount() - this.amount;
        this.amount = accountTxnSaveDto.getAmount();
        this.txnDate = accountTxnSaveDto.getTxnDate();
        this.description = accountTxnSaveDto.getDescription();
        if(this.txnType == TxnType.DEPOSIT) account.deposit(diff);
        if(this.txnType == TxnType.WITHDRAWAL) account.withdraw(diff);
    }

    public void delete(Account account) {
        if(txnType == TxnType.DEPOSIT) account.withdraw(this.amount);
        if(txnType == TxnType.WITHDRAWAL) account.deposit(this.amount);
        if(txnType == TxnType.TRANSFER) {
            if(fromTxnId != null) { // fromTxnId 가 존재 -> 입금에 해당하는 Txn
                account.withdraw(this.amount);
            }
            if(toTxnId != null) { // toTxnId 가 존재 -> 출금에 해당하는 Txn
                account.deposit(this.amount);
            }
        }
    }
}