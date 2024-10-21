package housit.housit_backend.domain.finance;

import jakarta.persistence.*;
import lombok.ToString;

import java.time.LocalDate;

@Entity
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

    private String description;  // 거래 내역 (Nullable)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId", nullable = false)
    private Account account;

    // Enum 타입 추가: 거래 유형
    public enum TxnType {
        DEPOSIT, WITHDRAWAL, EXCHANGE
    }
}