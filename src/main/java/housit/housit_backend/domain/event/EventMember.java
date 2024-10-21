package housit.housit_backend.domain.event;

import jakarta.persistence.*;
import lombok.ToString;
import housit.housit_backend.domain.room.Member;

@Entity
@ToString
public class EventMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;
}