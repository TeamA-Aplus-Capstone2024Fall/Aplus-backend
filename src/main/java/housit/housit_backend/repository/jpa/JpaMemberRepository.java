package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {
    private final EntityManager em;

    @Override
    public Member createMember(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Member findMemberById(Long memberId) {
        return null;
    }

    @Override
    public Member updateMember(Member member) {
        return null;
    }

    @Override
    public void deleteMember(Long memberId) {

    }

    @Override
    public List<Member> getAllMembers(Long roomId) {
        return List.of();
    }
}
