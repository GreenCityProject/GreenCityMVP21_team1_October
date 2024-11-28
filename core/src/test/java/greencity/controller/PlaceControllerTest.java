package greencity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import greencity.dto.place.*;
import greencity.service.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PlaceController.class)
class PlaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceService placeService;

    @Autowired
    private ObjectMapper objectMapper;

    private PlaceAddDto placeAddDto;

    @BeforeEach
    void setUp() {
        placeAddDto = new PlaceAddDto();
        placeAddDto.setName("Test Place");
        placeAddDto.setCategory(new CategoryDto("Test Category", null, null));
        placeAddDto.setLocation(new LocationAddressAndGeoDto("Test Address", 10.0, 20.0));
    }

    @Test
    void proposePlace_SuccessfulCreation() throws Exception {
        // Arrange
        PlaceWithUserDto response = new PlaceWithUserDto();
        Mockito.when(placeService.proposePlace(Mockito.any())).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/place/propose")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeAddDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void proposePlace_InvalidRequest() throws Exception {
        // Arrange
        placeAddDto.setName("");

        // Act & Assert
        mockMvc.perform(post("/place/propose")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(placeAddDto)))
                .andExpect(status().isBadRequest());
    }
}
