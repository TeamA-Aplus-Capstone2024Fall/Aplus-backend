package housit.housit_backend.controller;

import housit.housit_backend.domain.food.Food;
import housit.housit_backend.dto.request.FoodSaveRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class FoodController {

    // Food에는 민감정보 없으므로 엔티티 그대로 DTO로 넘김
    @GetMapping("/room/{roomId}/foods")
    public List<Food> getFoods(@PathVariable("roomId") Long roomId) {
        return null;
    }

    @PostMapping("/room/{roomId}/foods")
    public Food createFood(@PathVariable("roomId") Long roomId,
                                 @RequestBody FoodSaveRequestDto foodSaveRequestDto) {
        return new Food();
    }

    @PutMapping("/room/{roomId}/foods/{foodId}")
    public Food updateFood(@PathVariable("roomId") Long roomId,
                           @PathVariable("foodId") Long foodId,
                           @RequestBody FoodSaveRequestDto foodSaveRequestDto) {
        return new Food();
    }

}
