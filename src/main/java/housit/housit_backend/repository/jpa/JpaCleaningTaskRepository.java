package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.cleaning.CleaningTask;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.repository.CleaningTaskRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaCleaningTaskRepository implements CleaningTaskRepository {
    private final EntityManager em;

    @Override
    public void save(CleaningTask cleaningTask) {
        em.persist(cleaningTask);
    }

    @Override
    public CleaningTask findById(Long cleaningTaskId) {
        return em.find(CleaningTask.class, cleaningTaskId);
    }

    @Override
    public void delete(Long cleaningTaskId) {
        em.remove(em.find(CleaningTask.class, cleaningTaskId));
    }

    @Override
    public List<CleaningTask> findAll(Room room) {
        return em.createQuery("select ct from CleaningTask ct where ct.room =:room", CleaningTask.class)
                .setParameter("room", room)
                .getResultList();
    }
}
