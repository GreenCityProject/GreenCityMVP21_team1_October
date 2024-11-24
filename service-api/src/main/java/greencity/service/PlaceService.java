package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.place.PlaceInfoDto;
import greencity.dto.place.PlaceUpdateDto;
import greencity.enums.PlaceStatus;

import org.springframework.data.domain.Pageable;

public interface PlaceService {

    PlaceInfoDto getPlaceInfo(Long id);
    PlaceUpdateDto getPlace(Long id);
    PageableDto<PlaceInfoDto> getPlaces(PlaceStatus status, Pageable page);
}
