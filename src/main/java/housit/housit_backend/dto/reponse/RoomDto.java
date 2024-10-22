package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.room.Room;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomDto {
    private String roomName;
    private String description;
    List<MemberDto> members = new ArrayList<>();

    public RoomDto(Room room, List<MemberDto> members) {
        this.roomName = room.getRoomName();
        this.description = room.getDescription();
        this.members = members;
    }
}

