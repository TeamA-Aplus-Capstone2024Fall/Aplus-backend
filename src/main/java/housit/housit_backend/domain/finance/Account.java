package housit.housit_backend.domain.finance;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import housit.housit_backend.domain.room.Room;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false)
    private String accountName;  // 계좌 이름

    private Long balance;  // 잔액 (Nullable)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountTxn> transactions = new ArrayList<>();  // 계좌 거래 내역
}