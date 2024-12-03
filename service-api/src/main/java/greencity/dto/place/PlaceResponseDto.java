package greencity.dto.place;

import java.util.List;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponseDto {
    private CategoryDto category;
    private AddPlaceLocationDto locationAddressAndGeoDto;
    private List<OpenHoursDto> openingHoursList;
    private String placeName;
}
