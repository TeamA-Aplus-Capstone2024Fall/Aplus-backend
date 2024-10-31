package housit.housit_backend.domain.food;

import housit.housit_backend.dto.reponse.FoodDto;
import housit.housit_backend.dto.request.FoodSaveRequestDto;
import jakarta.persistence.*;
import lombok.*;
import housit.housit_backend.domain.room.Room;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;

@Builder
@Entity @Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    @Column(nullable = false)
    private String foodName;       // 음식 이름
    private LocalDate createAt;         // 구입 날짜 (or 보관 날짜)
    private LocalDate expirationDate;   // 유통기한
    private Integer minimumQuantity;
    private Integer quantity;      // 수량 (Nullable)
    private Integer amount;        // 남은 양(%) (Nullable)
    @Column(nullable = false)
    private Boolean isFavorite;    // 좋아하는 음식 여부
    private String memberName;     // 음식 주인 (Nullable)
    private Boolean isShared;      // 공유 음식 여부 (Nullable)

    @Enumerated(EnumType.STRING)
    private StorageType storageType;  // 보관 타입 (냉장, 냉동, 상온)

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "roomId")
    private Room room;

    public void updateFood(FoodSaveRequestDto dto) {
        this.foodName = dto.getFoodName();
        this.createAt = dto.getCreateAt();
        this.expirationDate = dto.getExpirationDate();
        this.quantity = dto.getQuantity();
        this.amount = dto.getAmount();
        this.isFavorite = dto.getIsFavorite();
        this.memberName = dto.getMemberName();
        this.isShared = dto.getIsShared();
        this.storageType = dto.getStorageType();
    }
}