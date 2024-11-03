package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.finance.PredictedExpense;
import housit.housit_backend.domain.finance.PredictedIncome;
import housit.housit_backend.domain.finance.SavingGoal;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HomeDto {
    private List<FoodDto> expiringSoonFoods = new ArrayList<>();
    private List<FoodDto> outOfFavoriteFoods = new ArrayList<>();
    private List<PredictedIncome> predictedIncomes = new ArrayList<>();
    private List<SavingGoal> savingGoals = new ArrayList<>();
    private List<PredictedExpense> predictedExpenses = new ArrayList<>();
    private List<ChoreDto> choreDtos = new ArrayList<>();
    private List<EventDto> eventDtos = new ArrayList<>();

    public HomeDto(List<FoodDto> expiringSoonFoods, List<FoodDto> outOfFavoriteFoods, List<PredictedIncome> predictedIncomes, List<SavingGoal> savingGoals, List<PredictedExpense> predictedExpenses, List<ChoreDto> choreDtos, List<EventDto> eventDtos) {
        this.expiringSoonFoods = expiringSoonFoods;
        this.outOfFavoriteFoods = outOfFavoriteFoods;
        this.predictedIncomes = predictedIncomes;
        this.savingGoals = savingGoals;
        this.predictedExpenses = predictedExpenses;
        this.choreDtos = choreDtos;
        this.eventDtos = eventDtos;
    }
}
