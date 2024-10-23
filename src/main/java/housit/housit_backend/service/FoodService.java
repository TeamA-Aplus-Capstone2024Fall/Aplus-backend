package housit.housit_backend.service;

import housit.housit_backend.domain.food.Food;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.FoodDto;
import housit.housit_backend.dto.request.FoodSaveRequestDto;
import housit.housit_backend.repository.FoodRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public List<FoodDto> getFoods(Long roomId) {
        List<Food> foods = foodRepository.getAllFoods(roomId);
        return foods.stream().map(Food::toFoodDto).collect(Collectors.toList());
    }

    @Transactional
    public FoodDto createFood(Long roomId, FoodSaveRequestDto foodSaveRequestDto) {
        // roomId로 Room을 조회하고 없으면 예외를 던진다
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Food food = foodSaveRequestDto.toFoodEntity(room);

        foodRepository.saveFood(food);

        return food.toFoodDto();  // 저장한 Food 엔티티 반환
    }

    @Transactional
    public FoodDto updateFood(Long foodId, FoodSaveRequestDto foodSaveRequestDto) {
        Optional<Food> food = foodRepository.findFoodById(foodId);
        if(food.isPresent()) {
            food.get().updateFood(foodSaveRequestDto);
            foodRepository.saveFood(food.get());
            return food.get().toFoodDto();
        }
        return null;
    }

    @Transactional
    public void deleteFood(Long foodId) {
        foodRepository.deleteFood(foodId);
    }
}
