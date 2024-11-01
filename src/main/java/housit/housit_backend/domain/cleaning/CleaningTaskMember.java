package housit.housit_backend.domain.cleaning;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import housit.housit_backend.domain.room.Member;

@Entity @Getter
@ToString
public class CleaningTaskMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cleaningTaskMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cleaningTaskId", nullable = false)
    private CleaningTask cleaningTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    public void createCleaning(CleaningTask cleaningTask) {
        this.cleaningTask = cleaningTask;
    }

    public void creatMember(Member member) {
        this.member = member;
    }
}