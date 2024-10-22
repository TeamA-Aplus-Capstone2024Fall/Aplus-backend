package housit.housit_backend.dto.reponse;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomCreateResponseDto {
    private Long roomId;
    private Long masterMemberId;
}
