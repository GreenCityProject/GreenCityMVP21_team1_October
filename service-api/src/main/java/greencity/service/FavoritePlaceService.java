package greencity.service;

import greencity.dto.user.UserVO;

public interface FavoritePlaceService {
    long deleteFavoritePlaceById(Long id, UserVO userVO);
    boolean existsByEmailAndId(String userEmail, long placeId);
}
