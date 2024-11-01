package housit.housit_backend.domain.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import housit.housit_backend.dto.request.AccountSaveDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import housit.housit_backend.domain.room.Room;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@ToString @BatchSize(size = 100)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false)
    private String accountName;  // 계좌 이름

    private Long balance;  // 잔액 (Nullable)
    private String color;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountTxn> transactions = new ArrayList<>();  // 계좌 거래 내역

    public static Account createAccount(AccountSaveDto accountSaveDto, Room room) {
        Account account = new Account();
        account.accountName = accountSaveDto.getAccountName();
        account.color = accountSaveDto.getColor();
        account.balance = 0L;
        account.room = room;
        return account;
    }

    public void withdraw(Long amount) {
        this.balance -= amount;
    }

    public void deposit(Long amount) {
        this.balance += amount;
    }

    public void update(AccountSaveDto accountSaveDto) {
        this.accountName = accountSaveDto.getAccountName();
        this.color = accountSaveDto.getColor();
    }
}