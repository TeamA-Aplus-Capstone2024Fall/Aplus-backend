package housit.housit_backend.domain.cleaning;

import housit.housit_backend.dto.request.CleaningTaskSaveRequestDto;
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
public class CleaningTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cleaningTaskId;

    @Column(nullable = false)
    private String cleaningArea;  // 청소 구역
    private String color;
    private String description;
    private String icon;

    @Enumerated(EnumType.STRING)
    private CleaningDay cleaningDay;  // 청소 요일 (Nullable)
    private Integer cleaningFrequency;  // 청소 주기 (예: 주 1회)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "cleaningTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CleaningTaskMember> cleaningTaskMembers = new ArrayList<>();

    public static CleaningTask createCleaningTask(Room room, CleaningTaskSaveRequestDto cleaningTaskSaveRequestDto,
                                              List<CleaningTaskMember> cleaningTaskMembers) {
        CleaningTask cleaningTask = new CleaningTask();
        cleaningTask.cleaningArea = cleaningTaskSaveRequestDto.getCleaningArea();
        cleaningTask.color = cleaningTaskSaveRequestDto.getColor();
        cleaningTask.description = cleaningTaskSaveRequestDto.getDescription();
        cleaningTask.icon = cleaningTaskSaveRequestDto.getIcon();
        cleaningTask.cleaningDay = cleaningTaskSaveRequestDto.getCleaningDay();
        cleaningTask.cleaningFrequency = cleaningTaskSaveRequestDto.getCleaningFrequency();
        cleaningTask.room = room;
        cleaningTask.cleaningTaskMembers = cleaningTaskMembers;
        for(CleaningTaskMember cleaningTaskMember : cleaningTaskMembers) {
            cleaningTaskMember.createCleaning(cleaningTask);
        }
        return cleaningTask;
    }

    public void updateCleaningTask(CleaningTaskSaveRequestDto cleaningTaskSaveDto, List<CleaningTaskMember> newMembers) {
        this.cleaningArea = cleaningTaskSaveDto.getCleaningArea();
        this.color = cleaningTaskSaveDto.getColor();
        this.description = cleaningTaskSaveDto.getDescription();
        this.icon = cleaningTaskSaveDto.getIcon();
        this.cleaningDay = cleaningTaskSaveDto.getCleaningDay();
        this.cleaningFrequency = cleaningTaskSaveDto.getCleaningFrequency();

        this.cleaningTaskMembers.clear();
        this.cleaningTaskMembers.addAll(newMembers);

        for (CleaningTaskMember newMember : newMembers) {
            newMember.createCleaning(this);
        }
    }
}