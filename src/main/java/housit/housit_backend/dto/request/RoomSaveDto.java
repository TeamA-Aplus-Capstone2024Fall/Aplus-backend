package housit.housit_backend.dto.request;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import lombok.Data;

@Data
public class RoomSaveDto {
    private String roomName;
    private String description;
    private String roomPassword;
    private String masterMemberName;
    private String masterMemberPassword;


    public Room toRoomEntity() {
        return Room.builder()
                .roomName(roomName)
                .description(description)
                .roomPassword(roomPassword)
                .build();
    }

    public Member toMasterMemberEntity(Room room) {
        return Member.builder()
                .memberName(masterMemberName)
                .memberPassword(masterMemberPassword)
                .hasPassword(!masterMemberPassword.isEmpty())
                .foodDays(7)
                .financeDays(7)
                .choreDays(7)
                .eventDays(7)
                .minimumFoodQuantity(1)
                .room(room)
                .roles("MASTER")
                .build();
    }
}
