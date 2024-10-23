package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.food.StorageType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FoodListDto {
    private List<FoodDto> refrigerated = new ArrayList<FoodDto>();
    private List<FoodDto> frozen = new ArrayList<FoodDto>();
    private List<FoodDto> roomTemperature = new ArrayList<FoodDto>();

    public FoodListDto(List<FoodDto> foods) {
        for (FoodDto food : foods) {
            if(food.getStorageType().equals(StorageType.refrigerated)) refrigerated.add(food);
            if(food.getStorageType().equals(StorageType.frozen)) frozen.add(food);
            if(food.getStorageType().equals(StorageType.roomTemperature)) roomTemperature.add(food);
        }
    }
}
