package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.MemberIcon;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberDto {
    private Long memberId;
    private String memberName;
    private Boolean hasPassword;
    private MemberIcon memberIcon;

    public static MemberDto entityToDto(Member member) {
            return MemberDto.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .memberIcon(member.getMemberIcon())
                    .hasPassword(member.getHasPassword())
                    .build();
    }
}
