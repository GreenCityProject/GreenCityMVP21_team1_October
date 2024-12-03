package greencity.repository;

import greencity.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(value = "SELECT l.id AS id, l.address AS address, l.lat AS lat, l.lng AS lng " +
                   "FROM locations l " +
                   "JOIN places p ON p.location_id = l.id " +
                   "JOIN users_favorite_places fup ON fup.place_id = p.id " +
                   "JOIN users u ON fup.user_id = u.id " +
                   "WHERE u.email = ?1 AND p.id = ?2",
            nativeQuery = true)
    Location getLocationByUserEmailAndPlaceId(String userEmail, long id);

    /**
     * Finds a location entity based on the given formatted address.
     *
     * @param address the full formatted address.
     * @return an {@link Optional} containing the {@link Location} if found, or an empty {@link Optional}
     *         if no matching location exists in the database.
     */
    Optional<Location> findByAddress(String address);
}
