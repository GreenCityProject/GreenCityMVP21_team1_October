package greencity.repository;

import greencity.entity.Place;
import greencity.enums.PlaceStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlaceRepository extends JpaRepository<Place, Long>, JpaSpecificationExecutor<Place> {
    Page<Place> findPlacesByStatus(@NotNull PlaceStatus status, Pageable pageable);

    Optional<Place> findPlaceByName(@NotNull @NotEmpty
                                    @Length(min = 1, max = 30, message = "Place name should be from 1 to 30 characters long")
                                    String name);

    @Transactional
    @Modifying
    @Query("UPDATE Place p SET p.status = :status WHERE p.id IN :ids")
    void updatePlacesStatus(List<Long> ids, @NotNull PlaceStatus status);
}
