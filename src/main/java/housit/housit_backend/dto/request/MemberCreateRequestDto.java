package housit.housit_backend.dto.request;

import housit.housit_backend.domain.room.MemberIcon;
import lombok.Data;

@Data
public class MemberCreateRequestDto {
    private String memberName;
    private String memberPassword;
    private MemberIcon memberIcon;
}
