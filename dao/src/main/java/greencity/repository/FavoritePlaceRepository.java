package greencity.repository;

import greencity.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FavoritePlaceRepository extends JpaRepository<Place, Long> {
    @Modifying
    @Query(value = "DELETE FROM users_favorite_places WHERE place_id = ?2 and " +
                   "user_id = (SELECT id FROM users WHERE email = ?1)", nativeQuery = true)
    void deleteByUserEmailAndPlaceId(String userEmail, long placeId);

    @Query(value = "SELECT EXISTS(SELECT * FROM users_favorite_places WHERE place_id = ?2 and user_id = (SELECT id FROM users WHERE email = ?1))", nativeQuery = true)
    boolean existsByEmailAndId(String userEmail, long placeId);
}
