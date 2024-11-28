package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.ModelUtils;
import greencity.converters.UserArgumentResolver;
import greencity.dto.place.AddPlaceDto;
import greencity.dto.place.BulkUpdatePlaceStatusDto;
import greencity.dto.place.FilterPlaceDto;
import greencity.dto.place.PlaceAddDto;
import greencity.dto.user.UserVO;
import greencity.enums.PlaceStatus;
import greencity.exception.handler.CustomExceptionHandler;
import greencity.service.PlaceService;
import greencity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PlaceControllerTest {

    private final String placeLink = "/place";

    @InjectMocks
    private PlaceController placeController;

    @Mock
    private PlaceService placeService;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;

    private final ErrorAttributes errorAttributes = new DefaultErrorAttributes();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(placeController)
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver(),
                        new UserArgumentResolver(userService, modelMapper)
                ).setControllerAdvice(new CustomExceptionHandler(errorAttributes, objectMapper))
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void getPlaceInfoTest() throws Exception {
        Long id = 1L;

        mockMvc.perform(get(placeLink + "/info/" + id))
                .andExpect(status().isOk());

        verify(placeService).getPlaceInfo(id);
    }

    @Test
    void getPlaceTest() throws Exception {
        Long id = 1L;

        mockMvc.perform(get(placeLink + "/about/" + id))
                .andExpect(status().isOk());

        verify(placeService).getPlace(id);
    }

    @Test
    void getPlacesTest() throws Exception {
        PlaceStatus placeStatus = PlaceStatus.APPROVED;
        Pageable pageable = PageRequest.of(0, 20);

        mockMvc.perform(get(placeLink + "/" + placeStatus.name()))
                .andExpect(status().isOk());

        verify(placeService).getPlaces(placeStatus, pageable);
    }

    @Test
    void getStatusesTest() throws Exception {
        mockMvc.perform(get(placeLink + "/statuses"))
                .andExpect(status().isOk());

        verify(placeService).getStatuses();
    }

    @Test
    void getFilteredPlacesCategoriesTest() throws Exception {
        mockMvc.perform(get(placeLink + "/v2/filteredPlacesCategories"))
                .andExpect(status().isOk());

        verify(placeService).getFilteredPlacesCategories();
    }

    @Test
    void bulkDeletePlacesTest() throws Exception {
        String ids = "1, 2, 3";

        mockMvc.perform(delete(placeLink)
                        .param("ids", ids))
                .andExpect(status().isOk());
        verify(placeService).bulkDeletePlaces(ids);
    }

    @Test
    void deletePlaceTest() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete(placeLink + "/" + id))
                .andExpect(status().isOk());

        verify(placeService).deletePlace(id);
    }

    @Test
    void bulkUpdatePlaceStatusTest() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        BulkUpdatePlaceStatusDto bulkUpdatePlaceStatusDto = new BulkUpdatePlaceStatusDto(ids, PlaceStatus.APPROVED);

        String json = objectMapper.writeValueAsString(bulkUpdatePlaceStatusDto);

        mockMvc.perform(patch(placeLink + "/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(placeService).bulkUpdatePlaceStatus(bulkUpdatePlaceStatusDto);
    }

    @Test
    void saveEcoPlaceFromUiUsingTest() throws Exception {
        AddPlaceDto addPlaceDto = new AddPlaceDto("name", "name", List.of(), "name");
        UserVO userVO = ModelUtils.getUserVO();

        when(userService.findByEmail(anyString())).thenReturn(userVO);

        String json = objectMapper.writeValueAsString(addPlaceDto);

        mockMvc.perform(post(placeLink + "/v2/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .principal(userVO::getEmail))
                .andExpect(status().isOk());

        verify(placeService).save(addPlaceDto, userVO);
    }

    @Test
    void getFilteredPlacesUsingTest() throws Exception {
        UserVO userVO = ModelUtils.getUserVO();
        FilterPlaceDto filterPlaceDto = new FilterPlaceDto();

        String json = objectMapper.writeValueAsString(filterPlaceDto);

        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(post(placeLink + "/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .principal(userVO::getEmail))
                .andExpect(status().isOk());

        verify(placeService).getFilteredPlaces(filterPlaceDto, userVO);
    }

    @Test
    void filterPlaceBySearchPredicateUsingTest() throws Exception {
        UserVO userVO = ModelUtils.getUserVO();
        FilterPlaceDto filterPlaceDto = new FilterPlaceDto();
        Pageable pageable = PageRequest.of(0, 20);

        String json = objectMapper.writeValueAsString(filterPlaceDto);

        when(userService.findByEmail(anyString())).thenReturn(userVO);

        mockMvc.perform(post(placeLink + "/filter/predicate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .principal(userVO::getEmail))
                .andExpect(status().isOk());

        verify(placeService).getFilteredPlaces(filterPlaceDto, userVO, pageable);
    }

    @Test
    void proposePlaceTest() throws Exception {
        PlaceAddDto placeAddDto = new PlaceAddDto();

        String json = objectMapper.writeValueAsString(placeAddDto);

        mockMvc.perform(post(placeLink + "/propose")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(placeService).proposePlace(placeAddDto);
    }



}