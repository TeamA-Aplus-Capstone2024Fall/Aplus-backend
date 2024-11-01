package housit.housit_backend.repository;

import housit.housit_backend.domain.cleaning.CleaningTask;
import housit.housit_backend.domain.room.Room;

import java.util.List;

public interface CleaningTaskRepository {
    void save(CleaningTask cleaningTask);
    CleaningTask findById(Long cleaningTaskId);
    void delete(Long cleaningTaskId);
    List<CleaningTask> findAll(Room room);
}
