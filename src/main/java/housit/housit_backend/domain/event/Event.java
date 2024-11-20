package housit.housit_backend.domain.event;

import housit.housit_backend.dto.request.EventSaveDto;
import jakarta.persistence.*;
import lombok.*;
import housit.housit_backend.domain.room.Room;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity @Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false)
    private String eventName;  // 이벤트명
    private String description; // 이벤트 설명

    private LocalDate eventDay;   // 이벤트 날짜
    private LocalTime eventTime;  // 이벤트 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventMember> eventMembers = new ArrayList<>();

    public static Event createEvent(Room room, EventSaveDto eventSaveDto,
                                    List<EventMember> eventMembers) {
        Event event = new Event();
        event.eventName = eventSaveDto.getEventName();
        event.eventDay = eventSaveDto.getEventDay();
        event.eventTime = eventSaveDto.getEventTime();
        event.room = room;
        event.eventMembers = eventMembers;
        event.description = eventSaveDto.getDescription();
        for (EventMember eventMember : eventMembers) {
            eventMember.createEvent(event);
        }
        return event;
    }

    public void updateEvent(EventSaveDto eventSaveDto, List<EventMember> newEventMembers) {
        this.eventName = eventSaveDto.getEventName();
        this.eventDay = eventSaveDto.getEventDay();
        this.eventTime = eventSaveDto.getEventTime();
        this.description = eventSaveDto.getDescription();
        // 기존 리스트 clear
        this.eventMembers.clear();
        // 새로운 멤버들 추가
        this.eventMembers.addAll(newEventMembers);

        // 양방향 관계 설정
        for (EventMember eventMember : newEventMembers) {
            eventMember.createEvent(this);
        }
    }
}