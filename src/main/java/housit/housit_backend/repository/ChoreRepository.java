package housit.housit_backend.repository;

import housit.housit_backend.domain.chore.Chore;
import housit.housit_backend.domain.room.Room;

import java.util.List;

public interface ChoreRepository {
    void save(Chore chore);
    Chore findById(Long choreId);
    void delete(Long choreId);
    List<Chore> findAll(Room room);
}
