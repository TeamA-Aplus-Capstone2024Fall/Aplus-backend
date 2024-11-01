package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.repository.EventRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaEventRepository implements EventRepository {
    private final EntityManager em;

    @Override
    public void saveEvent(Event event) {
        em.persist(event);
    }

    @Override
    public Event findEventById(Long eventId) {
        return em.find(Event.class, eventId);
    }

    @Override
    public void deleteEvent(Long eventId) {
        em.remove(findEventById(eventId));
    }

    @Override
    public List<Event> getAllEvents(Room room) {
        return em.createQuery("select e from Event e where e.room = :room", Event.class)
                .setParameter("room", room)
                .getResultList();
    }

    @Override
    public List<EventMember> getAllEventMembers(Event event) {
        return em.createQuery("select em from EventMember em where em.event =: event", EventMember.class)
                .setParameter("event", event)
                .getResultList();
    }
}
