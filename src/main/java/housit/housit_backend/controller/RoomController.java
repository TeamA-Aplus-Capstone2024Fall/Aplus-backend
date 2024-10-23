package housit.housit_backend.controller;

import housit.housit_backend.domain.room.MemberIcon;
import housit.housit_backend.dto.reponse.RoomCreateResponseDto;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.reponse.RoomDto;
import housit.housit_backend.dto.request.RoomCreateRequestDto;
import housit.housit_backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/room")
    public RoomCreateResponseDto createRoom(@RequestBody RoomCreateRequestDto roomCreateRequestDto) {
        return roomService.createRoom(roomCreateRequestDto);
    }

    @PostMapping("/room/{roomId}/member")
    public MemberDto createMember(@PathVariable("roomId") Long roomId,
                                  @RequestParam String memberName,
                                  @RequestParam String memberPassword,
                                  @RequestParam MemberIcon memberIcon) {
        return roomService.createMember(roomId, memberName, memberPassword, memberIcon);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<MemberDto>> enterRoom(@PathVariable("roomId") Long roomId, @RequestParam String roomPassword) {
        // 방 비밀번호 검증 실패 시 403 응답 반환
        if(!roomService.validateRoomPassword(roomId, roomPassword)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null); // 비어있는 body로 403 Forbidden 반환
        }

        List<MemberDto> members = roomService.getMembers(roomId);
        return ResponseEntity.ok(members);
    }

    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") Long roomId, @RequestParam String roomPassword) {
        // 비밀번호 검증 실패 시 403 Forbidden 응답 반환
        if (!roomService.validateRoomPassword(roomId, roomPassword)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/room/{roomId}/member")
    public ResponseEntity<Void> deleteMember(@PathVariable("roomId") Long roomId,
                                             @RequestParam Long memberId,
                                             @RequestParam String memberPassword) {
        // 비밀번호 검증 실패 시 403 Forbidden 응답 반환
        if (!roomService.validateMemberPassword(memberId, memberPassword)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        roomService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/room")
    public List<RoomDto> getRooms(Pageable pageable) {
        return roomService.getRoomsWithMembers(pageable);
    }
}