package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class EventDto {
    private Long eventId;
    private String eventName;  // 이벤트명
    private LocalDate eventDay;   // 이벤트 날짜
    private LocalTime eventTime;  // 이벤트 시간
    private String description; // 이벤트 설명

    private List<MemberDto> members = new ArrayList<>();

    public static EventDto entityToDto(Event event) {
        List<EventMember> eventMembers = event.getEventMembers();
        List<MemberDto> memberDtos = new ArrayList<>();

        for (EventMember eventMember : eventMembers) {
            Member member = eventMember.getMember();
            memberDtos.add(MemberDto.entityToDto(member));
        }

        return EventDto.builder()
                .eventId(event.getEventId())
                .eventName(event.getEventName())
                .eventDay(event.getEventDay())
                .eventTime(event.getEventTime())
                .members(memberDtos)
                .description(event.getDescription())
                .build();
    }
}
