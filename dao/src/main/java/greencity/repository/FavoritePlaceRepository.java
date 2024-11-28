package greencity.repository;

import greencity.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoritePlaceRepository extends JpaRepository<Place, Long> {
    @Modifying
    @Query(value = "DELETE FROM users_favorite_places WHERE place_id = ?2 AND " +
                   "user_id = (SELECT id FROM users WHERE email = ?1)",
            nativeQuery = true)
    void deleteByUserEmailAndPlaceId(String userEmail, long placeId);

    @Query(value = "SELECT EXISTS(SELECT * FROM users_favorite_places" +
                   " WHERE place_id = ?2 and user_id = (SELECT id FROM users WHERE email = ?1))",
            nativeQuery = true)
    boolean existsByEmailAndId(String userEmail, long placeId);

    @Query(nativeQuery = true, value = "SELECT place_id FROM users_favorite_places" +
                                       " WHERE user_id = (SELECT id FROM users WHERE email = ?1)")
    List<Long> getAllByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT name FROM users_favorite_places WHERE place_id = ?1" +
                                       " AND user_id = (SELECT id FROM users WHERE email = ?2)")
    String getFavoritePlaceNameByIdAndEmail(long placeId, String email);

    @Modifying
    @Query(nativeQuery = true,
            value = """
                    UPDATE users_favorite_places SET name = ?2
                    WHERE place_id = ?1 AND user_id = (SELECT id FROM users WHERE email = ?3)
                   """)
    void updateFavoritePlace(long placeId, String name, String email);
}
