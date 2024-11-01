package housit.housit_backend.domain.chore;

import housit.housit_backend.dto.request.ChoreSaveRequestDto;
import jakarta.persistence.*;
import lombok.*;
import housit.housit_backend.domain.room.Room;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity @Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Chore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choreId;

    @Column(nullable = false)
    private String choreArea;  // 청소 구역
    private String color;
    private String description;
    private String icon;

    @Enumerated(EnumType.STRING)
    private ChoreDay choreDay;  // 청소 요일 (Nullable)
    private Integer choreFrequency;  // 청소 주기 (예: 주 1회)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "chore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoreMember> choreMembers = new ArrayList<>();

    public static Chore createChore(Room room, ChoreSaveRequestDto choreSaveRequestDto,
                                           List<ChoreMember> choreMembers) {
        Chore chore = new Chore();
        chore.choreArea = choreSaveRequestDto.getChoreArea();
        chore.color = choreSaveRequestDto.getColor();
        chore.description = choreSaveRequestDto.getDescription();
        chore.icon = choreSaveRequestDto.getIcon();
        chore.choreDay = choreSaveRequestDto.getChoreDay();
        chore.choreFrequency = choreSaveRequestDto.getChoreFrequency();
        chore.room = room;
        chore.choreMembers = choreMembers;
        for(ChoreMember choreMember : choreMembers) {
            choreMember.createChore(chore);
        }
        return chore;
    }

    public void updateChore(ChoreSaveRequestDto choreSaveDto, List<ChoreMember> newMembers) {
        this.choreArea = choreSaveDto.getChoreArea();
        this.color = choreSaveDto.getColor();
        this.description = choreSaveDto.getDescription();
        this.icon = choreSaveDto.getIcon();
        this.choreDay = choreSaveDto.getChoreDay();
        this.choreFrequency = choreSaveDto.getChoreFrequency();

        this.choreMembers.clear();
        this.choreMembers.addAll(newMembers);

        for (ChoreMember newMember : newMembers) {
            newMember.createChore(this);
        }
    }
}