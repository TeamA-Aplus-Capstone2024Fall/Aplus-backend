package housit.housit_backend.dto.reponse;

import housit.housit_backend.domain.food.Food;
import housit.housit_backend.domain.food.StorageType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FoodDto {
    private Long foodId;
    private String foodName;       // 음식 이름
    private LocalDate createAt;         // 구입 날짜 (or 보관 날짜)
    private LocalDate expirationDate;   // 유통기한
    private Integer minimumQuantity;
    private Integer quantity;      // 수량 (Nullable)
    private Integer amount;        // 남은 양(%) (Nullable)
    private Boolean isFavorite;    // 좋아하는 음식 여부
    private String memberName;     // 음식 주인 (Nullable)
    private Boolean isShared;      // 공유 음식 여부 (Nullable)
    private StorageType storageType;  // 보관 타입 (냉장, 냉동, 상온)

    public static FoodDto entityToDto(Food food) {
        return FoodDto.builder()
                .foodId(food.getFoodId())
                .foodName(food.getFoodName())
                .createAt(food.getCreateAt())
                .expirationDate(food.getExpirationDate())
                .minimumQuantity(food.getMinimumQuantity())
                .quantity(food.getQuantity())
                .amount(food.getAmount())
                .isFavorite(food.getIsFavorite())
                .memberName(food.getMemberName())
                .isShared(food.getIsShared())
                .storageType(food.getStorageType())
                .build();
    }
}
