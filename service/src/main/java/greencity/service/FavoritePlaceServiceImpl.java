package greencity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.dto.location.LocationDto;
import greencity.dto.place.FavoritePlaceDto;
import greencity.dto.place.PlaceByBoundsDto;
import greencity.dto.place.PlaceInfoDto;
import greencity.dto.user.UserVO;
import greencity.exception.exceptions.FavoritePlaceNotFoundException;
import greencity.repository.FavoritePlaceRepository;
import greencity.repository.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FavoritePlaceServiceImpl implements FavoritePlaceService {
    private FavoritePlaceRepository favoritePlaceRepository;
    private LocationRepository locationRepository;
    private PlaceService placeService;
    private ObjectMapper objectMapper;

    @Transactional
    @Override
    public long deleteFavoritePlaceById(Long id, UserVO userVO) {
        if (existsByEmailAndPlaceId(userVO.getEmail(), id)) {
            favoritePlaceRepository.deleteByUserEmailAndPlaceId(userVO.getEmail(), id);
        }
        return id;
    }

    @Override
    public boolean existsByEmailAndPlaceId(String userEmail, long placeId) {
        if (!favoritePlaceRepository.existsByEmailAndId(userEmail, placeId)) {
            throw new FavoritePlaceNotFoundException("Favorite place with such place id and user email does not exist!");
        }
        return true;
    }

    @Transactional
    @Override
    public PlaceByBoundsDto getFavoriteByUserAndPlaceId(UserVO userVO, long id) {
        if (existsByEmailAndPlaceId(userVO.getEmail(), id)) {
            return getFavoritePlace(id, userVO);
        }
        // Workaround. Basically, you will never be here,
        // 'cause existsByEmailAndPlaceId throws exception if not exists
        throw new RuntimeException("You aren't suppose to be here. How did you do this?");
    }

    @Transactional
    @Override
    public List<PlaceByBoundsDto> getAllFavoritesByUser(UserVO userVO) {
        List<Long> ids = favoritePlaceRepository.getAllByEmail(userVO.getEmail());
        if (ids.isEmpty()) {
            throw new FavoritePlaceNotFoundException("Favorite place with such place id and user email does not exist!");
        }
        List<PlaceByBoundsDto> places = new ArrayList<>();
        for (Long id : ids) {
            places.add(getFavoritePlace(id, userVO));
        }
        return places;
    }

    @Override
    public String getFavoritePlaceName(long placeId, UserVO userVO) {
        return favoritePlaceRepository.getFavoritePlaceNameByIdAndEmail(placeId, userVO.getEmail());
    }

    @Transactional
    @Override
    public void updateFavoritePlace(FavoritePlaceDto favoritePlaceDto, UserVO userVO) {
        if (existsByEmailAndPlaceId(userVO.getEmail(), favoritePlaceDto.getPlaceId())) {
            favoritePlaceRepository.updateFavoritePlace(
                    favoritePlaceDto.getPlaceId(),
                    favoritePlaceDto.getName(),
                    userVO.getEmail()
            );
        }
    }

    @Transactional
    @Override
    public PlaceInfoDto getFavoritePlaceInfoByUserAndPlaceId(UserVO userVO, long placeId) {
        if (existsByEmailAndPlaceId(userVO.getEmail(), placeId)) {
            return placeService.getPlaceInfo(placeId);
        }
        throw new RuntimeException("You aren't suppose to be here. How did you do this?");
    }

    private PlaceByBoundsDto getFavoritePlace(long placeId, UserVO userVO) {
        PlaceByBoundsDto place = new PlaceByBoundsDto();
        place.setId(placeId);
        place.setName(getFavoritePlaceName(placeId, userVO));
        LocationDto location = objectMapper.convertValue(
                locationRepository.getLocationByUserEmailAndPlaceId(userVO.getEmail(), placeId),
                LocationDto.class
        );
        place.setLocationDto(location);
        return place;
    }
}
