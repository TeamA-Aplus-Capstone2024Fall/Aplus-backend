package housit.housit_backend.domain.chore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import housit.housit_backend.domain.room.Member;

@Entity @Getter
@ToString
public class ChoreMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long choreMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choreId", nullable = false)
    private Chore chore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    public void createChore(Chore chore) {
        this.chore = chore;
    }

    public void creatMember(Member member) {
        this.member = member;
    }
}