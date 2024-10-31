package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.EventDto;
import housit.housit_backend.dto.request.EventSaveRequestDto;
import housit.housit_backend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/room/{roomId}/events")
    public List<EventDto> getEvents(@PathVariable Long roomId) {
        return eventService.getEvents(roomId);
    }

    @PostMapping("/room/{roomId}/events")
    public EventDto createEvents(@PathVariable Long roomId,
                                       @RequestBody EventSaveRequestDto eventSaveRequestDto) {
        return eventService.createEvent(roomId, eventSaveRequestDto);
    }

    @PutMapping("/room/{roomId}/events/{eventId}")
    public EventDto updateEvents(@PathVariable Long roomId,
                                       @PathVariable Long eventId,
                                       @RequestBody EventSaveRequestDto eventSaveRequestDto) {
        return eventService.updateEvent(roomId, eventId, eventSaveRequestDto);
    }
}
