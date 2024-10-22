package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.room.Room;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventDto {
    private Long eventId;
    private String eventName;  // 이벤트명
    private LocalDate eventDay;   // 이벤트 날짜
    private LocalTime eventTime;  // 이벤트 시간

    private List<MemberDto> members = new ArrayList<>();
}
