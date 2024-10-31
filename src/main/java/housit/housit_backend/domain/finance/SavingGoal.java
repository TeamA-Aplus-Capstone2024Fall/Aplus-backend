package housit.housit_backend.domain.finance;

import jakarta.persistence.*;
import lombok.Getter;

@Entity @Getter
public class SavingGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savingGoalId;

    @ManyToOne
    @JoinColumn(name = "financePlanId", nullable = false)
    private FinancePlan financePlan;

    private String description;
    private String amount;
    private Boolean isChecked;

    // Getters and Setters
}
