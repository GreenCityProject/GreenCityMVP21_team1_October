package greencity.service;

import greencity.dto.place.PlaceInfoDto;
import greencity.dto.place.PlaceUpdateDto;

public interface PlaceService {

    PlaceInfoDto getPlaceInfo(Long id);
    PlaceUpdateDto getPlace(Long id);
}
