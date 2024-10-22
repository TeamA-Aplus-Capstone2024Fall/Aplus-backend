package housit.housit_backend.repository;

import housit.housit_backend.domain.room.Member;

import java.util.List;

public interface MemberRepository {
    public Member createMember(Member member);
    public Member findMemberById(Long memberId);
    public Member updateMember(Member member);
    public void deleteMember(Long memberId);

    public List<Member> getAllMembers(Long roomId);
}
