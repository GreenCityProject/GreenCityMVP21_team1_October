package greencity.dto.place;

import greencity.dto.location.LocationDto;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterPlaceResponseDto {
    private Long id;
    private String name;
    private LocationDto location;
}