package housit.housit_backend.domain.cleaning;

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

    @Enumerated(EnumType.STRING)
    private CleaningDay cleaningDay;  // 청소 요일 (Nullable)
    private Integer cleaningFrequency;  // 청소 주기 (예: 주 1회)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "cleaningTask", cascade = CascadeType.ALL)
    private List<CleaningTaskMember> cleaningTaskMembers = new ArrayList<>();
}