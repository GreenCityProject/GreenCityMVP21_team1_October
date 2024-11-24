package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.PageableDto;
import greencity.dto.place.PlaceInfoDto;
import greencity.dto.place.PlaceUpdateDto;
import greencity.entity.Place;
import greencity.enums.PlaceStatus;
import greencity.exception.exceptions.NotFoundException;
import greencity.filters.FilterPlaceCategory;
import greencity.mapping.PlaceInfoDtoMapper;
import greencity.mapping.PlaceUpdateDtoMapper;
import greencity.repository.PlaceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository placeRepository;

    private PlaceInfoDtoMapper placeInfoDtoMapper;
    private PlaceUpdateDtoMapper placeUpdateDtoMapper;

    @Override
    public PlaceInfoDto getPlaceInfo(Long id) {
        return placeInfoDtoMapper.convert(placeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.PLACE_NOT_FOUND_BY_ID + id)));
    }

    @Override
    public PlaceUpdateDto getPlace(Long id) {
        return placeUpdateDtoMapper.convert(placeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.PLACE_NOT_FOUND_BY_ID + id)));
    }

    @Override
    public PageableDto<PlaceInfoDto> getPlaces(PlaceStatus status, Pageable page) {
        Page<Place> placesPage = placeRepository.findPlacesByStatus(status, page);
        List<PlaceInfoDto> placeInfoDtoList = placesPage.getContent().stream()
                .map(e -> placeInfoDtoMapper.convert(e))
                .toList();
        return new PageableDto<>(placeInfoDtoList, placesPage.getTotalElements(),
                placesPage.getNumber(), placesPage.getTotalPages());
    }

    @Override
    public List<String> getStatuses() {
        return Arrays.stream(PlaceStatus.values())
                .map(Enum::name)
                .toList();
    }

    @Override
    public List<FilterPlaceCategory> getFilteredPlacesCategories() {
        return placeRepository.findAll().stream()
                .map(e -> FilterPlaceCategory.builder()
                        .id(e.getId())
                        .name(e.getCategory().getName())
                        .nameUa(e.getCategory().getNameUa())
                        .build())
                .toList();
    }
}
