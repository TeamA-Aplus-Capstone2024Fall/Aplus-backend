package housit.housit_backend.domain.event;

import jakarta.persistence.*;
import lombok.ToString;
import housit.housit_backend.domain.room.Room;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@ToString
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false)
    private String eventName;  // 이벤트명

    private LocalDate eventDay;   // 이벤트 날짜
    private LocalTime eventTime;  // 이벤트 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;
}