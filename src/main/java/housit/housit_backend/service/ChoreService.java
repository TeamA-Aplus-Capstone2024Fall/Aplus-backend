package housit.housit_backend.service;

import housit.housit_backend.domain.chore.Chore;
import housit.housit_backend.domain.chore.ChoreMember;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.ChoreDto;
import housit.housit_backend.dto.request.ChoreSaveDto;
import housit.housit_backend.repository.ChoreRepository;
import housit.housit_backend.repository.MemberRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChoreService {
    private final ChoreRepository choreRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public List<ChoreDto> getChore(Long roomId) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        List<Chore> chores = choreRepository.findAll(room);
        List<ChoreDto> choreDtos = new ArrayList<>();
        for (Chore chore : chores) {
            choreDtos.add(ChoreDto.entityToDto(chore));
        }
        return choreDtos;
    }

    @Transactional
    public ChoreDto createChore(Long roomId, ChoreSaveDto choreSaveDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        List<ChoreMember> choreMembers = new ArrayList<>();
        List<Member> belongMembers = memberRepository.findBelongMembers(choreSaveDto.getMemberIds());
        for (Member belongMember : belongMembers) {
            ChoreMember choreMember = new ChoreMember();
            choreMember.creatMember(belongMember);
            choreMembers.add(choreMember);
        }

        Chore chore = Chore.createChore(room, choreSaveDto, choreMembers);
        choreRepository.save(chore);

        return ChoreDto.entityToDto(chore);
    }

    @Transactional
    public ChoreDto updateChore(Long roomId, Long choreTaskId, ChoreSaveDto choreSaveDto) {
        Chore chore = choreRepository.findById(choreTaskId);
        List<Member> updatedMembers = memberRepository.findBelongMembers(choreSaveDto.getMemberIds());

        // 새로운 멤버 리스트 생성
        List<ChoreMember> newMembers = new ArrayList<>();

        // 새로운 멤버들로 ChoreMember 생성
        for (Member member : updatedMembers) {
            ChoreMember newMember = new ChoreMember();
            newMember.creatMember(member);
            newMember.createChore(chore);
            newMembers.add(newMember);
        }

        chore.updateChore(choreSaveDto, newMembers);
        return ChoreDto.entityToDto(chore);
    }

    @Transactional
    public void deleteChore(Long roomId, Long choreTaskId) {
        choreRepository.delete(choreTaskId);
    }
}
