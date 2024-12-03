package greencity.dto.place;

import greencity.dto.location.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PlaceByBoundsDto {
    private Long id;
    private LocationDto location;
    private String name;
}
