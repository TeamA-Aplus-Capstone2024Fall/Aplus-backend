package housit.housit_backend.dto.request;

import housit.housit_backend.domain.chore.ChoreDay;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChoreSaveRequestDto {
    private String choreArea;  // 청소 구역
    private String color;
    private String description;
    private String icon;
    private ChoreDay choreDay;  // 청소 요일 (Nullable)
    private Integer choreFrequency;  // 청소 주기 (예: 주 1회)
    private List<Long> memberIds = new ArrayList<>();
}
