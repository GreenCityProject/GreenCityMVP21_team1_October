package greencity.service;

import greencity.dto.user.UserVO;
import greencity.exception.exceptions.FavoritePlaceNotFoundException;
import greencity.repository.FavoritePlaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FavoritePlaceServiceImpl implements FavoritePlaceService {
    private FavoritePlaceRepository favoritePlaceRepository;

    @Transactional
    @Override
    public long deleteFavoritePlaceById(Long id, UserVO userVO) {
        if (existsByEmailAndId(userVO.getEmail(), id)) {
            favoritePlaceRepository.deleteByUserEmailAndPlaceId(userVO.getEmail(), id);
        }
        return id;
    }

    @Override
    public boolean existsByEmailAndId(String userEmail, long placeId) {
        boolean exists = favoritePlaceRepository.existsByEmailAndId(userEmail, placeId);
        if (!exists) {
            throw new FavoritePlaceNotFoundException("Favorite place with such place id and user email does not exist!");
        }
        return true;
    }

}
