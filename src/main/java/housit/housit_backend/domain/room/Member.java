package housit.housit_backend.domain.room;

import housit.housit_backend.dto.reponse.MemberDto;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Getter
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String memberName;
    private String memberPassword;

    @Enumerated(EnumType.STRING)
    private MemberIcon memberIcon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public MemberDto toMemberDto() {
        return MemberDto.builder()
                .memberId(this.memberId)
                .memberName(this.memberName)
                .memberIcon(this.memberIcon)
                .build();
    }

    public Boolean validatePassword(String memberPassword) {
        return this.memberPassword.equals(memberPassword);
    }

    public static Member createMember(String memberName, String memberPassword, MemberIcon memberIcon, Room room) {
        return Member.builder()
                .memberName(memberName)
                .memberPassword(memberPassword)
                .memberIcon(memberIcon)
                .room(room)
                .build();
    }

    public void updateMember(String memberName, String memberPassword, MemberIcon memberIcon) {
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.memberIcon = memberIcon;
    }
}
