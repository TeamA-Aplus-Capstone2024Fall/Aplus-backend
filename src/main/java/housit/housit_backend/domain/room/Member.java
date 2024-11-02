package housit.housit_backend.domain.room;

import housit.housit_backend.domain.chore.ChoreMember;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.dto.request.MemberSaveDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Builder
@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String memberName;
    private String memberPassword;

    @Enumerated(EnumType.STRING)
    private MemberIcon memberIcon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoreMember> choreMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<EventMember> eventMembers = new ArrayList<>();

    public Boolean validatePassword(String memberPassword) {
        return this.memberPassword.equals(memberPassword);
    }

    public static Member createMember(MemberSaveDto memberSaveDto, Room room) {
        return Member.builder()
                .memberName(memberSaveDto.getMemberName())
                .memberPassword(memberSaveDto.getMemberPassword())
                .memberIcon(memberSaveDto.getMemberIcon())
                .room(room)
                .build();
    }

    public void updateMember(String memberName, String memberPassword, MemberIcon memberIcon) {
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.memberIcon = memberIcon;
    }
}
