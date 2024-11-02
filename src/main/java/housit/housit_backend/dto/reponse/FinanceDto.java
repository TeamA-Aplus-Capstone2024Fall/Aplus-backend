package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.finance.Account;
import housit.housit_backend.domain.finance.PredictedExpense;
import housit.housit_backend.domain.finance.PredictedIncome;
import housit.housit_backend.domain.finance.SavingGoal;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FinanceDto {
    @Setter
    private List<Account> accounts = new ArrayList<>();
    private Long income;
    private Long expense;
    private List<PredictedIncome> predictedIncomes = new ArrayList<>();
    private List<SavingGoal> savingGoals = new ArrayList<>();
    private List<PredictedExpense> predictedExpenses = new ArrayList<>();

    public FinanceDto(List<Account> accounts, Long income, Long expense,
                      List<PredictedIncome> predictedIncomes,
                      List<SavingGoal> savingGoals,
                      List<PredictedExpense> predictedExpenses) {
        this.accounts = accounts;
        this.income = income;
        this.expense = expense;
        this.predictedIncomes = predictedIncomes;
        this.savingGoals = savingGoals;
        this.predictedExpenses = predictedExpenses;
    }
}
