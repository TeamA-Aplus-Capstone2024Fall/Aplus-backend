package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.HomeDto;
import housit.housit_backend.dto.reponse.RoomCreateResponseDto;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.reponse.RoomDto;
import housit.housit_backend.dto.request.RoomSaveDto;
import housit.housit_backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    // 토큰 X
    @GetMapping("/room")
    public List<RoomDto> getRooms(Pageable pageable) {
        return roomService.getRoomsWithMembers(pageable);
    }

    @PostMapping("/room/roomId")
    public ResponseEntity<Long> getRoomId(@RequestBody Map<String, String> requestBody) {
        String roomName = requestBody.get("roomName");
        Long roomId = roomService.getRoomId(roomName);
        // roomId 이 null 인 경우 404 Not Found 반환
        if (roomId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(roomId);
    }

    // 토큰 X
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody RoomSaveDto roomSaveDto) {
        String validRoomSaveDto = isValidRoomSaveDto(roomSaveDto);
        if(validRoomSaveDto != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validRoomSaveDto);
        }
        RoomCreateResponseDto response = roomService.createRoom(roomSaveDto);

        if (response == null) {
            // 이미 리소스가 존재할 경우 409 Conflict 반환
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Room already exists");
        }

        // 성공 시 201 Created와 함께 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private String isValidRoomSaveDto(RoomSaveDto roomSaveDto) {
        if (roomSaveDto.getRoomName() == null || roomSaveDto.getRoomName().isEmpty()) return "RoomName is empty";
        if (roomSaveDto.getRoomName().length() < 2 || roomSaveDto.getRoomName().length() > 30) return "RoomName length is invalid";

        String roomPassword = roomSaveDto.getRoomPassword();
        if (roomPassword == null || !roomPassword.matches("\\d{6}")) return "RoomPassword is invalid";

        String masterMemberName = roomSaveDto.getMasterMemberName();
        if (masterMemberName == null || masterMemberName.isEmpty()) return "MasterMemberName is empty";
        if (masterMemberName.length() < 2 || masterMemberName.length() > 30) return "MasterMemberName length is invalid";
        String masterMemberPassword = roomSaveDto.getMasterMemberPassword();
        if (Objects.equals(masterMemberPassword, "")) return null;
        if (masterMemberPassword == null) return null;
        if (!masterMemberPassword.matches("\\d{4}")) return "MasterMemberPassword is invalid";

        return null;
    }


    // roomPassword 필요
    @DeleteMapping("/room/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") Long roomId,
                                           @RequestBody Map<String, Object> data) {
        String roomPassword = (String) data.get("roomPassword");
        // 비밀번호 검증 실패 시 403 Forbidden 응답 반환
        if (!roomService.validateRoomPassword(roomId, roomPassword)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        roomService.deleteRoom(roomId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Room 토큰 필요, Member 토큰 필요
    @GetMapping("/room/{roomId}/home")
    public HomeDto getHome(@PathVariable("roomId") Long roomId, @RequestParam Long memberId) {
        return roomService.getHome(roomId, memberId);
    }
}