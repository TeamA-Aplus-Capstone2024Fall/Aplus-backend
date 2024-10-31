package housit.housit_backend.repository;

import housit.housit_backend.domain.room.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member saveMember(Member member);
    Member findMemberById(Long memberId);
    void deleteMember(Long memberId);
    List<Member> getAllMembers(Long roomId);
}
