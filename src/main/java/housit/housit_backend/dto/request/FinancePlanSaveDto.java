package housit.housit_backend.dto.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class FinancePlanSaveDto {
    private String description;
    private Long amount;
    private LocalDate enrolledDate;
    private LocalDate dueDate;
    private Boolean isChecked;
}
