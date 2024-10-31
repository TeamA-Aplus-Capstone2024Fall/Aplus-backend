package housit.housit_backend.service;

import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.EventDto;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.request.EventSaveRequestDto;
import housit.housit_backend.repository.EventRepository;
import housit.housit_backend.repository.MemberRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public List<EventDto> getEvents(Long roomId) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        List<Event> events = eventRepository.getAllEvents(room);
        List<EventDto> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(EventDto.entityToDto(event));
        }
        return eventDtos;
    }

    @Transactional
    public EventDto createEvent(Long roomId, EventSaveRequestDto eventSaveRequestDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        List<EventMember> eventMembers = new ArrayList<>();
        List<Member> belongMembers = findBelongMembers(eventSaveRequestDto.getMemberIds());
        for (Member belongMember : belongMembers) {
            EventMember eventMember = new EventMember();
            eventMember.createMember(belongMember);
            eventMembers.add(eventMember);
        }

        Event event = Event.createEvent(room, eventSaveRequestDto, eventMembers);
        eventRepository.saveEvent(event);

        return EventDto.entityToDto(event);
    }

    @Transactional
    public EventDto updateEvent(Long roomId, Long eventId, EventSaveRequestDto eventSaveRequestDto) {
        Event event = eventRepository.findEventById(eventId);

        List<EventMember> eventMembers = eventRepository.getAllEventMembers(event);
        List<Member> updatedMembers = findBelongMembers(eventSaveRequestDto.getMemberIds());

        // 업데이트한 이벤트에 멤버가 없으면 기존 EventMember 삭제
        for (EventMember eventMember : eventMembers) {
            Member member = eventMember.getMember();
            if(!updatedMembers.contains(member)) {eventMembers.remove(eventMember);}
        }

        // 업데이트한 이벤트에 새로운 멤버가 있으면 EventMember 추가
        for (Member belongMember : updatedMembers) {
            // eventMembers에서 해당 멤버를 가진 EventMember가 있는지 확인
            boolean exists = eventMembers.stream()
                    .anyMatch(em -> em.getMember().getMemberId().equals(belongMember.getMemberId()));
            if (!exists) {
                EventMember newEventMember = new EventMember();
                newEventMember.createMember(belongMember);
                eventMembers.add(newEventMember);
            }
        }

        event.updateEvent(eventSaveRequestDto, eventMembers);
        return EventDto.entityToDto(event);
    }

    private List<Member> findBelongMembers(List<Long> memberIds) {
        List<Member> members = new ArrayList<>();
        for (Long memberId : memberIds) {
            members.add(memberRepository.findMemberById(memberId));
        }
        return members;
    }
}
