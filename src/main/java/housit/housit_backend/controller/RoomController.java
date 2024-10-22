package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.RoomCreateResponseDto;
import housit.housit_backend.dto.reponse.MemberDto;
import housit.housit_backend.dto.request.MemberCreateRequestDto;
import housit.housit_backend.dto.request.RoomCreateRequestDto;
import housit.housit_backend.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        log.info(roomCreateRequestDto.toString());
        return roomService.roomCreate(roomCreateRequestDto);
    }

    @GetMapping("/room/{roomId}")
    public List<MemberDto> enterRoom(@PathVariable("roomId") Long roomId) {
        return new ArrayList<>();
    }

    @PostMapping("/room/{roomId}/member")
    public List<MemberDto> createMember(@PathVariable("roomId") Long roomId,
                                        @RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        return new ArrayList<>();
    }
}
