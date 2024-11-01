package housit.housit_backend.dto.request;

import housit.housit_backend.domain.cleaning.CleaningDay;
import housit.housit_backend.dto.reponse.MemberDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CleaningTaskSaveRequestDto {
    private String cleaningArea;  // 청소 구역
    private String color;
    private String description;
    private String icon;
    private CleaningDay cleaningDay;  // 청소 요일 (Nullable)
    private Integer cleaningFrequency;  // 청소 주기 (예: 주 1회)
    private List<Long> memberIds = new ArrayList<>();
}
