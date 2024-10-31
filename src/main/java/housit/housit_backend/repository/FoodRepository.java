package housit.housit_backend.repository;

import housit.housit_backend.domain.food.Food;
import housit.housit_backend.domain.room.Member;

import java.util.List;
import java.util.Optional;

public interface FoodRepository {
    void saveFood(Food food);
    Optional<Food> findFoodById(Long foodId);
    void deleteFood(Long foodId);
    List<Food> getAllFoods(Long roomId);
}
