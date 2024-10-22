package housit.housit_backend.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class EventSaveRequestDto {
    private String eventName;  // 이벤트명
    private LocalDate eventDay;   // 이벤트 날짜
    private LocalTime eventTime;  // 이벤트 시간
    private List<Long> memberIds = new ArrayList<>();
}
