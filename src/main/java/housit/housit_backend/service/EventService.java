package housit.housit_backend.service;

import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.EventDto;
import housit.housit_backend.dto.request.EventSaveDto;
import housit.housit_backend.repository.EventRepository;
import housit.housit_backend.repository.MemberRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public List<EventDto> getEvents(Long roomId) { // N+1 터짐
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        List<Event> events = eventRepository.findAll(room);
        List<EventDto> eventDtos = new ArrayList<>();
        for (Event event : events) {
            eventDtos.add(EventDto.entityToDto(event));
        }
        return eventDtos;
    }

    @Transactional
    public EventDto createEvent(Long roomId, EventSaveDto eventSaveDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        List<EventMember> eventMembers = new ArrayList<>();
        List<Member> belongMembers = findBelongMembers(eventSaveDto.getMemberIds());
        for (Member belongMember : belongMembers) {
            EventMember eventMember = new EventMember();
            eventMember.createMember(belongMember);
            eventMembers.add(eventMember);
        }

        Event event = Event.createEvent(room, eventSaveDto, eventMembers);
        eventRepository.save(event);

        return EventDto.entityToDto(event);
    }

    @Transactional
    public EventDto updateEvent(Long roomId, Long eventId, EventSaveDto eventSaveDto) {
        Event event = eventRepository.findById(eventId);
        List<Member> updatedMembers = findBelongMembers(eventSaveDto.getMemberIds());

        // 새로운 EventMember 리스트 생성
        List<EventMember> newMembers = new ArrayList<>();

        // 새로운 멤버들로 EventMember 생성
        for (Member member : updatedMembers) {
            EventMember newMember = new EventMember();
            newMember.createMember(member);
            newMember.createEvent(event);
            newMembers.add(newMember);
        }

        event.updateEvent(eventSaveDto, newMembers);
        return EventDto.entityToDto(event);
    }

    @Transactional
    public void deleteEvent(Long roomId, Long eventId) {
        eventRepository.delete(eventId);
    }

    private List<Member> findBelongMembers(List<Long> memberIds) {
        List<Member> members = new ArrayList<>();
        for (Long memberId : memberIds) {
            members.add(memberRepository.findMemberById(memberId));
        }
        return members;
    }
}
