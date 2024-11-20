package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.event.Event;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.domain.finance.PredictedIncome;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.EventDto;
import housit.housit_backend.repository.EventRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaEventRepository implements EventRepository {
    private final EntityManager em;

    @Override
    public void save(Event event) {
        em.persist(event);
    }

    @Override
    public Event findById(Long eventId) {
        return em.find(Event.class, eventId);
    }

    @Override
    public void delete(Long eventId) {
        em.remove(findById(eventId));
    }

    @Override
    public List<Event> findAll(Room room) {
        return em.createQuery("select e from Event e where e.room = :room", Event.class)
                .setParameter("room", room)
                .getResultList();
    }

    @Override
    public List<EventMember> findAllWithMembers(Event event) {
        return em.createQuery("select em from EventMember em where em.event =: event", EventMember.class)
                .setParameter("event", event)
                .getResultList();
    }

    @Override
    public List<Event> getSoonEvents(Long roomId, int days, Long memberId) {
        // 현재 날짜에 days를 더한 날짜를 계산합니다.
        LocalDate expirationThreshold = LocalDate.now().plusDays(days);

        return em.createQuery(
                        "SELECT DISTINCT e FROM Event e " +
                                "JOIN e.eventMembers em " +
                                "WHERE e.room.roomId = :roomId " +
                                "AND e.eventDay BETWEEN CURRENT_DATE AND :expirationThreshold " +
                                "AND em.member.memberId = :memberId " +
                                "ORDER BY e.eventDay ASC", Event.class)
                .setParameter("roomId", roomId)
                .setParameter("expirationThreshold", expirationThreshold)
                .setParameter("memberId", memberId)
                .getResultList();

    }
}
