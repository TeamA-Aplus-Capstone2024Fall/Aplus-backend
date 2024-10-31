package housit.housit_backend.domain.event;

import housit.housit_backend.domain.cleaning.CleaningTaskMember;
import housit.housit_backend.dto.request.EventSaveRequestDto;
import jakarta.persistence.*;
import lombok.*;
import housit.housit_backend.domain.room.Room;

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

    private LocalDate eventDay;   // 이벤트 날짜
    private LocalTime eventTime;  // 이벤트 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventMember> eventMembers = new ArrayList<>();

    public static Event createEvent(Room room, EventSaveRequestDto eventSaveRequestDto,
                                    List<EventMember> eventMembers) {
        Event event = new Event();
        event.eventName = eventSaveRequestDto.getEventName();
        event.eventDay = eventSaveRequestDto.getEventDay();
        event.eventTime = eventSaveRequestDto.getEventTime();
        event.room = room;
        event.eventMembers = eventMembers;
        for (EventMember eventMember : eventMembers) {
            eventMember.createEvent(event);
        }
        return event;
    }

    public void updateEvent(EventSaveRequestDto eventSaveRequestDto, List<EventMember> eventMembers) {
        this.eventName = eventSaveRequestDto.getEventName();
        this.eventDay = eventSaveRequestDto.getEventDay();
        this.eventTime = eventSaveRequestDto.getEventTime();
        this.eventMembers = eventMembers;
        for (EventMember eventMember : eventMembers) {
            eventMember.createEvent(this);
        }
    }
}