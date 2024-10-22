package housit.housit_backend.dto.request;

import housit.housit_backend.domain.food.StorageType;
import lombok.Data;
import java.time.LocalDate;

@Data
public class FoodSaveRequestDto {
    private String foodName;       // 음식 이름
    private LocalDate createAt;         // 구입 날짜 (or 보관 날짜)
    private LocalDate expirationDate;   // 유통기한
    private Integer quantity;      // 수량 (Nullable)
    private Integer amount;        // 남은 양(%) (Nullable)
    private Boolean isFavorite;    // 좋아하는 음식 여부
    private String memberName;     // 음식 주인 (Nullable)
    private Boolean isShared;      // 공유 음식 여부 (Nullable)
    private StorageType storageType;  // 보관 타입 (냉장, 냉동, 상온)
}
