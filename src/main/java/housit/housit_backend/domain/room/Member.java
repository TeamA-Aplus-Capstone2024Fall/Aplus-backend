package housit.housit_backend.domain.room;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Builder
@Entity
@ToString
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
    private Room room;
}
