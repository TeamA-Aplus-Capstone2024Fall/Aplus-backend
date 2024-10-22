package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import org.springframework.data.domain.Pageable;
import housit.housit_backend.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaRoomRepository implements RoomRepository {
    private final EntityManager em;

    @Override
    public Room saveRoom(Room room) {
        if(room.getRoomId() == null) em.persist(room);
        else em.merge(room);
        return room;
    }

    @Override
    public Room findRoomById(Long roomId) {
        return em.find(Room.class, roomId);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = findRoomById(roomId);
        if(room != null) em.remove(room);
    }

    @Override
    public List<Room> findAllRooms(Pageable pageable) {
        return em.createQuery("SELECT r FROM Room r", Room.class)
                .setFirstResult((int) pageable.getOffset()) // 페이지 시작점 설정
                .setMaxResults(pageable.getPageSize())      // 한 페이지에 가져올 데이터 수 설정
                .getResultList();
    }

    @Override
    public List<Room> findRoomsByRoomName(String roomName, Pageable pageable) {
        return List.of();
    }
}
