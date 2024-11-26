package greencity.repository;

import greencity.entity.Place;
import greencity.enums.PlaceStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Optional;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Page<Place> findPlacesByStatus(@NotNull PlaceStatus status, Pageable pageable);

    Optional<Place> findPlaceByName(@NotNull @NotEmpty
                                    @Length(min = 1, max = 30, message = "Place name should be from 1 to 30 characters long")
                                    String name);
}
