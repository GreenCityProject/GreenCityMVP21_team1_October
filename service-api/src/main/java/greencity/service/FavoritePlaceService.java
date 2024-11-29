package greencity.service;

import greencity.dto.place.FavoritePlaceDto;
import greencity.dto.place.PlaceByBoundsDto;
import greencity.dto.user.UserVO;

import java.util.List;

public interface FavoritePlaceService {
    long deleteFavoritePlaceById(Long id, UserVO userVO);

    boolean existsByEmailAndPlaceId(String userEmail, long placeId);

    PlaceByBoundsDto getFavoriteByUserAndPlaceId(UserVO userVO, long id);

    List<PlaceByBoundsDto> getAllFavoritesByUser(UserVO userVO);

    String getFavoritePlaceName(long placeId, UserVO userVO);

    void updateFavoritePlace(FavoritePlaceDto favoritePlaceDto, UserVO userVO);
}
