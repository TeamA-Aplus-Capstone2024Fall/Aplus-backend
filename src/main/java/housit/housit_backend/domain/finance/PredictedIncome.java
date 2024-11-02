package housit.housit_backend.domain.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.request.FinancePlanSaveDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity @Getter
public class PredictedIncome implements FinancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long incomeId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    private Room room;

    private String description;
    private Long amount;
    private Boolean isChecked;
    private LocalDate dueDate;
    private LocalDate enrolledDate;

    public static PredictedIncome createPredictedIncome(FinancePlanSaveDto financePlanSaveDto, Room room) {
        PredictedIncome predictedIncome = new PredictedIncome();
        predictedIncome.description = financePlanSaveDto.getDescription();
        predictedIncome.amount = financePlanSaveDto.getAmount();
        predictedIncome.isChecked = financePlanSaveDto.getIsChecked();
        predictedIncome.dueDate = financePlanSaveDto.getDueDate();
        predictedIncome.enrolledDate = financePlanSaveDto.getEnrolledDate();
        predictedIncome.room = room;
        return predictedIncome;
    }
}
