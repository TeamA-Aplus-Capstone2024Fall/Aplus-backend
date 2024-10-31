package housit.housit_backend.domain.event;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import housit.housit_backend.domain.room.Member;

@Entity @Getter
@ToString
public class EventMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    public void createEvent(Event event) {
        this.event = event;
    }

    public void createMember(Member member) {
        this.member = member;
    }
}