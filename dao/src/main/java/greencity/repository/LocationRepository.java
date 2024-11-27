package greencity.repository;

import greencity.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {

    /**
     * Finds a location entity based on the given formatted address.
     *
     * @param address the full formatted address.
     * @return an {@link Optional} containing the {@link Location} if found, or an empty {@link Optional}
     *         if no matching location exists in the database.
     */
    Optional<Location> findByAddress(String address);
}
