package housit.housit_backend.domain.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.request.FinancePlanSaveDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity @Getter
public class SavingGoal implements FinancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingGoalId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    private Room room;

    private String description;
    private Long amount;
    private Boolean isChecked;
    private LocalDate dueDate;
    private LocalDate enrolledDate;

    public static SavingGoal createSavingGoal(FinancePlanSaveDto financePlanSaveDto, Room room) {
        SavingGoal savingGoal = new SavingGoal();
        savingGoal.description = financePlanSaveDto.getDescription();
        savingGoal.amount = financePlanSaveDto.getAmount();
        savingGoal.isChecked = financePlanSaveDto.getIsChecked();
        savingGoal.dueDate = financePlanSaveDto.getDueDate();
        savingGoal.enrolledDate = financePlanSaveDto.getEnrolledDate();
        savingGoal.room = room;
        return savingGoal;
    }

    public void update(FinancePlanSaveDto financePlanSaveDto) {
        this.description = financePlanSaveDto.getDescription();
        this.amount = financePlanSaveDto.getAmount();
        this.isChecked = financePlanSaveDto.getIsChecked();
        this.dueDate = financePlanSaveDto.getDueDate();
        this.enrolledDate = financePlanSaveDto.getEnrolledDate();
    }
}
