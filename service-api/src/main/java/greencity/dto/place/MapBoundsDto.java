package greencity.dto.place;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import static greencity.constant.ServiceValidationConstants.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapBoundsDto {
    @Min(value = -90, message = LAT_MIN_VALIDATION)
    @Max(value = 90, message = LAT_MAX_VALIDATION)
    private Double northEastLat;

    @Min(value = -90, message = LAT_MIN_VALIDATION)
    @Max(value = 90, message = LAT_MAX_VALIDATION)
    private Double southWestLat;

    @Min(value = -180, message = LNG_MIN_VALIDATION)
    @Max(value = 180, message = LNG_MAX_VALIDATION)
    private Double northEastLng;

    @Min(value = -180, message = LNG_MIN_VALIDATION)
    @Max(value = 180, message = LNG_MAX_VALIDATION)
    private Double southWestLng;
}
