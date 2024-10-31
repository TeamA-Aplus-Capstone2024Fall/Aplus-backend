package housit.housit_backend.domain.finance;

import jakarta.persistence.*;
import lombok.Getter;

@Entity @Getter
public class PredictedExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @ManyToOne
    @JoinColumn(name = "financePlanId", nullable = false)
    private FinancePlan financePlan;

    private String description;
    private String amount;
    private Boolean isChecked;
}
