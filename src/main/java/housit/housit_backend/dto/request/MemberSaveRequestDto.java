package housit.housit_backend.dto.request;

import housit.housit_backend.domain.room.MemberIcon;
import lombok.Getter;

@Getter
public class MemberSaveRequestDto {
    private String memberName;
    private String memberPassword;
    private MemberIcon memberIcon;
}
