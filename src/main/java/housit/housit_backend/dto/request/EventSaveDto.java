package housit.housit_backend.dto.request;

import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.domain.room.Room;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventSaveDto {
    private String eventName;  // 이벤트명
    private LocalDate eventDay;   // 이벤트 날짜
    private LocalTime eventTime;  // 이벤트 시간
    private List<Long> memberIds = new ArrayList<>();
    private String description; // 이벤트 설명

    public Event toEventEntity(Room room, List<EventMember> eventMembers) {
        return Event.builder()
                .room(room)
                .eventName(eventName)
                .eventDay(eventDay)
                .eventTime(eventTime)
                .eventMembers(eventMembers)
                .build();
    }
}
