package housit.housit_backend.service;

import housit.housit_backend.domain.food.Food;
import housit.housit_backend.domain.room.Room;
import housit.housit_backend.dto.reponse.FoodDto;
import housit.housit_backend.dto.request.FoodSaveDto;
import housit.housit_backend.repository.FoodRepository;
import housit.housit_backend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public List<FoodDto> getFoods(Long roomId) {
        List<Food> foods = foodRepository.getAllFoods(roomId);
        List<FoodDto> foodDtos = new ArrayList<>();
        for (Food food : foods) {
            foodDtos.add(FoodDto.entityToDto(food));
        }
        return foodDtos;
    }

    @Transactional
    public FoodDto createFood(Long roomId, FoodSaveDto foodSaveDto) {
        // roomId로 Room을 조회하고 없으면 예외를 던진다
        Room room = roomRepository.findRoomById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Food food = foodSaveDto.toFoodEntity(room);

        foodRepository.saveFood(food);

        return FoodDto.entityToDto(food);  // 저장한 Food 엔티티 반환
    }

    @Transactional
    public FoodDto updateFood(Long foodId, FoodSaveDto foodSaveDto) {
        Optional<Food> food = foodRepository.findFoodById(foodId); // Food 는 영속 상태
        if(food.isPresent()) {
            food.get().updateFood(foodSaveDto);
            //foodRepository.saveFood(food.get()); // 굳이 saveFood 필요 없이 dirty checking 으로 트랜잭션 끝날 때 업데이트됨
            return FoodDto.entityToDto(food.get());
        }
        return null;
    }

    @Transactional
    public void deleteFood(Long foodId) {
        foodRepository.deleteFood(foodId);
    }
}
