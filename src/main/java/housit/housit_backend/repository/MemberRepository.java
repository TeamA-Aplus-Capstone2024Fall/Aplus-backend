package housit.housit_backend.repository;

import housit.housit_backend.domain.room.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    public Member saveMember(Member member);
    public Optional<Member> findMemberById(Long memberId);
    public void deleteMember(Long memberId);
    public List<Member> getAllMembers(Long roomId);
}
