package housit.housit_backend.domain.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.request.FinancePlanSaveDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity @Getter
public class PredictedExpense implements FinancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    private Room room;

    private String description;
    private Long amount;
    private Boolean isChecked;
    private LocalDate dueDate;
    private LocalDate enrolledDate;

    public static PredictedExpense createPredictedExpense(FinancePlanSaveDto financePlanSaveDto, Room room) {
        PredictedExpense predictedExpense = new PredictedExpense();
        predictedExpense.description = financePlanSaveDto.getDescription();
        predictedExpense.amount = financePlanSaveDto.getAmount();
        predictedExpense.isChecked = financePlanSaveDto.getIsChecked();
        predictedExpense.dueDate = financePlanSaveDto.getDueDate();
        predictedExpense.enrolledDate = financePlanSaveDto.getEnrolledDate();
        predictedExpense.room = room;
        return predictedExpense;
    }

    public void update(FinancePlanSaveDto financePlanSaveDto) {
        this.description = financePlanSaveDto.getDescription();
        this.amount = financePlanSaveDto.getAmount();
        this.isChecked = financePlanSaveDto.getIsChecked();
        this.dueDate = financePlanSaveDto.getDueDate();
        this.enrolledDate = financePlanSaveDto.getEnrolledDate();
    }
}
