package housit.housit_backend.controller;

import housit.housit_backend.domain.room.MemberIcon;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.request.MemberSaveRequestDto;
import housit.housit_backend.dto.request.MemberDeleteRequestDto;
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
                                  @RequestBody MemberSaveRequestDto memberSaveRequestDto) {
        return roomService.createMember(roomId, memberSaveRequestDto);
    }

    @DeleteMapping("/room/{roomId}/member/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable("roomId") Long roomId,
                                             @PathVariable("memberId") Long memberId,
                                             @RequestBody MemberDeleteRequestDto memberDeleteRequestDto) {
        // 비밀번호 검증 실패 시 403 Forbidden 응답 반환
        if (!roomService.validateMemberPassword(memberId, memberDeleteRequestDto.getMemberPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        roomService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/room/{roomId}/member/{memberId}")
    public MemberDto updateMember(@PathVariable("roomId") Long roomId,
                                  @PathVariable("memberId") Long memberId,
                                  @RequestBody MemberSaveRequestDto memberSaveRequestDto) {
        return roomService.updateMember(memberId, memberSaveRequestDto);
    }
}
