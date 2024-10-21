package housit.housit_backend.domain.room;

import jakarta.persistence.*;
import lombok.ToString;
import housit.housit_backend.domain.cleaning.CleaningTask;
import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.finance.Account;
import housit.housit_backend.domain.finance.FinancePlan;
import housit.housit_backend.domain.food.Food;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long roomId;

    @Column(nullable = false)
    private String roomPassword;

    @Column(nullable = false)
    private String roomName;

    @Column(nullable = false)
    private Long masterMemberId; // 멤버를 먼저 생성하고 그 Id를 가져와서 매핑

    private String description;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinancePlan> financePlans = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CleaningTask> cleaningTasks = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> foods = new ArrayList<>(); // 여러 음식과의 관계
}
