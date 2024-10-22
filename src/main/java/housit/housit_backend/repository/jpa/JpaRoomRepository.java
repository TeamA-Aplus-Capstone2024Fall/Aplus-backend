package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.room.Room;
import housit.housit_backend.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@RequiredArgsConstructor
public class JpaRoomRepository implements RoomRepository {
    private final EntityManager em;


    @Override
    public Room createRoom(Room room) {
        em.persist(room);
        return room;
    }

    @Override
    public Room findRoomById(Long roomId) {
        return null;
    }

    @Override
    public Room updateRoom(Room room) {
        return null;
    }

    @Override
    public void deleteRoom(Room room) {

    }

    @Override
    public List<Room> findAllRooms() {
        return List.of();
    }

    @Override
    public List<Room> findRoomsByRoomId(Long roomId) {
        return List.of();
    }

    @Override
    public List<Room> findRoomsByRoomName(String roomName, Pageable pageable) {
        return List.of();
    }
}
