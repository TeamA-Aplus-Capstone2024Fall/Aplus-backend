package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.request.MemberSaveDto;
import housit.housit_backend.dto.request.MemberDeleteDto;
import housit.housit_backend.dto.request.MemberSettingSaveDto;
import housit.housit_backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final RoomService roomService;

    // roomPassword 필요
    @PostMapping("/room/{roomId}/join")
    public ResponseEntity<List<MemberDto>> enterRoom(@PathVariable("roomId") Long roomId,
                                                     @RequestBody Map<String, Object> data) {
        String roomPassword = (String) data.get("roomPassword");
        // 방 비밀번호 검증 실패 시 403 응답 반환
        if(!roomService.validateRoomPassword(roomId, roomPassword)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null); // 비어있는 body로 403 Forbidden 반환
        }

        List<MemberDto> members = roomService.getMembers(roomId);
        return ResponseEntity.ok(members);
    }

    // Room 토큰 필요
    @PostMapping("/room/{roomId}/member")
    public MemberDto createMember(@PathVariable("roomId") Long roomId,
                                  @RequestBody MemberSaveDto memberSaveDto) {
        return roomService.createMember(roomId, memberSaveDto);
    }

    // Room 토큰 필요, memberPassword 필요
    @DeleteMapping("/room/{roomId}/member/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable("roomId") Long roomId,
                                             @PathVariable("memberId") Long memberId,
                                             @RequestBody Map<String, Object> data) {
        String memberPassword = (String) data.get("memberPassword");

        // 비밀번호 검증 실패 시 403 Forbidden 응답 반환
        if (!roomService.validateMemberPassword(memberId, memberPassword)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        roomService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Room 토큰 필요, memberPassword 필요
    @PutMapping("/room/{roomId}/member/{memberId}")
    public MemberDto updateMember(@PathVariable("roomId") Long roomId,
                                  @PathVariable("memberId") Long memberId,
                                  @RequestBody MemberSaveDto memberSaveDto) {
        return roomService.updateMember(memberId, memberSaveDto);
    }

    // 멤버 세팅 변경
    @PutMapping("/room/{roomId}/member/{memberId}/settings")
    public MemberDto updateMemberSetting(@PathVariable("roomId") Long roomId,
                                  @PathVariable("memberId") Long memberId,
                                  @RequestBody MemberSettingSaveDto memberSettingSaveDto) {
        return roomService.updateMemberSetting(memberId, memberSettingSaveDto);
    }

    // Room 토큰 필요, Member 토큰 발급
    @GetMapping("/room/{roomId}/member/{memberId}/login")
    public void joinRoom(@PathVariable("roomId") Long roomId,
                         @PathVariable("memberId") Long memberId) {

    }
}
