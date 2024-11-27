package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.PageableDto;
import greencity.dto.place.*;
import greencity.entity.Place;
import greencity.enums.PlaceStatus;
import greencity.dto.user.UserVO;
import greencity.entity.Location;
import greencity.entity.OpenHours;
import greencity.entity.User;
import greencity.exception.exceptions.BadPlaceRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.WrongIdException;
import greencity.filters.FilterPlaceCategory;
import greencity.mapping.PlaceInfoDtoMapper;
import greencity.mapping.PlaceUpdateDtoMapper;
import greencity.repository.CategoryRepo;
import greencity.repository.LocationRepository;
import greencity.repository.PlaceRepository;
import jakarta.transaction.Transactional;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private PlaceRepository placeRepository;
    private CategoryRepo categoryRepo;
    private LocationRepository locationRepository;
    private ModelMapper modelMapper;

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

    @Override
    @Transactional
    public Long bulkDeletePlaces(String ids) {
        List<Long> idList;
        try {
            idList = Arrays.stream(ids.split(","))
                    .map(Long::valueOf)
                    .toList();
        } catch (NumberFormatException e) {
            throw new WrongIdException(ErrorMessage.WRONG_ID_LIST);
        }
        long countBefore = placeRepository.count();
        placeRepository.deleteAllById(idList);
        return countBefore - placeRepository.count();
    }

    @Override
    @Transactional
    public List<UpdatePlaceStatusDto> bulkUpdatePlaceStatus(BulkUpdatePlaceStatusDto dto) {
        placeRepository.updatePlacesStatus(dto.getIds(), dto.getStatus());
        return dto.getIds().stream()
                .map(id -> new UpdatePlaceStatusDto(id, dto.getStatus()))
                .toList();
    }

    @Override
    public PlaceResponseDto save(AddPlaceDto placeDto, UserVO userVO) {
        if (placeRepository.findPlaceByName(placeDto.getPlaceName()).isPresent()) {
            throw new BadPlaceRequestException(ErrorMessage.PLACE_ALREADY_EXISTS);
        }

        Place place = new Place();
        place.setCategory(Optional.ofNullable(categoryRepo.findByName(placeDto.getCategoryName())).orElseThrow());
        place.setName(placeDto.getPlaceName());
        place.setAuthor(modelMapper.map(userVO, User.class));
        place.setStatus(PlaceStatus.PROPOSED);
        place.setOpenHoursList(
                placeDto.getOpeningHoursList().stream().map(row -> modelMapper.map(row, OpenHours.class).setPlace(place))
                        .toList());

        //todo: provide separate service for converting address to geo lat and lng
        Location location = locationRepository.save(Location
                .builder()
                .address(placeDto.getLocationName())
                .lng(0D)
                .lat(0D)
                .build());
        place.setLocation(location);
        place.setRate(0D);

        return modelMapper.map(
                placeRepository.save(place),
                PlaceResponseDto.class);
    }
}
