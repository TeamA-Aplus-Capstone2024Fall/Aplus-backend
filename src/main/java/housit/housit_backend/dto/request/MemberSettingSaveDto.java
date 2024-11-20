package housit.housit_backend.dto.request;

import lombok.Getter;

@Getter
public class MemberSettingSaveDto {
    private Integer foodDays;
    private Integer financeDays;
    private Integer choreDays;
    private Integer eventDays;
    private Integer minimumFoodQuantity;
}
