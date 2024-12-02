package greencity.service;

import greencity.ModelUtils;
import greencity.dto.PageableDto;
import greencity.dto.place.*;
import greencity.dto.user.UserVO;
import greencity.entity.*;
import greencity.enums.PlaceStatus;
import greencity.enums.WeekDay;
import greencity.exception.exceptions.BadPlaceRequestException;
import greencity.exception.exceptions.BadRequestException;
import greencity.exception.exceptions.NotFoundException;
import greencity.exception.exceptions.WrongIdException;
import greencity.filters.FilterPlaceCategory;
import greencity.mapping.PlaceInfoDtoMapper;
import greencity.mapping.PlaceUpdateDtoMapper;
import greencity.repository.CategoryRepo;
import greencity.repository.LocationRepository;
import greencity.repository.PlaceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlaceServiceImplTest {

    @InjectMocks
    private PlaceServiceImpl placeService;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PlaceInfoDtoMapper placeInfoDtoMapper;

    @Mock
    private PlaceUpdateDtoMapper placeUpdateDtoMapper;

    @Test
    void getPlaceInfoTest() {
        Place place = ModelUtils.getPlace();

        PlaceInfoDto expectedDto = new PlaceInfoDto();

        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));
        when(placeInfoDtoMapper.convert(place)).thenReturn(expectedDto);

        PlaceInfoDto result = placeService.getPlaceInfo(place.getId());

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(placeRepository).findById(place.getId());
        verify(placeInfoDtoMapper).convert(place);
    }

    @Test
    void getPlaceInfoTestThrowsNotFoundException() {
        Place place = ModelUtils.getPlace();

        when(placeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> placeService.getPlaceInfo(place.getId()));

        verify(placeRepository).findById(anyLong());
        verifyNoInteractions(placeInfoDtoMapper);
    }

    @Test
    void getPlaceTest() {
        Place place = ModelUtils.getPlace();

        PlaceUpdateDto expectedDto = Mockito.mock(PlaceUpdateDto.class);

        when(placeRepository.findById(place.getId())).thenReturn(Optional.of(place));
        when(placeUpdateDtoMapper.convert(place)).thenReturn(expectedDto);

        PlaceUpdateDto result = placeService.getPlace(place.getId());

        assertEquals(expectedDto, result);
        verify(placeRepository).findById(place.getId());
        verify(placeUpdateDtoMapper).convert(place);
    }

    @Test
    void getPlaceTestThrowsNotFoundException() {
        Place place = ModelUtils.getPlace();

        when(placeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> placeService.getPlace(place.getId()));

        verify(placeRepository).findById(anyLong());
        verifyNoInteractions(placeInfoDtoMapper);
    }

    @Test
    void getPlacesTest() {
        PlaceStatus status = PlaceStatus.APPROVED;
        Pageable pageable = Pageable.unpaged();

        Place place = new Place();
        PlaceInfoDto dto = new PlaceInfoDto();

        List<Place> places = List.of(place);
        Page<Place> placesPage = new PageImpl<>(places);

        when(placeRepository.findPlacesByStatus(status, pageable)).thenReturn(placesPage);
        when(placeInfoDtoMapper.convert(place)).thenReturn(dto);

        PageableDto<PlaceInfoDto> result = placeService.getPlaces(status, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(placeRepository).findPlacesByStatus(status, pageable);
    }

    @Test
    void getStatusesTest() {
        List<String> expected = Arrays.stream(PlaceStatus.values())
                .map(Enum::name)
                .toList();
        List<String> actual = placeService.getStatuses();

        assertIterableEquals(expected, actual);
    }

    @Test
    void getFilteredPlacesCategoriesTest() {
        Place place = ModelUtils.getPlace();

        List<Place> places = List.of(place);
        List<FilterPlaceCategory> expected = List.of(new FilterPlaceCategory(place.getId(), place.getCategory().getName(), place.getCategory().getNameUa()));

        when(placeRepository.findAll()).thenReturn(places);

        List<FilterPlaceCategory> actual = placeService.getFilteredPlacesCategories();
        assertIterableEquals(expected, actual);
    }

    @Test
    void bulkDeletePlacesTest() {
        List<String> ids = List.of("1", "2", "3");
        List<Long> idList = ids.stream().map(Long::valueOf).toList();
        String idsStr = String.join(",", ids);
        long initialAmountOfPlaces = 10L;

        when(placeRepository.count()).thenReturn(initialAmountOfPlaces, initialAmountOfPlaces - ids.size());

        Long result = placeService.bulkDeletePlaces(idsStr);

        assertEquals(ids.size(), result);
        verify(placeRepository).deleteAllById(idList);
    }

    @Test
    void bulkDeletePlacesTestThrowsWrongIdException() {
        List<String> ids = List.of("1", "2", "3a");
        String idsStr = String.join(",", ids);

        assertThrows(WrongIdException.class, () -> placeService.bulkDeletePlaces(idsStr));
    }

    @Test
    void bulkUpdatePlaceStatusTest() {
        List<Long> ids = List.of(1L, 2L);
        BulkUpdatePlaceStatusDto bulkUpdatePlaceStatusDto = new BulkUpdatePlaceStatusDto(ids, PlaceStatus.APPROVED);

        placeService.bulkUpdatePlaceStatus(bulkUpdatePlaceStatusDto);

        verify(placeRepository, times(1)).updatePlacesStatus(bulkUpdatePlaceStatusDto.getIds(), bulkUpdatePlaceStatusDto.getStatus());
    }

    @Test
    void deletePlaceTest() {
        Long placeId = 1L;

        when(placeRepository.existsById(placeId)).thenReturn(true);

        Long result = placeService.deletePlace(placeId);

        assertEquals(placeId, result);
        verify(placeRepository).deleteById(placeId);
    }

    @Test
    void deletePlaceTestThrowsBadRequestException() {
        Long placeId = 1L;

        when(placeRepository.existsById(placeId)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> placeService.deletePlace(placeId));
    }

    @Test
    void proposePlaceTest() {
        Place place = ModelUtils.getPlace();
        PlaceAddDto placeAddDto = ModelUtils.getPlaceAddDto();
        Category category = ModelUtils.getCategory();
        OpenHours openHours = new OpenHours(1L, WeekDay.FRIDAY, Time.valueOf(LocalTime.now()), Time.valueOf(LocalTime.now()), BreakTime.builder().build(), place);

        when(placeRepository.findPlaceByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepo.findByName(anyString())).thenReturn(category);
        when(placeRepository.save(any())).thenReturn(place);
        when(modelMapper.map(any(OpeningHoursDto.class), eq(OpenHours.class))).thenReturn(openHours);

        placeService.proposePlace(placeAddDto);

        verify(locationRepository, times(1)).save(any(Location.class));
        verify(placeRepository, times(1)).save(any(Place.class));
        verify(modelMapper, times(1)).map(any(Place.class), eq(PlaceWithUserDto.class));
    }

    @Test
    void proposePlaceTestUnableToMapOpeningHoursDtoToOpenHours() {
        PlaceAddDto placeAddDto = ModelUtils.getPlaceAddDto();
        Category category = ModelUtils.getCategory();

        when(placeRepository.findPlaceByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepo.findByName(anyString())).thenReturn(category);
        when(modelMapper.map(any(OpeningHoursDto.class), eq(OpenHours.class))).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> placeService.proposePlace(placeAddDto));
    }

    @Test
    void proposePlaceTestThrowsBadRequestExceptionWhenCategoryNotFound() {
        Place place = ModelUtils.getPlace();
        PlaceAddDto placeAddDto = ModelUtils.getPlaceAddDto();

        when(placeRepository.findPlaceByName(anyString())).thenReturn(Optional.of(place));

        assertThrows(BadPlaceRequestException.class, () -> placeService.proposePlace(placeAddDto));
    }

    @Test
    void proposePlaceTestThrowsBadPlaceRequestException() {
        Place place = ModelUtils.getPlace();
        PlaceAddDto placeAddDto = ModelUtils.getPlaceAddDto();

        when(placeRepository.findPlaceByName(anyString())).thenReturn(Optional.of(place));
        assertThrows(BadPlaceRequestException.class, () -> placeService.proposePlace(placeAddDto));
    }

    @Test
    void saveTest() {
        UserVO userVO = ModelUtils.getUserVO();
        AddPlaceDto addPlaceDto = ModelUtils.getAddPlaceDto();
        Category category = ModelUtils.getCategory();

        when(placeRepository.findPlaceByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepo.findByName(addPlaceDto.getCategoryName())).thenReturn(category);

        placeService.save(addPlaceDto, userVO);

        verify(locationRepository, times(1)).save(any());
        verify(placeRepository, times(1)).save(any());
    }

    @Test
    void saveTestThrowsExceptionOnCategoryNotFound() {
        UserVO userVO = ModelUtils.getUserVO();
        AddPlaceDto addPlaceDto = ModelUtils.getAddPlaceDto();

        when(placeRepository.findPlaceByName(anyString())).thenReturn(Optional.empty());
        when(categoryRepo.findByName(addPlaceDto.getCategoryName())).thenReturn(null);

        assertThrows(Exception.class, () -> placeService.save(addPlaceDto, userVO));
    }

    @Test
    void saveTestThrowsBadPlaceRequestException() {
        UserVO userVO = ModelUtils.getUserVO();
        AddPlaceDto addPlaceDto = ModelUtils.getAddPlaceDto();
        Place place = ModelUtils.getPlace();

        when(placeRepository.findPlaceByName(anyString())).thenReturn(Optional.of(place));

        assertThrows(BadPlaceRequestException.class, () -> placeService.save(addPlaceDto, userVO));
    }

    @Test
    void saveAsFavoritePlace_shouldSaveFavoritePlace() {
        // Arrange
        long placeId = 1L;
        FavoritePlaceDto favoritePlaceDto = new FavoritePlaceDto("My Favorite Place", placeId);
        Place place = new Place();
        place.setId(placeId);
        place.setFavorite(false);

        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));
        when(placeRepository.save(any(Place.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(modelMapper.map(place, FavoritePlaceDto.class)).thenReturn(favoritePlaceDto);

        // Act
        FavoritePlaceDto result = placeService.saveAsFavoritePlace(favoritePlaceDto);

        // Assert
        assertNotNull(result);
        assertTrue(place.isFavorite());
        assertEquals(favoritePlaceDto.getName(), result.getName());
        assertEquals(favoritePlaceDto.getPlaceId(), result.getPlaceId());

        verify(placeRepository, times(1)).findById(placeId);
        verify(placeRepository, times(1)).save(place);
        verify(modelMapper, times(1)).map(place, FavoritePlaceDto.class);
    }

    @Test
    void saveAsFavoritePlace_shouldThrowNotFoundExceptionIfPlaceNotFound() {
        // Arrange
        long placeId = 1L;
        FavoritePlaceDto favoritePlaceDto = new FavoritePlaceDto("My Favorite Place", placeId);

        when(placeRepository.findById(placeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> placeService.saveAsFavoritePlace(favoritePlaceDto));

        verify(placeRepository, times(1)).findById(placeId);
        verify(placeRepository, never()).save(any(Place.class));
    }
}

