package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.EventDto;
import housit.housit_backend.dto.request.EventSaveRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EventController {
    @GetMapping("/room/{roomId}/events")
    public List<EventDto> getEvents(@PathVariable Long roomId) {

        return null;
    }

    @PostMapping("/room/{roomId}/events")
    public List<EventDto> createEvents(@PathVariable Long roomId,
                                       @RequestBody EventSaveRequestDto eventSaveRequestDto) {
        return null;
    }


    @PutMapping("/room/{roomId}/events")
    public List<EventDto> updateEvents(@PathVariable Long roomId,
                                       @RequestBody EventSaveRequestDto eventSaveRequestDto) {
        return null;
    }
}
