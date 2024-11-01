package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {
    private final EntityManager em;

    @Override
    public Member saveMember(Member member) {
        if (member.getMemberId() == null) em.persist(member);
        else em.merge(member);
        return member;
    }

    @Override
    public Member findMemberById(Long memberId) {
        return em.find(Member.class, memberId);
    }

    @Override
    public void deleteMember(Long memberId) {
        em.remove(em.find(Member.class, memberId));
    }

    @Override
    public List<Member> getAllMembers(Long roomId) {
        return em.createQuery("select m from Member m where m.room.id = :roomId", Member.class) // room의 id를 조건으로 조회
                .setParameter("roomId", roomId)  // 파라미터 바인딩
                .getResultList();
    }

    @Override
    public List<Member> findBelongMembers(List<Long> memberIds) { // 이거 where in 절로 최적화 가능
        List<Member> members = new ArrayList<>();
        for (Long memberId : memberIds) {
            members.add(findMemberById(memberId));
        }
        return members;
    }
}
