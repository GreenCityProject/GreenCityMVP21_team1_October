package greencity.repository;

import greencity.entity.DiscountValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountValueRepository extends JpaRepository<DiscountValue, Integer> {
}
