package housit.housit_backend.domain.finance;

import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.request.FinancePlanSaveDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity @Getter
public class PredictedExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    private Room room;

    private String description;
    private Long amount;
    private Boolean isChecked;
    private LocalDate dueDate;

    public static PredictedExpense createPredictedExpense(FinancePlanSaveDto financePlanSaveDto, Room room) {
        PredictedExpense predictedExpense = new PredictedExpense();
        predictedExpense.description = financePlanSaveDto.getDescription();
        predictedExpense.amount = financePlanSaveDto.getAmount();
        predictedExpense.isChecked = financePlanSaveDto.getIsChecked();
        predictedExpense.dueDate = financePlanSaveDto.getDueDate();
        predictedExpense.room = room;
        return predictedExpense;
    }
}
