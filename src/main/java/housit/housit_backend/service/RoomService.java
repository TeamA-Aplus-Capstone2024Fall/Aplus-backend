package housit.housit_backend.service;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.reponse.RoomCreateResponseDto;
import housit.housit_backend.dto.reponse.RoomDto;
import housit.housit_backend.dto.request.MemberSaveRequestDto;
import housit.housit_backend.dto.request.RoomSaveRequestDto;
import housit.housit_backend.repository.MemberRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RoomCreateResponseDto createRoom(RoomSaveRequestDto roomSaveRequestDto) {
        Room createdRoom = roomSaveRequestDto.toRoomEntity();
        roomRepository.saveRoom(createdRoom);

        Member masterMember = roomSaveRequestDto.toMemberEntity(createdRoom);
        memberRepository.saveMember(masterMember);

        createdRoom.initializeMasterMemberRoomId(masterMember.getMemberId());
        roomRepository.saveRoom(createdRoom);

        return createdRoom.toRoomCreateResponseDto();
    }

    @Transactional
    public List<RoomDto> getRoomsWithMembers(Pageable pageable) {
        List<Room> allRooms = roomRepository.findAllRooms(pageable);
        List<RoomDto> roomDtos = new ArrayList<>();
        for (Room room : allRooms) {
            List<Member> members = memberRepository.getAllMembers(room.getRoomId());
            List<MemberDto> memberDtos = new ArrayList<>();
            for (Member member : members) memberDtos.add(MemberDto.entityToDto(member));
            RoomDto roomDto = new RoomDto(room, memberDtos);
            roomDtos.add(roomDto);
        }
        return roomDtos;
    }

    @Transactional
    public Boolean validateRoomPassword(Long roomId, String roomPassword) {
        Optional<Room> room = roomRepository.findRoomById(roomId);
        if (room.isEmpty()) return false;
        return room.get().validatePassword(roomPassword);
    }

    @Transactional
    public MemberDto createMember(Long roomId, MemberSaveRequestDto memberSaveRequestDto) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Member member = Member.createMember(memberSaveRequestDto, room);
        Member saveMember = memberRepository.saveMember(member);
        return MemberDto.entityToDto(saveMember);
    }

    @Transactional
    public List<MemberDto> getMembers(Long roomId) {
        List<Member> allMembers = memberRepository.getAllMembers(roomId);
        List<MemberDto> memberDtoList = new ArrayList<>();
        for (Member member : allMembers) {
            memberDtoList.add(MemberDto.entityToDto(member));
        }
        return memberDtoList;
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        roomRepository.deleteRoom(roomId);
    }

    @Transactional
    public Boolean validateMemberPassword(Long memberId, String memberPassword) {
        Member member = memberRepository.findMemberById(memberId);
        if (member == null) return false;
        return member.validatePassword(memberPassword);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteMember(memberId);
    }

    @Transactional
    public MemberDto updateMember(Long memberId, MemberSaveRequestDto memberSaveRequestDto) {
        Member member = memberRepository.findMemberById(memberId);
        if(member != null){
            member.updateMember(memberSaveRequestDto.getMemberName(),
                    memberSaveRequestDto.getMemberPassword(),
                    memberSaveRequestDto.getMemberIcon());
            memberRepository.saveMember(member);
            return MemberDto.entityToDto(member);
        }
        return null;
    }
}
