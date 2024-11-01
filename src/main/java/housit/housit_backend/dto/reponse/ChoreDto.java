package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.chore.ChoreDay;
import housit.housit_backend.domain.chore.Chore;
import housit.housit_backend.domain.chore.ChoreMember;
import housit.housit_backend.domain.room.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ChoreDto {
    private Long choreId;
    private String choreArea;  // 청소 구역
    private String color;
    private String description;
    private String icon;
    private ChoreDay choreDay;  // 청소 요일 (Nullable)
    private Integer choreFrequency;  // 청소 주기 (예: 주 1회)
    private List<MemberDto> choreMembers = new ArrayList<>();

    public static ChoreDto entityToDto(Chore chore) {
        List<ChoreMember> choreMembers = chore.getChoreMembers();
        List<MemberDto> memberDtos = new ArrayList<>();

        for (ChoreMember choreMember : choreMembers) {
            Member member = choreMember.getMember();
            memberDtos.add(MemberDto.entityToDto(member));
        }

        return ChoreDto.builder()
                .choreId(chore.getChoreId())
                .choreArea(chore.getChoreArea())
                .color(chore.getColor())
                .description(chore.getDescription())
                .icon(chore.getIcon())
                .choreDay(chore.getChoreDay())
                .choreFrequency(chore.getChoreFrequency())
                .choreMembers(memberDtos)
                .build();
    }
}
