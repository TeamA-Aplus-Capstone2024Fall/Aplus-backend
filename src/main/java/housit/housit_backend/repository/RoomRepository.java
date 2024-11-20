package housit.housit_backend.repository;

import housit.housit_backend.domain.room.Room;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;

public interface RoomRepository{
    Room saveRoom(Room room);
    Optional<Room> findRoomById(Long roomId);
    Room findRoomByRoomName(String roomName);
    void deleteRoom(Long roomId);

    List<Room> findAllRooms(Pageable pageable);
    List<Room> findRoomsByRoomName(String roomName, Pageable pageable);
}
