package housit.housit_backend.service;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.RoomCreateResponseDto;
import housit.housit_backend.dto.request.RoomCreateRequestDto;
import housit.housit_backend.repository.MemberRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RoomCreateResponseDto roomCreate(RoomCreateRequestDto roomCreateRequestDto) {
        // 방장 생성 후 방 생성

        // 방장 생성
        Member masterMember = roomCreateRequestDto.toMemberEntity();
        memberRepository.createMember(masterMember);

        Room createdRoom = roomCreateRequestDto.toRoomEntity(masterMember.getMemberId());
        roomRepository.createRoom(createdRoom);
        return createdRoom.toRoomCreateResponseDto();
    }


}
