package greencity.service;

import greencity.constant.ErrorMessage;
import greencity.dto.PageableAdvancedDto;
import greencity.dto.PageableDto;
import greencity.dto.location.LocationDto;
import greencity.dto.place.*;
import greencity.dto.user.UserVO;
import greencity.entity.*;
import greencity.enums.PlaceStatus;
import greencity.exception.exceptions.BadPlaceRequestException;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.WrongIdException;
import greencity.filters.FilterPlace;
import greencity.filters.FilterPlaceCategory;
import greencity.filters.PlaceSpecification;
import greencity.filters.SearchCriteria;
import greencity.mapping.PlaceInfoDtoMapper;
import greencity.mapping.PlaceUpdateDtoMapper;
import greencity.repository.CategoryRepo;
import greencity.repository.FavoritePlaceRepository;
import greencity.repository.LocationRepository;
import greencity.repository.PlaceRepository;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final FavoritePlaceRepository favoritePlaceRepository;
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
    public Long deletePlace(Long id) {
        if (!placeRepository.existsById(id)) {
            throw new BadRequestException(ErrorMessage.PLACE_NOT_FOUND_BY_ID + id);
        }
        placeRepository.deleteById(id);
        return id;
    }

    @Override
    public List<PlaceByBoundsDto> getListPlaceLocationByMapsBounds(FilterPlaceDto dto) {
        log.info("N-E latitude : " + dto.getMapBoundsDto().getNorthEastLat());
        log.info("N-E longitude : " + dto.getMapBoundsDto().getNorthEastLng());
        log.info("S-W latitude : " + dto.getMapBoundsDto().getSouthWestLat());
        log.info("S-W longitude : " + dto.getMapBoundsDto().getSouthWestLng());
        Double testLat = ((dto.getMapBoundsDto().getNorthEastLat() + dto.getMapBoundsDto().getSouthWestLat()) / 2);
        Double testLng = ((dto.getMapBoundsDto().getNorthEastLng() + dto.getMapBoundsDto().getSouthWestLng()) / 2);
        return List.of(PlaceByBoundsDto.builder()
                        .id(1L)
                        .location(LocationDto.builder()
                                .id(101L)
                                .address("Location response address")
                                .lat(testLat)
                                .lng(testLng)
                                .build())
                        .name("Place response name")
                .build());
    }

    @Override
    @Transactional
    public PlaceWithUserDto proposePlace(PlaceAddDto placeAddDto) {
        if (placeRepository.findPlaceByName(placeAddDto.getName()).isPresent()) {
            throw new BadPlaceRequestException(ErrorMessage.PLACE_ALREADY_EXISTS);
        }
        Place place = new Place();
        place.setName(placeAddDto.getName());
        place.setCategory(Optional.ofNullable(categoryRepo.findByName(placeAddDto.getCategory().getName()))
                .orElseThrow(() -> new BadRequestException(ErrorMessage.CATEGORY_NOT_FOUND_BY_NAME)));
        Location location = locationRepository.save(Location.builder()
                .address(placeAddDto.getLocation().getAddress())
                .lat(placeAddDto.getLocation().getLat())
                .lng(placeAddDto.getLocation().getIng())
                .build());
        place.setLocation(location);
        place.setOpenHoursList(
                placeAddDto.getOpeningHoursList().stream()
                        .map(row -> {
                            OpenHours openHours = modelMapper.map(row, OpenHours.class);
                            if (openHours == null) {
                                throw new IllegalArgumentException("Failed to map OpeningHoursDto to OpenHours");
                            }
                            openHours.setPlace(place);
                            return openHours;
                        })
                        .toList());
        place.setPhotos(placeAddDto.getPhotos().stream()
                .map(photoAddDto -> {
                    Photo photo = new Photo();
                    photo.setName(photoAddDto.getName());
                    photo.setPlace(place);
                    return photo;
                })
                .toList());
        place.setStatus(PlaceStatus.PROPOSED);
        Place savedPlace = placeRepository.save(place);
        return modelMapper.map(savedPlace, PlaceWithUserDto.class);
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
        place.setStatus(PlaceStatus.APPROVED);
        place.setOpenHoursList(
            placeDto.getOpeningHoursList()
                .stream()
                .map(row -> modelMapper.map(row, OpenHours.class).setPlace(place))
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

    @Override
    public List<FilterPlaceResponseDto> getFilteredPlaces(FilterPlaceDto filterPlaceDto, UserVO userVO) {
        return placeRepository.findAll(getSpecification(filterPlaceDto)).stream()
            .map(place -> modelMapper.map(place, FilterPlaceResponseDto.class)).toList();
    }

    @Override
    @Transactional
    public FavoritePlaceDto saveAsFavoritePlace(FavoritePlaceDto favoritePlaceDto) {
        Place place = placeRepository.findById(favoritePlaceDto.getPlaceId())
                .orElseThrow(() -> new NotFoundException("Place not found with id: " + favoritePlaceDto.getPlaceId()));
        place.setFavorite(true);
        placeRepository.save(place);
        return modelMapper.map(place, FavoritePlaceDto.class);
    }

    @Override
    public PageableDto<FilterPlaceResponseDto> getFilteredPlaces(FilterPlaceDto filterPlaceDto, UserVO userVO,
                                                                         Pageable page) {
        Page<Place> pageWithPlaces = placeRepository.findAll(getSpecification(filterPlaceDto), page);
        return PageableDto.<FilterPlaceResponseDto>builder()
            .currentPage(pageWithPlaces.getNumber())
            .totalElements(pageWithPlaces.getTotalElements())
            .totalPages(pageWithPlaces.getTotalPages())
            .page(
                pageWithPlaces
                    .getContent()
                    .stream()
                    .map(place -> modelMapper.map(place, FilterPlaceResponseDto.class))
                    .toList())
            .build();
    }

    PlaceSpecification getSpecification(FilterPlaceDto filterPlaceDto) {
        return new PlaceSpecification(modelMapper.map(filterPlaceDto, FilterPlace.class));
    }
}
