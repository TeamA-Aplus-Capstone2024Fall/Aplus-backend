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

    // 토큰 X
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody RoomSaveDto roomSaveDto) {
        RoomCreateResponseDto response = roomService.createRoom(roomSaveDto);

        if (response == null) {
            // 이미 리소스가 존재할 경우 409 Conflict 반환
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Room already exists");
        }

        // 성공 시 201 Created와 함께 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    public HomeDto getHome(@PathVariable("roomId") Long roomId) {
        return roomService.getHome(roomId);
    }
}