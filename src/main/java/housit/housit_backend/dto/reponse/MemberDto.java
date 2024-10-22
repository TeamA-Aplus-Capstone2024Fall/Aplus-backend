package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.room.MemberIcon;
import lombok.Data;

@Data
public class MemberDto {
    private Long memberId;
    private String memberName;
    private MemberIcon memberIcon;
}
