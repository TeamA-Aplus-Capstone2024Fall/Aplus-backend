package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import org.springframework.data.domain.Pageable;
import housit.housit_backend.repository.RoomRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<Room> findRoomById(Long roomId) {
        Room room = em.find(Room.class, roomId);
        return Optional.ofNullable(room); // room이 null일 경우 Optional.empty()를 반환
    }

    @Override
    public Room findRoomByRoomName(String roomName) {
        return em.createQuery("SELECT r FROM Room r WHERE r.roomName = :roomName", Room.class)
                .setParameter("roomName", roomName)
                .getSingleResult();
    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> room = findRoomById(roomId);
        room.ifPresent(em::remove);
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
