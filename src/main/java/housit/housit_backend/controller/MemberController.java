package housit.housit_backend.controller;

import housit.housit_backend.domain.room.MemberIcon;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final RoomService roomService;

    @PostMapping("/room/{roomId}/member")
    public MemberDto createMember(@PathVariable("roomId") Long roomId,
                                  @RequestParam String memberName,
                                  @RequestParam String memberPassword,
                                  @RequestParam MemberIcon memberIcon) {
        return roomService.createMember(roomId, memberName, memberPassword, memberIcon);
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

    @PutMapping("/room/{roomId}/member")
    public MemberDto updateMember(@PathVariable("roomId") Long roomId,
                                  @RequestParam Long memberId,
                                  @RequestParam String memberName,
                                  @RequestParam String memberPassword,
                                  @RequestParam MemberIcon memberIcon) {
        return roomService.updateMember(memberId, memberName, memberPassword, memberIcon);
    }
}
