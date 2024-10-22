package housit.housit_backend.repository;

import housit.housit_backend.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface RoomRepository{
    public Room createRoom(Room room);
    public Room findRoomById(Long roomId);
    public Room updateRoom(Room room);
    public void deleteRoom(Room room);

    public List<Room> findAllRooms();
    public List<Room> findRoomsByRoomId(Long roomId);
    public List<Room> findRoomsByRoomName(String roomName, Pageable pageable);
}
