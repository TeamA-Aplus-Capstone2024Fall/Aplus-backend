package housit.housit_backend.service;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.reponse.RoomCreateResponseDto;
import housit.housit_backend.dto.reponse.RoomDto;
import housit.housit_backend.dto.request.RoomCreateRequestDto;
import housit.housit_backend.repository.MemberRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<RoomDto> getRoomsWithMembers(Pageable pageable) {
        List<Room> allRooms = roomRepository.findAllRooms(pageable);
        List<RoomDto> roomDtos = new ArrayList<>();
        for (Room room : allRooms) {
            List<Member> members = memberRepository.getAllMembers(room.getRoomId());
            List<MemberDto> memberDtos = new ArrayList<>();
            for (Member member : members) memberDtos.add(member.toMemberDto());
            RoomDto roomDto = new RoomDto(room, memberDtos);
            roomDtos.add(roomDto);
        }
        return roomDtos;
    }

    @Transactional
    public RoomCreateResponseDto createRoom(RoomCreateRequestDto roomCreateRequestDto) {
        Room createdRoom = roomCreateRequestDto.toRoomEntity();
        roomRepository.saveRoom(createdRoom);

        Member masterMember = roomCreateRequestDto.toMemberEntity(createdRoom);
        memberRepository.saveMember(masterMember);

        createdRoom.initializeMasterMemberRoomId(masterMember.getMemberId());
        roomRepository.saveRoom(createdRoom);

        return createdRoom.toRoomCreateResponseDto();
    }

    @Transactional
    public Boolean validateRoomPassword(Long roomId, String roomPassword) {
        return roomRepository.findRoomById(roomId).validatePassword(roomPassword);
    }

    @Transactional
    public List<MemberDto> getMembers(Long roomId) {
        List<Member> allMembers = memberRepository.getAllMembers(roomId);
        List<MemberDto> memberDtoList = new ArrayList<>();
        for (Member member : allMembers) {
            memberDtoList.add(member.toMemberDto());
        }
        return memberDtoList;
    }

    @Transactional
    void deleteRoom(Long roomId) {
        roomRepository.deleteRoom(roomId);
    }
}
