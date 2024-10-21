package housit.housit_backend.domain.cleaning;

import jakarta.persistence.*;
import lombok.ToString;
import housit.housit_backend.domain.room.Room;

@Entity
@ToString
public class CleaningTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cleaningTaskId;

    @Column(nullable = false)
    private String cleaningArea;  // 청소 구역

    @Enumerated(EnumType.STRING)
    private CleaningDay cleaningDay;  // 청소 요일 (Nullable)

    @Column(nullable = false)
    private Integer cleaningFrequency;  // 청소 주기 (예: 주 1회)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    // 양방향 관계
//    @OneToMany(mappedBy = "cleaningTask", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CleaningTaskMember> cleaningTaskMembers = new ArrayList<>();
}