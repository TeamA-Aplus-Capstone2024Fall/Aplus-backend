package housit.housit_backend.domain.cleaning;

import jakarta.persistence.*;
import lombok.ToString;
import housit.housit_backend.domain.room.Member;

@Entity
@ToString
public class CleaningTaskMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cleaningTaskId", nullable = false)
    private CleaningTask cleaningTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;
}