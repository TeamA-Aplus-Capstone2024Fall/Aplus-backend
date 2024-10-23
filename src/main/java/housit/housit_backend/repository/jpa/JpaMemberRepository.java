package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public Optional<Member> findMemberById(Long memberId) {
        Member member = em.find(Member.class, memberId);
        return Optional.ofNullable(member);
    }

    @Override
    public void deleteMember(Long memberId) {
        Optional<Member> member = findMemberById(memberId);
        member.ifPresent(em::remove);
    }

    @Override
    public List<Member> getAllMembers(Long roomId) {
        return em.createQuery("select m from Member m where m.room.id = :roomId", Member.class) // room의 id를 조건으로 조회
                .setParameter("roomId", roomId)  // 파라미터 바인딩
                .getResultList();
    }
}
