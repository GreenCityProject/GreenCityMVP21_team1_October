package greencity.service;

import greencity.dto.PageableAdvancedDto;
import greencity.dto.PageableDto;
import greencity.dto.place.*;
import greencity.dto.user.UserVO;
import greencity.enums.PlaceStatus;
import greencity.filters.FilterPlaceCategory;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface PlaceService {
    PlaceInfoDto getPlaceInfo(Long id);

    PlaceUpdateDto getPlace(Long id);

    PageableDto<PlaceInfoDto> getPlaces(PlaceStatus status, Pageable page);

    List<String> getStatuses();

    List<FilterPlaceCategory> getFilteredPlacesCategories();

    Long bulkDeletePlaces(String ids);

    List<UpdatePlaceStatusDto> bulkUpdatePlaceStatus(BulkUpdatePlaceStatusDto dto);

    PlaceResponseDto save(AddPlaceDto placeDto, UserVO userVO);

    List<FilterPlaceResponseDto> getFilteredPlaces(FilterPlaceDto filterPlaceDto, UserVO userVO);

    PageableAdvancedDto<FilterPlaceResponseDto> getFilteredPlaces(FilterPlaceDto filterPlaceDto, UserVO userVO,
                                                                  Pageable page);

    Long deletePlace(Long id);

    PlaceWithUserDto proposePlace(PlaceAddDto placeAddDto);

    FavoritePlaceDto saveAsFavoritePlace(FavoritePlaceDto favoritePlaceDto);
}
