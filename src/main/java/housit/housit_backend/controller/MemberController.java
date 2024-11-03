package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.request.MemberSaveDto;
import housit.housit_backend.dto.request.MemberDeleteDto;
import housit.housit_backend.security.JwtUtil;
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
    private final JwtUtil jwtUtil;

    @GetMapping("/room/{roomId}/member")
    public ResponseEntity<List<MemberDto>> enterRoom(@PathVariable("roomId") Long roomId, @RequestParam String roomPassword) {
        // 방 비밀번호 검증 실패 시 403 응답 반환
        if(!roomService.validateRoomPassword(roomId, roomPassword)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null); // 비어있는 body로 403 Forbidden 반환
        }

        List<MemberDto> members = roomService.getMembers(roomId);
        return ResponseEntity.ok(members);
    }

    @PostMapping("/room/{roomId}/member")
    public MemberDto createMember(@PathVariable("roomId") Long roomId,
                                  @RequestBody MemberSaveDto memberSaveDto) {
        return roomService.createMember(roomId, memberSaveDto);
    }

    @DeleteMapping("/room/{roomId}/member/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable("roomId") Long roomId,
                                             @PathVariable("memberId") Long memberId,
                                             @RequestBody MemberDeleteDto memberDeleteDto) {
        // 비밀번호 검증 실패 시 403 Forbidden 응답 반환
        if (!roomService.validateMemberPassword(memberId, memberDeleteDto.getMemberPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        roomService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/room/{roomId}/member/{memberId}")
    public MemberDto updateMember(@PathVariable("roomId") Long roomId,
                                  @PathVariable("memberId") Long memberId,
                                  @RequestBody MemberSaveDto memberSaveDto) {
        return roomService.updateMember(memberId, memberSaveDto);
    }

    @GetMapping("/room/{roomId}/member/{memberId}/join")
    public void joinRoom(@PathVariable("roomId") Long roomId,
                         @PathVariable("memberId") Long memberId) {

        String accessToken = jwtUtil.generateAccessToken(roomId, memberId);
        String refreshToken = jwtUtil.generateRefreshToken(roomId, memberId);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
    }
}
