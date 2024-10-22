package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.RoomCreateResponseDto;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.reponse.RoomDto;
import housit.housit_backend.dto.request.MemberCreateRequestDto;
import housit.housit_backend.dto.request.RoomCreateRequestDto;
import housit.housit_backend.service.RoomService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/room")
    public List<RoomDto> getRooms(Pageable pageable) {
        return roomService.getRoomsWithMembers(pageable);
    }

    @PostMapping("/room")
    public RoomCreateResponseDto createRoom(@RequestBody RoomCreateRequestDto roomCreateRequestDto) {
        return roomService.createRoom(roomCreateRequestDto);
    }

    @GetMapping("/room/{roomId}")
    public List<MemberDto> enterRoom(@PathVariable("roomId") Long roomId, @RequestParam String roomPassword) {
        if(!roomService.validateRoomPassword(roomId, roomPassword))
            return null;// 여기서 예외 터지면 어떻게 핸들링?

        return roomService.getMembers(roomId);
    }

    @DeleteMapping("/room/{roomId}")
    public void deleteRoom(@PathVariable("roomId") Long roomId) {

    }

    @PostMapping("/room/{roomId}/member")
    public List<MemberDto> createMember(@PathVariable("roomId") Long roomId,
                                        @RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        return new ArrayList<>();
    }


}
