package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.room.MemberIcon;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberDto {
    private Long memberId;
    private String memberName;
    private MemberIcon memberIcon;
}
