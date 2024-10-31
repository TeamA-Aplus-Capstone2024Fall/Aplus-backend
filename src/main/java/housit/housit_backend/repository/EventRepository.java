package housit.housit_backend.repository;

import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.domain.food.Food;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;

import java.util.List;
import java.util.Optional;

public interface EventRepository {
    void saveEvent(Event event);
    Event findEventById(Long eventId);
    void deleteEvent(Long eventId);
    List<Event> getAllEvents(Room room);
    List<EventMember> getAllEventMembers(Event event);
}