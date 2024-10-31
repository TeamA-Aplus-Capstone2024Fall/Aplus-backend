package housit.housit_backend.domain.room;

import housit.housit_backend.dto.reponse.RoomCreateResponseDto;
import jakarta.persistence.*;
import lombok.*;
import housit.housit_backend.domain.cleaning.CleaningTask;
import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.finance.Account;
import housit.housit_backend.domain.finance.FinancePlan;
import housit.housit_backend.domain.food.Food;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long roomId;

    @Column(nullable = false)
    private String roomPassword;

    @Getter
    @Column(nullable = false)
    private String roomName;

    private Long masterMemberId;

    @Getter
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

    public RoomCreateResponseDto toRoomCreateResponseDto() {
        return RoomCreateResponseDto.builder()
                .roomId(roomId)
                .masterMemberId(masterMemberId)
                .build();
    }

    public Boolean validatePassword(String roomPassword) {
        return this.roomPassword.equals(roomPassword);
    }

    public void initializeMasterMemberRoomId(Long masterMemberId) {
        this.masterMemberId = masterMemberId;
    }
}
