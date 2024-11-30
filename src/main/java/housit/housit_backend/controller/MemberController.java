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

    @PostMapping("/room/{roomId}/member/{memberId}/join")
    public ResponseEntity<Boolean> memberLogin(@PathVariable("roomId") Long roomId,
                                               @PathVariable("memberId") Long memberId,
                                               @RequestBody Map<String, Object> data) {
        String memberPassword = (String) data.get("memberPassword");

        // 비밀번호 검증
        Boolean isValidPassword = roomService.validateMemberPassword(memberId, memberPassword);

        // 로그인 실패시 401 Unauthorized
        if (!isValidPassword) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(false); // 실패를 나타내는 Boolean 값 반환
        }

        // 로그인 성공시 true 반환
        return ResponseEntity.ok(true);
    }

    // Room 토큰 필요
    @PostMapping("/room/{roomId}/member")
    public ResponseEntity<?> createMember(@PathVariable("roomId") Long roomId,
                                                  @RequestBody MemberSaveDto memberSaveDto) {
        String validMemberSaveDto = isValidMemberSaveDto(memberSaveDto);
        if (validMemberSaveDto != null) {
            return ResponseEntity.badRequest().body(validMemberSaveDto); // 400 Bad Request 반환
        }
        MemberDto createdMember = roomService.createMember(roomId, memberSaveDto);
        return ResponseEntity.ok(createdMember); // 성공 시 200 OK와 함께 결과 반환
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
    public ResponseEntity<?> updateMember(@PathVariable("roomId") Long roomId,
                                  @PathVariable("memberId") Long memberId,
                                  @RequestBody MemberSaveDto memberSaveDto) {
        String validMemberSaveDto = isValidMemberSaveDto(memberSaveDto);
        if (validMemberSaveDto != null) {
            return ResponseEntity.badRequest().body(validMemberSaveDto); // 400 Bad Request 반환
        }
        return ResponseEntity.ok(roomService.updateMember(memberId, memberSaveDto));
    }

    // 멤버 세팅 변경
    @PutMapping("/room/{roomId}/member/{memberId}/settings")
    public ResponseEntity<?> updateMemberSetting(@PathVariable("roomId") Long roomId,
                                  @PathVariable("memberId") Long memberId,
                                  @RequestBody MemberSettingSaveDto memberSettingSaveDto) {
        String validMemberSettingSaveDto = isValidMemberSettingSaveDto(memberSettingSaveDto);
        if (validMemberSettingSaveDto != null) {
            return ResponseEntity.badRequest().body(validMemberSettingSaveDto); // 400 Bad Request 반환
        }
        return ResponseEntity.ok(roomService.updateMemberSetting(memberId, memberSettingSaveDto));
    }

    // Room 토큰 필요, Member 토큰 발급
    @GetMapping("/room/{roomId}/member/{memberId}/login")
    public void joinRoom(@PathVariable("roomId") Long roomId,
                         @PathVariable("memberId") Long memberId) {

    }

    private String isValidMemberSaveDto(MemberSaveDto memberSaveDto) {
        if (memberSaveDto.getMemberName() == null || memberSaveDto.getMemberName().isEmpty()) return "MemberName is empty";
        if (memberSaveDto.getMemberName().length() < 2 || memberSaveDto.getMemberName().length() > 30) return "MemberName length is invalid";
        String memberPassword = memberSaveDto.getMemberPassword();
        if (memberPassword == null || memberPassword.isEmpty()) return null;
        if (!memberPassword.matches("\\d{4}")) return "MemberPassword is invalid";

        return null;
    }

    private String isValidMemberSettingSaveDto(MemberSettingSaveDto memberSettingSaveDto) {
        if (memberSettingSaveDto.getFoodDays() < 0) return "FoodDays is invalid";
        if (memberSettingSaveDto.getFinanceDays() < 0) return "FinanceDays is invalid";
        if (memberSettingSaveDto.getChoreDays() < 0) return "ChoreDays is invalid";
        if (memberSettingSaveDto.getEventDays() < 0) return "EventDays is invalid";
        if (memberSettingSaveDto.getMinimumFoodQuantity() < 0) return "MinimumFoodQuantity is invalid";

        return null;
    }
}
