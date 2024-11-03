package housit.housit_backend.repository.jpa;

import housit.housit_backend.domain.food.Food;
import housit.housit_backend.domain.room.Member;
import housit.housit_backend.repository.FoodRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaFoodRepository implements FoodRepository {
    private final EntityManager em;

    @Override
    public void saveFood(Food food) {
        em.persist(food);
    }

    @Override
    public Optional<Food> findFoodById(Long foodId) {
        return Optional.ofNullable(em.find(Food.class, foodId));
    }

    @Override
    public void deleteFood(Long foodId) {
        Optional<Food> food = findFoodById(foodId);
        food.ifPresent(em::remove);
    }

    @Override
    public List<Food> getAllFoods(Long roomId) {
        return em.createQuery("SELECT f FROM Food f WHERE f.room.id = :roomId " +
                        "ORDER BY CASE f.storageType " +
                        "WHEN 'refrigerated' THEN 1 " +
                        "WHEN 'frozen' THEN 2 " +
                        "WHEN 'roomTemperature' THEN 3 " +
                        "END, f.expirationDate", Food.class)  // room의 id를 조건으로 조회
                .setParameter("roomId", roomId)  // 파라미터 바인딩
                .getResultList();
    }

    @Override
    public List<Food> getExpiringSoonFoods(Long roomId, Integer days) {
        // 현재 날짜에 days를 더해서 만료 기준 날짜를 계산합니다.
        LocalDate expirationThreshold = LocalDate.now().plusDays(days);

        return em.createQuery("SELECT f FROM Food f WHERE f.room.id = :roomId " +
                        "AND f.expirationDate <= :expirationThreshold", Food.class)
                .setParameter("roomId", roomId)
                .setParameter("expirationThreshold", expirationThreshold)  // 계산된 날짜를 전달
                .getResultList();
    }
    
    @Override
    public List<Food> getOutOfFavoriteFoods(Long roomId, Integer minimumQuantity) {
        return em.createQuery("SELECT f FROM Food f WHERE f.room.id = :roomId AND f.isFavorite = true AND f.quantity <= :minimumQuantity", Food.class)
                .setParameter("roomId", roomId)
                .setParameter("minimumQuantity", minimumQuantity)
                .getResultList();
    }
}
