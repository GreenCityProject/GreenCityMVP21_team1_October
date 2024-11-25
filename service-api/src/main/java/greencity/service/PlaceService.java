package greencity.service;

import greencity.dto.PageableDto;
import greencity.dto.place.AddPlaceDto;
import greencity.dto.place.PlaceInfoDto;
import greencity.dto.place.PlaceUpdateDto;
import greencity.enums.PlaceStatus;

import greencity.filters.FilterPlaceCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;
import greencity.dto.place.PlaceResponseDto;
import greencity.dto.user.UserVO;

public interface PlaceService {
    PlaceInfoDto getPlaceInfo(Long id);
    PlaceUpdateDto getPlace(Long id);
    PageableDto<PlaceInfoDto> getPlaces(PlaceStatus status, Pageable page);
    List<String> getStatuses();
    List<FilterPlaceCategory> getFilteredPlacesCategories();
    Long bulkDeletePlaces(String ids);
    PlaceResponseDto save(AddPlaceDto placeDto, UserVO userVO);
}
