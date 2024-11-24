package greencity.repository;

import greencity.entity.Place;
import greencity.enums.PlaceStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Page<Place> findPlacesByStatus(@NotNull PlaceStatus status, Pageable pageable);
}
