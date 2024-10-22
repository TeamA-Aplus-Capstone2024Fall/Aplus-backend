package housit.housit_backend.repository;

import housit.housit_backend.domain.room.Room;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface RoomRepository{
    public Room saveRoom(Room room);
    public Room findRoomById(Long roomId);
    public void deleteRoom(Long roomId);

    public List<Room> findAllRooms(Pageable pageable);
    public List<Room> findRoomsByRoomName(String roomName, Pageable pageable);
}
