package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.ChoreDto;
import housit.housit_backend.dto.request.ChoreSaveRequestDto;
import housit.housit_backend.service.ChoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChoreController {
    private final ChoreService choreService;

    @GetMapping("/room/{roomId}/chores")
    public List<ChoreDto> getEvents(@PathVariable Long roomId) {
        log.info("Get events from room {}", roomId);
        return choreService.getChore(roomId);
    }

    @PostMapping("/room/{roomId}/chores")
    public ChoreDto createEvents(@PathVariable Long roomId,
                                 @RequestBody ChoreSaveRequestDto choreSaveDto) {
        return choreService.createChore(roomId, choreSaveDto);
    }

    @PutMapping("/room/{roomId}/chores/{choreId}")
    public ChoreDto updateEvents(@PathVariable Long roomId,
                                 @PathVariable Long choreId,
                                 @RequestBody ChoreSaveRequestDto choreSaveDto) {
        return choreService.updateChore(roomId, choreId, choreSaveDto);
    }

    @DeleteMapping("/room/{roomId}/chores/{choreId}")
    public void deleteEvents(@PathVariable Long roomId,
                             @PathVariable Long choreId) {
        choreService.deleteChore(roomId, choreId);
    }
}
