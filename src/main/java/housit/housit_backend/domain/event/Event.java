package housit.housit_backend.domain.event;

import housit.housit_backend.domain.cleaning.CleaningTaskMember;
import housit.housit_backend.dto.request.EventSaveRequestDto;
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

    private LocalDate eventDay;   // 이벤트 날짜
    private LocalTime eventTime;  // 이벤트 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @BatchSize(size = 100)
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

    public void updateEvent(EventSaveRequestDto eventSaveRequestDto, List<EventMember> newEventMembers) {
        this.eventName = eventSaveRequestDto.getEventName();
        this.eventDay = eventSaveRequestDto.getEventDay();
        this.eventTime = eventSaveRequestDto.getEventTime();

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