package greencity.mapping;

import greencity.dto.location.LocationDto;
import greencity.entity.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class LocationDtoMapperTest {
    @Test
    void testLocationDtoMapper() {
        Location location = Location.builder()
                .id(1L)
                .address("Test Address")
                .lat(10.0)
                .lng(20.0)
                .build();

        LocationDtoMapper mapper = new LocationDtoMapper();
        LocationDto dto = mapper.convert(location);

        assertNotNull(dto);
        assertEquals(location.getId(), dto.getId());
        assertEquals(location.getAddress(), dto.getAddress());
        assertEquals(location.getLat(), dto.getLat());
        assertEquals(location.getLng(), dto.getLng());
    }

}
