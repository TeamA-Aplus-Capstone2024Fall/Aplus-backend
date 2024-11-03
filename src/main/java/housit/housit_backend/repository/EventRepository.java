package housit.housit_backend.repository;

import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.EventDto;

import java.util.List;

public interface EventRepository {
    void save(Event event);
    Event findById(Long eventId);
    void delete(Long eventId);
    List<Event> findAll(Room room);
    List<EventMember> findAllWithMembers(Event event);

    List<Event> getSoonEvents(Long roomId, int days);
}