package housit.housit_backend.domain.room;

import jakarta.persistence.*;
import lombok.ToString;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@ToString
public class Member {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String memberName;
    private String memberPassword;

    @ManyToOne(fetch = LAZY)
    private Room room;
}
