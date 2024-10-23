package housit.housit_backend.repository;

import housit.housit_backend.domain.food.Food;
import housit.housit_backend.domain.room.Member;

import java.util.List;
import java.util.Optional;

public interface FoodRepository {
    public Food saveFood(Food food);
    public Optional<Food> findFoodById(Long foodId);
    public void deleteFood(Long foodId);
    public List<Food> getAllFoods(Long roomId);
}
