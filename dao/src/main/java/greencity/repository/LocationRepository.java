package greencity.repository;

import greencity.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(value = "SELECT l.id AS id, l.address AS address, l.lat AS lat, l.lng AS lng " +
                   "FROM locations l " +
                   "JOIN places p ON p.location_id = l.id " +
                   "JOIN users_favorite_places fup ON fup.place_id = p.id " +
                   "JOIN users u ON fup.user_id = u.id " +
                   "WHERE u.email = ?1 AND p.id = ?2",
            nativeQuery = true)
    Location getLocationByUserEmailAndPlaceId(String userEmail, long id);
}
