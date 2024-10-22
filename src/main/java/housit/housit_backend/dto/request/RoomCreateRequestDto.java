package housit.housit_backend.dto.request;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import lombok.Builder;
import lombok.Data;


@Data
public class RoomCreateRequestDto {
    private String roomName;
    private String description;
    private String roomPassword;
    private String masterMemberName;
    private String masterMemberPassword;


    public Room toRoomEntity(Long masterMemberId) {
        return Room.builder() // 모호한 메서드입니다 오류
                .roomName(roomName)
                .description(description)
                .roomPassword(roomPassword)
                .masterMemberId(masterMemberId)
                .build();
    }

    public Member toMemberEntity() {
        return Member.builder()
                .memberName(masterMemberName)
                .memberPassword(masterMemberPassword)
                .build();
    }
}
