package greencity.dto.place;

import greencity.dto.location.LocationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceByBoundsDto {
    private long id;
    private LocationDto locationDto;
    private String name;
}
