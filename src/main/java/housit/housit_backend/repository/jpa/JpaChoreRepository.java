package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.chore.Chore;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.repository.ChoreRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaChoreRepository implements ChoreRepository {
    private final EntityManager em;

    @Override
    public void save(Chore chore) {
        em.persist(chore);
    }

    @Override
    public Chore findById(Long choreId) {
        return em.find(Chore.class, choreId);
    }

    @Override
    public void delete(Long choreId) {
        em.remove(em.find(Chore.class, choreId));
    }

    @Override
    public List<Chore> findAll(Room room) {
        return em.createQuery("select c from Chore c where c.room =:room", Chore.class)
                .setParameter("room", room)
                .getResultList();
    }

    @Override
    public List<Chore> getSoonChores(Long roomId, Integer choreDays, Long memberId) {
        return em.createQuery(
                        "SELECT c FROM Chore c " +
                                "JOIN c.choreMembers cm " +
                                "WHERE c.room.roomId = :roomId " +
                                "AND cm.member.memberId = :memberId", Chore.class)
                .setParameter("roomId", roomId)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
