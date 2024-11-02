package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.finance.PredictedExpense;
import housit.housit_backend.domain.finance.PredictedIncome;
import housit.housit_backend.domain.finance.SavingGoal;
import lombok.Getter;

import java.util.List;

@Getter
public class FinancePlanDto {
    private List<PredictedIncome> predictedIncomes;
    private List<SavingGoal> savingGoals;
    private List<PredictedExpense> predictedExpenses;

    private Long incomeAmount;
    private Long savingAmount;
    private Long expenseAmount;
}
