package housit.housit_backend.domain.room;

import housit.housit_backend.domain.chore.ChoreMember;
import housit.housit_backend.domain.event.EventMember;
import housit.housit_backend.dto.request.MemberSaveDto;
import housit.housit_backend.dto.request.MemberSettingSaveDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Boolean hasPassword; // 프론트에서 hasPassword 가 true 면 비밀번호 입력창을 띄워주도록 하기 위함
    private String roles;

    @Enumerated(EnumType.STRING)
    private MemberIcon memberIcon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoreMember> choreMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<EventMember> eventMembers = new ArrayList<>();

    private Integer foodDays;
    private Integer financeDays;
    //private Integer choreDays;
    private Integer eventDays;
    private Integer minimumFoodQuantity;

    public Boolean validatePassword(String memberPassword) {
        return this.memberPassword.equals(memberPassword);
    }

    public static Member createMember(MemberSaveDto memberSaveDto, Room room) {
        return Member.builder()
                .memberName(memberSaveDto.getMemberName())
                .memberPassword(memberSaveDto.getMemberPassword())
                .memberIcon(memberSaveDto.getMemberIcon())
                .room(room)
                .foodDays(7)
                .financeDays(7)
                .hasPassword(!Objects.equals(memberSaveDto.getMemberPassword(), ""))
                //.choreDays(7)
                .eventDays(7)
                .minimumFoodQuantity(1)
                .build();
    }

    public void updateMember(String memberName, String memberPassword, MemberIcon memberIcon) {
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.memberIcon = memberIcon;
        this.hasPassword = !Objects.equals(memberPassword, "");
    }

    public void updateMemberSetting(MemberSettingSaveDto memberSettingSaveDto) {
        this.foodDays = memberSettingSaveDto.getFoodDays();
        this.financeDays = memberSettingSaveDto.getFinanceDays();
        //this.choreDays = memberSettingSaveDto.getChoreDays();
        this.eventDays = memberSettingSaveDto.getEventDays();
        this.minimumFoodQuantity = memberSettingSaveDto.getMinimumFoodQuantity();
    }
}
