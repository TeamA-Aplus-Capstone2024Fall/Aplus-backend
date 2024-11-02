package housit.housit_backend.controller;

import housit.housit_backend.dto.reponse.FoodDto;
import housit.housit_backend.dto.reponse.FoodListDto;
import housit.housit_backend.dto.request.FoodSaveDto;
import housit.housit_backend.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @GetMapping("/room/{roomId}/foods")
    public FoodListDto getFoods(@PathVariable("roomId") Long roomId) {
        List<FoodDto> foods = foodService.getFoods(roomId);
        return new FoodListDto(foods);
    }

    @PostMapping("/room/{roomId}/foods")
    public FoodDto createFood(@PathVariable("roomId") Long roomId,
                                 @RequestBody FoodSaveDto foodSaveDto) {
        return foodService.createFood(roomId, foodSaveDto);
    }

    @PutMapping("/room/{roomId}/foods/{foodId}")
    public FoodDto updateFood(@PathVariable("roomId") Long roomId,
                           @PathVariable("foodId") Long foodId,
                           @RequestBody FoodSaveDto foodSaveDto) {
        return foodService.updateFood(foodId, foodSaveDto);
    }

    @DeleteMapping("/room/{roomId}/foods/{foodId}")
    public void deleteFood(@PathVariable("roomId") Long roomId,
                           @PathVariable("foodId") Long foodId) {
        foodService.deleteFood(foodId);
    }
}
