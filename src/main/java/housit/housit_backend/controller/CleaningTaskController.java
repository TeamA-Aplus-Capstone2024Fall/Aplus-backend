package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.CleaningTaskDto;
import housit.housit_backend.dto.reponse.EventDto;
import housit.housit_backend.dto.request.CleaningTaskSaveRequestDto;
import housit.housit_backend.dto.request.EventSaveRequestDto;
import housit.housit_backend.service.CleaningTaskService;
import housit.housit_backend.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CleaningTaskController {
    private final CleaningTaskService cleaningTaskService;

    @GetMapping("/room/{roomId}/cleans")
    public List<CleaningTaskDto> getEvents(@PathVariable Long roomId) {
        log.info("Get events from room {}", roomId);
        return cleaningTaskService.getCleaningTask(roomId);
    }

    @PostMapping("/room/{roomId}/cleans")
    public CleaningTaskDto createEvents(@PathVariable Long roomId,
                                 @RequestBody CleaningTaskSaveRequestDto cleaningTaskSaveDto) {
        return cleaningTaskService.createCleaningTask(roomId, cleaningTaskSaveDto);
    }

    @PutMapping("/room/{roomId}/cleans/{cleanTaskId}")
    public CleaningTaskDto updateEvents(@PathVariable Long roomId,
                                 @PathVariable Long cleanTaskId,
                                 @RequestBody CleaningTaskSaveRequestDto cleaningTaskSaveDto) {
        return cleaningTaskService.updateCleaningTask(roomId, cleanTaskId, cleaningTaskSaveDto);
    }

    @DeleteMapping("/room/{roomId}/cleans/{cleanTaskId}")
    public void deleteEvents(@PathVariable Long roomId,
                             @PathVariable Long cleanTaskId) {
        cleaningTaskService.deleteCleaningTask(roomId, cleanTaskId);
    }
}
