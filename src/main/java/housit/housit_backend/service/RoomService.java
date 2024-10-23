package housit.housit_backend.service;

import housit.housit_backend.domain.room.Member;
import housit.housit_backend.domain.room.MemberIcon;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.reponse.RoomCreateResponseDto;
import housit.housit_backend.dto.reponse.RoomDto;
import housit.housit_backend.dto.request.RoomCreateRequestDto;
import housit.housit_backend.repository.MemberRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

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
    public Boolean validateRoomPassword(Long roomId, String roomPassword) {
        Optional<Room> room = roomRepository.findRoomById(roomId);
        if (room.isEmpty()) return false;
        return room.get().validatePassword(roomPassword);
    }

    @Transactional
    public MemberDto createMember(Long roomId,
                                        String memberName,
                                        String memberPassword,
                                        MemberIcon memberIcon) {
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        Member member = Member.createMember(memberName, memberPassword, memberIcon, room);
        Member saveMember = memberRepository.saveMember(member);
        return saveMember.toMemberDto();
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
    public void deleteRoom(Long roomId) {
        roomRepository.deleteRoom(roomId);
    }

    @Transactional
    public Boolean validateMemberPassword(Long memberId, String memberPassword) {
        Optional<Member> member = memberRepository.findMemberById(memberId);
        if (member.isEmpty()) return false;
        return member.get().validatePassword(memberPassword);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteMember(memberId);
    }

    @Transactional
    public MemberDto updateMember(Long memberId, String memberName, String memberPassword, MemberIcon memberIcon) {
        Optional<Member> member = memberRepository.findMemberById(memberId);
        if(member.isPresent()){
            member.get().updateMember(memberName, memberPassword, memberIcon);
            memberRepository.saveMember(member.get());
            return member.get().toMemberDto();
        }
        return null;
    }
}
